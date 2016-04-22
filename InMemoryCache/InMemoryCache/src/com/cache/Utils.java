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
	
	public final static Logger GetLogger(String fileName) throws SecurityException, IOException
	{
		Logger logger;
		Handler fileHandler = new FileHandler("InMemoryFileCache.log");
		logger=Logger.getLogger(fileName);
		logger.addHandler(fileHandler);
		return logger;
	}
}
