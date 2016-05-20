package com.cache;

import com.cache.exception.InMemoryCacheException;

public class FileTooBigException extends InMemoryCacheException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6167325327856607406L;
	private String filename;

	public FileTooBigException(String file) {
		this.filename = file;
	}

	@Override
	public String getMessage() {
		return filename + ": File size is exceeds the MAX_FILE_SIZE limit ";
	}

}
