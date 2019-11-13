package com.appspot.gpgususj;

import static com.appspot.gpgususj.Fire.BD;
import static net.ramptors.appengine.Util.SEGMENTO;
import static net.ramptors.appengine.Util.getDocRef;
import static net.ramptors.appengine.Util.getDocRefs;
import static net.ramptors.appengine.Util.isNullOrEmpty;
import static net.ramptors.appengine.Util.preparaParaBúsqueda;
import static net.ramptors.appengine.Util.subeImagen;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import com.google.cloud.firestore.DocumentReference;

import net.ramptors.appengine.Util;

@MultipartConfig
@WebServlet(name = "CtrlUsuarioGuarda", urlPatterns = { "/CtrlUsuarioGuarda" })
public class CtrlUsuarioGuarda extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final DaoUsuario dao = new DaoUsuario();

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		try {
			String id = request.getParameter("id");
			final String cue = request.getParameter("cue");
			final String pasatiempoId = request.getParameter("pasatiempo");
			final String[] rolIds = request.getParameterValues("roles");
			final Part imagenPart = request.getPart("imagen");
			if (isNullOrEmpty(cue)) {
				throw new Exception("Falta el cue");
			} else if (imagenPart == null && isNullOrEmpty(id)) {
				throw new Exception("Falta la imagen");
			} else {
				final DocumentReference pasatiempo = getDocRef(BD, "Pasatiempo", pasatiempoId);
				final List<DocumentReference> roles = getDocRefs(BD, "Rol", rolIds);
				final InfoUsuario modelo = new InfoUsuario(id, cue, null, pasatiempo, roles, preparaParaBúsqueda(cue));
				if (isNullOrEmpty(id)) {
					id = dao.agrega(modelo).getId();
				} else {
					dao.actualiza(modelo);
				}
				if (imagenPart != null) {
					final String segmento = getServletContext().getInitParameter(SEGMENTO);
					final Map<String, Object> map = new HashMap<String, Object>();
					map.put("imagen", subeImagen(imagenPart, segmento, id));
					BD.collection("Usuario").document(id).update(map).get();
				}
			}
			response.sendRedirect("CtrlUsuarios");
		} catch (

		Exception e) {
			Util.procesa(this, request, response, e);
		}
	}
}