package com.cache;

public class FileTooBigException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6167325327856607406L;
	private String filename;

	public FileTooBigException(String file) {
		this.filename = file;
	}

	public String message() {
		return filename + ": File size is exceeds the MAX_FILE_SIZE limit ";
	}

}
