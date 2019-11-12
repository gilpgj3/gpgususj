package com.appspot.gpgususj;

import java.util.Map;
import java.util.Objects;

import net.ramptors.appengine.Entidad;

public class InfoRol extends Entidad {
	private String descripcion;
	private String upId;

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getUpId() {
		return upId;
	}

	public void setUpId(String upId) {
		this.upId = upId;
	}

	@Override
	public String getFiltro() {
		return Objects.toString(id, "") + " " + Objects.toString(descripcion, "");
	}

	@Override
	public Map<String, Object> toMap() {
		final Map<String, Object> map = super.toMap();
		if (descripcion != null) {
			map.put("descripcion", descripcion);
		}
		if (upId != null) {
			map.put("upId", upId);
		}
		return map;
	}

}