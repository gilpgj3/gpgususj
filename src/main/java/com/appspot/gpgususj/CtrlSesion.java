package com.appspot.gpgususj;

import static com.appspot.gpgususj.Fire.BD;
import static net.ramptors.appengine.Util.preparaParaBúsqueda;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.cloud.firestore.QueryDocumentSnapshot;

import net.ramptors.appengine.Util;

@WebServlet(name = "CtrlSesion", urlPatterns = { "/CtrlSesion" })
public class CtrlSesion extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		try {
			final Principal principal = request.getUserPrincipal();
			if (principal == null) {
				throw new Exception("Falta iniciar sesión");
			} else {
				final UserService userService = UserServiceFactory.getUserService();
				request.setAttribute("logoutURL", userService.createLogoutURL("/index.jsp"));
				final List<QueryDocumentSnapshot> documents = BD.collection("Usuario")
						.whereEqualTo("upCue", preparaParaBúsqueda(principal.getName())).limit(1).get().get()
						.getDocuments();
				if (documents.size() == 0) {
					request.setAttribute("imagen", "");
				} else {
					for (QueryDocumentSnapshot doc : documents) {
						final InfoUsuario modelo = doc.toObject(InfoUsuario.class);
						request.setAttribute("imagen", modelo.getImagen());
					}
				}
			}
			request.getRequestDispatcher("FormSesion.jsp").forward(request, response);
		} catch (Exception e) {
			Util.procesa(this, request, response, e);
		}
	}
}