package com.appspot.gpgususj;

import static com.appspot.gpgususj.Fire.BD;
import static java.util.stream.Collectors.joining;
import static net.ramptors.appengine.Util.getArr;
import static net.ramptors.appengine.Util.getRef;
import static net.ramptors.appengine.Util.preparaParaBúsqueda;

import java.util.List;
import java.util.Map;

import javax.servlet.annotation.WebServlet;

import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.QueryDocumentSnapshot;

import net.ramptors.appengine.CtrlFiltro;

@WebServlet(name = "CtrlUsuarios", urlPatterns = { "/CtrlUsuarios" })
public class CtrlUsuarios extends CtrlFiltro {
	private static final long serialVersionUID = 1L;

	public CtrlUsuarios() {
		super("FormUsuarios.jsp");
	}

	@Override
	protected Query getConsulta() {
		return BD.collection("Usuario").orderBy("upCue");
	}

	@Override
	protected Map<String, Object> getMap(QueryDocumentSnapshot doc) {
		final InfoUsuario modelo = doc.toObject(InfoUsuario.class);
		final InfoPasatiempo pasatiempo = getRef(InfoPasatiempo.class, modelo.getPasatiempo());
		final List<InfoRol> roles = getArr(InfoRol.class, modelo.getRoles());
		final Map<String, Object> map = modelo.toMap();
		map.put("id", doc.getId());
		map.put("pasatiempo", pasatiempo == null ? "" : pasatiempo.getNombre());
		map.put("roles", roles.stream().map(rol -> rol.getId()).collect(joining(", ")));
		map.put("filtro", preparaParaBúsqueda(modelo.getFiltro()));
		return map;
	}
}