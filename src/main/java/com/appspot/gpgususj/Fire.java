package com.appspot.gpgususj;

import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOptions;

public class Fire {
	private static final FirestoreOptions firestoreOptions = FirestoreOptions.getDefaultInstance().toBuilder()
			.setProjectId("gpgususj").build();
	public static final Firestore BD = firestoreOptions.getService();
}