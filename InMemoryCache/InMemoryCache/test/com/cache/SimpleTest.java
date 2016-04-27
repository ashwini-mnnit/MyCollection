package com.cache;

import java.io.IOException;

import org.junit.Test;

public class SimpleTest {

	public static final String TestFileDir = "TestFiles";
	@Test
	public void test() {
		FileCache cache = new FileCacheImpl(10);		
		try {
			cache.pinFiles(Utils.GetFilesFromDir(TestFileDir));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InMemoryCacheException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
