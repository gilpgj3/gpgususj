package com.appspot.gpgususj;

import static net.ramptors.appengine.Util.SEGMENTO;
import static net.ramptors.appengine.Util.isNullOrEmpty;
import static net.ramptors.appengine.Util.valida;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

import net.ramptors.appengine.Util;

@WebServlet(name = "CtrlUsuarioElimina", urlPatterns = { "/CtrlUsuarioElimina" })
public class CtrlUsuarioElimina extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final DaoUsuario dao = new DaoUsuario();

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		try {
			final String segmento = getServletContext().getInitParameter(SEGMENTO);
			final String id = request.getParameter("id");
			valida(!isNullOrEmpty(id), "Falta el id.");
			dao.elimina(id);
			final Storage storage = StorageOptions.getDefaultInstance().getService();
			final BlobId blobId = BlobId.of(segmento, id);
			storage.delete(blobId);
			response.sendRedirect("CtrlUsuarios");
		} catch (Exception e) {
			Util.procesa(this, request, response, e);
		}
	}
}