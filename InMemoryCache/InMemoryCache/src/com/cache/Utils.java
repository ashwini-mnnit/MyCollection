package com.cache;

import java.io.FilterInputStream;

public class Utils {

	public final static void CloseStreamIgnoreException(Object stream) {
		if (stream instanceof FilterInputStream) {
			try {
				((FilterInputStream) stream).close();
			} catch (Exception e) {
				// Ignore
			}
		}
	}
}
