package net.ramptors.appengine;

import static java.util.Collections.emptyList;
import static java.util.Collections.emptySet;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import com.google.appengine.api.images.ImagesServiceFactory;
import com.google.appengine.api.images.ServingUrlOptions;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.Query;
import com.google.cloud.storage.Acl;
import com.google.cloud.storage.Acl.Role;
import com.google.cloud.storage.Acl.User;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

public class Util {
	public static final String SEGMENTO = "SEGMENTO";

	public static void procesa(HttpServlet servlet, HttpServletRequest request, HttpServletResponse response,
			Exception e) throws ServletException, IOException {
		request.setAttribute("error", e.getMessage());
		servlet.log("Error", e);
		request.getRequestDispatcher("FormError.jsp").forward(request, response);
	}

	public static boolean isNullOrEmpty(String s) {
		return s == null || s.isEmpty();
	}

	public static byte[] lee(InputStream is) throws IOException {
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();

		int nRead;
		byte[] data = new byte[16384];

		while ((nRead = is.read(data, 0, data.length)) != -1) {
			buffer.write(data, 0, nRead);
		}

		return buffer.toByteArray();
	}

	public static String subeImagen(Part part, String segmento, String nombre) throws IOException {
		final Storage storage = StorageOptions.getDefaultInstance().getService();
		// Bucket bucket = storage.create(BucketInfo.of(segmento));
		// Upload a blob to the newly created bucket
		final BlobInfo blobInfo = BlobInfo.newBuilder(segmento, nombre).build();
		final Blob blob = storage.create(blobInfo, lee(part.getInputStream()));
		storage.createAcl(blob.getBlobId(), Acl.of(User.ofAllUsers(), Role.READER));
		// Create a fixed dedicated URL that points to the GCS hosted file
		final ServingUrlOptions opcionesDeImagen = ServingUrlOptions.Builder
				.withGoogleStorageFileName("/gs/" + segmento + "/" + nombre).secureUrl(true);
		return ImagesServiceFactory.getImagesService().getServingUrl(opcionesDeImagen);

	}

	public static <T extends Entidad> T getRef(Class<T> tipo, DocumentReference ref) {
		try {
			if (ref == null) {
				return null;
			} else {
				final DocumentSnapshot doc = ref.get().get();
				if (doc.exists()) {
					final T modelo = doc.toObject(tipo);
					modelo.setId(doc.getId());
					return modelo;
				} else {
					return null;
				}
			}
		} catch (InterruptedException | ExecutionException e) {
			throw new RuntimeException(e);
		}
	}

	public static <T extends Entidad> List<T> getArr(Class<T> tipo, List<DocumentReference> arr) {
		if (arr == null) {
			return Collections.emptyList();
		} else {
			return arr.stream().map(ref -> getRef(tipo, ref)).filter(obj -> obj != null).collect(Collectors.toList());
		}
	}

	public static DocumentReference getDocRef(Firestore bd, String collection, String id) {
		return isNullOrEmpty(id) ? null : bd.collection(collection).document(id);
	}

	public static List<DocumentReference> getDocRefs(Firestore bd, String collection, String[] ids) {
		return ids == null ? emptyList()
				: Arrays.stream(ids).map(id -> bd.collection(collection).document(id)).collect(Collectors.toList());
	}

	public static <T extends Entidad> List<T> consulta(Class<T> tipo, Query query)
			throws InterruptedException, ExecutionException {
		return query.get().get().getDocuments().stream().map(doc -> {
			final T modelo = doc.toObject(tipo);
			modelo.setId(doc.getId());
			return modelo;
		}).collect(Collectors.toList());
	}

	public static void valida(boolean condición, String mensaje) {
		if (!condición) {
			throw new RuntimeException(mensaje);
		}
	}

	/**
	 * Quita los espacios al inicio y al final. Sustituye cualquier secuencia de
	 * espacios y saltos de línea por un solo espacio.
	 * 
	 * @param texto el texto a procesar.
	 * @returns el texto procesado
	 */
	public static String colapsaEspacios(String texto) {
		return Objects.toString(texto, "").trim().replaceAll("\\s+", " ");
	}

	/**
	 * Prepara un téxto para los algoritmos de búsqueda: Colapsa espacios, convierte
	 * el texto a mayúsculas y finalmente, quita los acentos y tildes.
	 * 
	 * @param texto el texto procesado.
	 */
	public static String preparaParaBúsqueda(String texto) {
		// Sustituye un caracter acentuado por su versión no acentuada y mayúscula.
		return colapsaEspacios(texto).toUpperCase().replace('Á', 'A').replace('É', 'E').replace('Í', 'I')
				.replace('Ó', 'O').replace('Ú', 'U').replace('Ñ', 'N');
	}

	public static <T extends Entidad> List<Option> opciones(String textoDeNull, List<T> lista,
			DocumentReference selección, Function<T, String> fun) {
		final List<Option> resultado = new ArrayList<>();
		if (textoDeNull != null) {
			resultado.add(new Option("", selección == null, textoDeNull));
		}
		resultado.addAll(lista
				.stream().map(it -> new Option(it.getId(),
						selección != null && Objects.equals(it.getId(), selección.getId()), fun.apply(it)))
				.collect(Collectors.toList()));
		return resultado;
	}

	public static <T extends Entidad> Option[] opciones(List<T> lista, List<DocumentReference> selección,
			Function<T, String> fun) {
		final Set<String> set = selección == null ? emptySet()
				: selección.stream().map(s -> s.getId()).collect(Collectors.toSet());
		return lista.stream().map(it -> new Option(it.getId(), set.contains(it.getId()), fun.apply(it)))
				.toArray(Option[]::new);
	}
}