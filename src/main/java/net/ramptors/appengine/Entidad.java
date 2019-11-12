package net.ramptors.appengine;

import java.util.HashMap;
import java.util.Map;

public abstract class Entidad {
	protected String id;

	public abstract String getFiltro();

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Map<String, Object> toMap() {
		final Map<String, Object> map = new HashMap<>();
		if (!Util.isNullOrEmpty(id)) {
			map.put("id", id);
		}
		return map;
	}
}
