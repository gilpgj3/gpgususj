package net.ramptors.appengine;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

import com.appspot.gpgususj.Fire;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.QuerySnapshot;

public class Dao {
	public final CollectionReference collection;
	public final Unique[] uniques;
	public final Foreign[] requeridoPor;

	/**
	 * Inicializa una instancia.
	 * 
	 * @param nombre       nombre de la collection.
	 * @param uniques      conjunto de reglas unique, cada una de las cuales es un
	 *                     conjunto de campos.
	 * @param requeridoPor conjunto de collection que hacen referencia a esta
	 *                     collection y los nombres de sus llaves foráneas.
	 */
	public Dao(Firestore bd, String nombre, Unique[] uniques, Foreign[] requeridoPor) {
		this.collection = bd.collection(nombre);
		/**
		 * conjunto de reglas unique, cada una de las cuales es un conjunto de campos.
		 */
		this.uniques = uniques;
		/**
		 * conjunto de collection que hacen referencia a esta collection y los nombres
		 * de sus llaves foráneas.
		 */
		this.requeridoPor = requeridoPor;
	}

	/**
	 * agrega el modelo a la base de datos y devuelve el nuevo id.
	 * 
	 * @param modelo
	 * @return referencia a los datos registrados.
	 * @throws ExecutionException
	 * @throws InterruptedException
	 */
	public DocumentReference agrega(Entidad modelo) throws InterruptedException, ExecutionException {
		final Map<String, Object> map = this.revisaUniquesAlAgregar(modelo);
		return this.collection.add(map).get();
	}

	/**
	 * actualiza el modelo en la base de datos.
	 * 
	 * @param id     id del modelo
	 * @param modelo datos a modificar.
	 * @throws ExecutionException
	 * @throws InterruptedException
	 */
	public void actualiza(Entidad modelo) throws InterruptedException, ExecutionException {
		final Map<String, Object> map = this.revisaUniquesAlActualizar(modelo);
		if (map.get("id") != null) {
			map.remove("id");
		}
		final DocumentReference ref = this.collection.document(modelo.getId());
		Fire.BD.<Void>runTransaction(tx -> {
			final DocumentSnapshot doc = tx.get(ref).get();
			Util.valida(doc.exists(), "Registro no encontrado.");
			tx.update(ref, map);
			return null;
		});
	}

	/**
	 * Elimina un documento en la base de datos.
	 * 
	 * @param id id que se busca.
	 * @throws ExecutionException
	 * @throws InterruptedException
	 */
	public void elimina(String id) throws InterruptedException, ExecutionException {
		this.revisaRequeridoPorAlEliminar(id);
		this.collection.document(id).delete().get();
	}

	/**
	 * Revisa que las constraint unique se cumplan al momento de agregar.
	 * 
	 * @param modelo modelo que se agrega.
	 * @returns el objeto en formato Map.
	 * @throws ExecutionException
	 * @throws InterruptedException.
	 */
	Map<String, Object> revisaUniquesAlAgregar(Entidad modelo) throws InterruptedException, ExecutionException {
		final Map<String, Object> map = modelo.toMap();
		uniq: for (final Unique unique : this.uniques) {
			Query query = this.collection;
			for (final String campo : unique.campos) {
				final Object valor = map.get(campo);
				if (valor == null) {
					continue uniq;
				}
				query = query.whereEqualTo(campo, valor);
			}
			final QuerySnapshot resultado = query.get().get();
			Util.valida(resultado.size() == 0, unique.mensaje);
		}
		return map;
	}

	/**
	 * Revisa que las constraint unique se cumplan al momento de actualizar.
	 * 
	 * @param id     id del modelo.
	 * @param modelo datos que se actualizan.
	 * @returns el objeto en formato Map.
	 * @throws ExecutionException
	 * @throws InterruptedException
	 */
	Map<String, Object> revisaUniquesAlActualizar(Entidad modelo) throws InterruptedException, ExecutionException {
		final Map<String, Object> map = modelo.toMap();
		uniq: for (final Unique unique : this.uniques) {
			Query query = this.collection;
			for (final String campo : unique.campos) {
				Object valor = map.get(campo);
				if (valor == null) {
					continue uniq;
				}
				query = query.whereEqualTo(campo, valor);
			}
			final QuerySnapshot resultado = query.get().get();
			resultado.forEach(
					doc -> Util.valida(Objects.toString(modelo.getId(), "").equals(doc.getId()), unique.mensaje));
		}
		return map;
	}

	/**
	 * Revisa que ninguna de las foreign keys indicada apunte a este documento.
	 * 
	 * @param id id del documento que se revisa.
	 * @throws ExecutionException
	 * @throws InterruptedException
	 */
	void revisaRequeridoPorAlEliminar(String id) throws InterruptedException, ExecutionException {
		for (Foreign foránea : this.requeridoPor) {
			final QuerySnapshot resultado = Fire.BD.collection(foránea.collection).whereEqualTo(foránea.foránea, id)
					.get().get();
			Util.valida(resultado.size() == 0, foránea.mensaje);
		}
	}

}