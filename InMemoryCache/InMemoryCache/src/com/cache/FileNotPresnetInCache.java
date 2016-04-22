package com.cache;

public class FileNotPresnetInCache extends InMemoryCacheException {

	private static final long serialVersionUID = 1L;
	private String filename;

	public FileNotPresnetInCache(String file) {
		this.filename = file;
	}

	@Override
	public String getMessage() {
		return "File " + filename + " doesnot exist in the cache";
	}
}
