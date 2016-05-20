package com.cache.util;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collection;
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
	
	public final static Collection<String> GetFilesFromDir(String dir)
	{
		ArrayList<String> rv = new ArrayList<String>();
		java.io.File[] files = new java.io.File(dir).listFiles();
		for (java.io.File file : files) {
		    if (file.isFile()) {
		        rv.add(file.getName());
		    }
		}
		return rv;
	}
	
	public final static String CreateHash(byte[] input)  {
		String hexStr = "";
		MessageDigest md;
		try {
			md = MessageDigest.getInstance("SHA1");
			md.reset();
			md.update(input);
			byte[] digest = md.digest();
			
			for (int i = 0; i < digest.length; i++) {
				hexStr += Integer.toString((digest[i] & 0xff) + 0x100, 16).substring(1);
			}
		} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
		}
		return hexStr;
	}
	
}
