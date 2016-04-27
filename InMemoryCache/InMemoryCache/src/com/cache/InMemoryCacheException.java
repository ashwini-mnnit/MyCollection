package com.cache;

public class InMemoryCacheException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String message;

	public InMemoryCacheException() {
		this.message = "";
	}

	public InMemoryCacheException(String message) {
		super();
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
}
