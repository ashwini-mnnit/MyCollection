package com.cache;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

//TODO: Add logger
public class FileCacheImpl extends FileCache {
	public static final int MAX_FILE_SIZE = 10240; // in KB
	public static final int CACHE_SIZE = 100; // Number of files.
	private Map<String, File> fileMap = new HashMap<String, File>();

	protected FileCacheImpl(int maxCacheEntries) {
		super(maxCacheEntries);
	}

	@Override
	void pinFiles(Collection<String> fileNames) {
		for (Iterator<String> it = fileNames.iterator(); it.hasNext();) {
			String filename = (String) it.next();
			if (fileMap.containsKey(filename)) {
				fileMap.get(filename).setIsPinned(true);
			} else {
				// TODO: Implement LRU here
				File file = new File(filename);
				try {
					file.readContent();
				} catch (FileTooBigException e) {

					e.printStackTrace();
				} catch (IOException e) {

					e.printStackTrace();
				}
				file.setIsPinned(true);
				fileMap.put(filename, file);
			}

		}

	}

	@Override
	void unpinFiles(Collection<String> fileNames) {
		for (Iterator<String> it = fileNames.iterator(); it.hasNext();) {
			String filename = (String) it.next();
			fileMap.get(filename).setIsPinned(false);
		}
	}

	@Override
	ByteBuffer fileData(String fileName) throws Exception {
		try {
			fileMap.get(fileName).readContent();
		} catch (Exception e) {
			throw e;
		}
		return fileMap.get(fileName).getBytes();
	}

	@Override
	ByteBuffer mutableFileData(String fileName) {
		fileMap.get(fileName).setIsDirty(true);
		return fileMap.get(fileName).getBytes();
	}

	@Override
	void shutdown() {
		for (File file : fileMap.values()) {
			file.flushContent();
		}
	}

	public Map<String, File> getFileMap() {
		return fileMap;
	}

	public void setFileMap(Map<String, File> fileMap) {
		this.fileMap = fileMap;
	}

}
