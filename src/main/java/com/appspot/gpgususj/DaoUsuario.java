package com.appspot.gpgususj;

import net.ramptors.appengine.Dao;
import net.ramptors.appengine.Foreign;
import net.ramptors.appengine.Unique;

public class DaoUsuario extends Dao {

	public DaoUsuario() {
		super(Fire.BD, "Usuario", new Unique[] { new Unique(new String[] { "upCue" }, "El cue ya está registrado.") },
				new Foreign[] {});
	}

}
