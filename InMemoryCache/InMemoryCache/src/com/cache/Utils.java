package com.cache;

import java.io.FilterInputStream;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Logger;

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
	
	public final static Logger GetLogger(String fileName)
	{
		Logger logger;
		Handler fileHandler= null;
		try {
			fileHandler = new FileHandler("InMemoryFileCache.log");
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logger=Logger.getLogger(fileName);
		logger.addHandler(fileHandler);
		return logger;
	}
}
