package com.appspot.gpgususj;

import static com.appspot.gpgususj.Fire.BD;
import static java.util.Objects.requireNonNull;
import static net.ramptors.appengine.Util.consulta;
import static net.ramptors.appengine.Util.getDocRef;
import static net.ramptors.appengine.Util.getRef;
import static net.ramptors.appengine.Util.isNullOrEmpty;
import static net.ramptors.appengine.Util.opciones;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.ramptors.appengine.Util;

@WebServlet(name = "CtrlUsuarioBusca", urlPatterns = { "/CtrlUsuarioBusca" })
public class CtrlUsuarioBusca extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		try {
			final String id = request.getParameter("id");
			final InfoUsuario modelo;
			if (isNullOrEmpty(id)) {
				modelo = new InfoUsuario();
			} else {
				modelo = getRef(InfoUsuario.class, getDocRef(BD, "Usuario", id));
				requireNonNull(modelo, "Usuario no Encontrado.");
			}
			final List<InfoPasatiempo> pasatiempos = consulta(InfoPasatiempo.class,
					BD.collection("Pasatiempo").orderBy("upNombre"));
			final List<InfoRol> roles = consulta(InfoRol.class, BD.collection("Rol").orderBy("upId"));
			request.setAttribute("pasatiempos",
					opciones("-- Sin Pasatiempo --", pasatiempos, modelo.getPasatiempo(), p -> p.getNombre()));
			request.setAttribute("roles",
					opciones(roles, modelo.getRoles(), r -> r.getId() + ": " + r.getDescripcion()));
			request.setAttribute("modelo", modelo);
			request.getRequestDispatcher("FormUsuario.jsp").forward(request, response);
		} catch (Exception e) {
			Util.procesa(this, request, response, e);
		}
	}
}