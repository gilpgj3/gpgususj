package com.appspot.gpgususj;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import com.google.cloud.firestore.DocumentReference;

import net.ramptors.appengine.Entidad;
import net.ramptors.appengine.Util;

public class InfoUsuario extends Entidad {
	/** Cuenta del usuario. */
	private String cue = "";
	/** URL de la imagen del usuario. */
	private String imagen;
	/** Referencia al pasatiempo. */
	private DocumentReference pasatiempo;
	/** Referencias a los roles. */
	private List<DocumentReference> roles = new ArrayList<>();
	private String upCue;

	public InfoUsuario() {
	}

	public InfoUsuario(String id, String cue, String imagen, DocumentReference pasatiempo, List<DocumentReference> roles,
			String upCue) {
		this.id = id;
		this.cue = cue;
		this.imagen = imagen;
		this.pasatiempo = pasatiempo;
		this.roles = roles;
		this.upCue = upCue;
	}

	public String getCue() {
		return cue;
	}

	public void setCue(String cue) {
		this.cue = cue;
	}

	public String getImagen() {
		return imagen;
	}

	public void setImagen(String imagen) {
		this.imagen = imagen;
	}

	public DocumentReference getPasatiempo() {
		return pasatiempo;
	}

	public void setPasatiempo(DocumentReference pasatiempo) {
		this.pasatiempo = pasatiempo;
	}

	public List<DocumentReference> getRoles() {
		return roles;
	}

	public void setRoles(List<DocumentReference> roles) {
		this.roles = roles;
	}

	public String getUpCue() {
		return upCue;
	}

	public void setUpCue(String upCue) {
		this.upCue = upCue;
	}

	@Override
	public String getFiltro() {
		final InfoPasatiempo pasa = Util.getRef(InfoPasatiempo.class, this.pasatiempo);
		final List<InfoRol> rols = Util.getArr(InfoRol.class, this.roles);
		return Objects.toString(cue, "") + " " + (pasa == null ? "" : Objects.toString(pasa.getNombre(), "")) + " "
				+ rols.stream().map(rol -> rol.getId()).collect(Collectors.joining(", "));
	}

	@Override
	public Map<String, Object> toMap() {
		final Map<String, Object> map = super.toMap();
		if (cue != null) {
			map.put("cue", cue);
		}
		if (imagen != null) {
			map.put("imagen", imagen);
		}
		if (pasatiempo != null) {
			map.put("pasatiempo", pasatiempo);
		}
		map.put("roles", roles == null ? new DocumentReference[0] : roles);
		if (upCue != null) {
			map.put("upCue", upCue);
		}
		return map;
	}

}