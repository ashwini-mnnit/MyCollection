package com.cache;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Logger;

import com.cache.exception.InMemoryCacheException;
import com.cache.util.Utils;

//TODO: Add logger
public class FileCacheImpl extends FileCache {
	private Logger log = Utils.GetLogger(CacheFile.class.getName());
	public static final int MAX_FILE_SIZE = 10240; // in KB
	private Map<String, CacheFile> fileMap = new HashMap<String, CacheFile>();

	protected FileCacheImpl(int maxCacheEntries) {
		super(maxCacheEntries);
	}

	@Override
	void pinFiles(Collection<String> fileNames) throws IOException, InMemoryCacheException {
		for (Iterator<String> it = fileNames.iterator(); it.hasNext();) {
			String filename = (String) it.next();
			if (!fileMap.containsKey(filename)) {
				log.info("[Cache Miss]File " + filename + " is not in cache. Adding the file to cache.");
				AddFileToCache(filename);
			}
			fileMap.get(filename).setIsPinned(true);
		}
	}

	private void RemoveFileFromCache(String filename) {
		// flush content and Remove
		fileMap.get(filename).flushContent();
		fileMap.remove(filename);
	}

	private void AddFileToCache(String filename) throws IOException, InMemoryCacheException {

		if (fileMap.size() >= this.maxCacheEntries) {
			String lastUsedFile = getLastUsedFile();
			if (!lastUsedFile.isEmpty()) {
				RemoveFileFromCache(lastUsedFile);
			} else {
				throw new InMemoryCacheException(
						"Cache is full and all Files are pinned. Unpin some files for cache eviction before new files can be added. ");
			}
		}

		log.info("Adding file " +filename +" to cache");
		CacheFile file = new CacheFile(filename);
		try {
			file.readContent();
		} catch (FileTooBigException e) {
			throw e;
		} catch (IOException e) {
			throw e;
		}
		fileMap.put(filename, file);
		log.info("Added file " +filename +" to cache");
	}

	private String getLastUsedFile() {
		// TODO:Improve this algo. Use heap
		CacheFile rvFile = null;
		boolean first = true;
		for (CacheFile file : fileMap.values()) {
			if (!file.canEvict()) {
				if (first) {
					first = false;
					rvFile = file;
				}

				if (rvFile.getLastAccessedTime().isBefore(file.getLastAccessedTime())) {
					rvFile = file;
				}
			}
		}

		if (rvFile != null)
			return rvFile.getFilename();

		return "";
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
		if(!fileMap.containsKey(fileName))
		{
			throw new InMemoryCacheException("File "+fileName+" not present in the cache");
		}
		return fileMap.get(fileName).getBytes();
	}

	@Override
	ByteBuffer mutableFileData(String fileName) throws InMemoryCacheException {
		if(!fileMap.containsKey(fileName))
		{
			throw new InMemoryCacheException("File "+fileName+" not present in the cache");
		}
		
		fileMap.get(fileName).setIsDirty(true);
		return fileMap.get(fileName).getBytes();
	}

	@Override
	void shutdown() {
		for (CacheFile file : fileMap.values()) {
			file.flushContent();
		}
	}

	public Map<String, CacheFile> getFileMap() {
		return fileMap;
	}

	public void setFileMap(Map<String, CacheFile> fileMap) {
		this.fileMap = fileMap;
	}

}
