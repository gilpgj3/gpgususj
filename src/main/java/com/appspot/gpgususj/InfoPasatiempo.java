package com.appspot.gpgususj;

import java.util.Map;
import java.util.Objects;

import net.ramptors.appengine.Entidad;

public class InfoPasatiempo extends Entidad {
	private String nombre;
	private String upNombre;

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getUpNombre() {
		return upNombre;
	}

	public void setUpNombre(String upNombre) {
		this.upNombre = upNombre;
	}

	@Override
	public String getFiltro() {
		return Objects.toString(nombre, "");
	}

	@Override
	public Map<String, Object> toMap() {
		final Map<String, Object> map = super.toMap();
		if (nombre != null) {
			map.put("nombre", nombre);
		}
		if (upNombre != null) {
			map.put("upNombre", upNombre);
		}
		return map;
	}

}