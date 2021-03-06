package com.cache;

import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.cache.exception.FileTooBigException;
import com.cache.storage.BlockStore;
import com.cache.storage.HeapDisk;
import com.cache.util.Utils;

public class CacheFile {

	private Logger log=Utils.GetLogger(CacheFile.class.getName());

	private final UUID id;

	private Boolean isPinned;
	private Boolean isDirty;

	// File
	private String filename;
	private ByteBuffer filebytes;
	private List<UUID> diskBlockIds;
	
	// Timestamp
	private LocalDateTime createTime;
	private LocalDateTime lastModifiedTime;
	private LocalDateTime lastAccessedTime;

	// TODO: implementation of the storage.
	/**
	 * Constructor
	 * @param filename
	 */
	public CacheFile(String filename){
		id = UUID.randomUUID();
		this.isPinned = false;
		this.filename = filename;
		this.filebytes = null;
		LocalDateTime now = LocalDateTime.now();
		this.createTime = now;
		this.lastModifiedTime = now;
		this.diskBlockIds = new ArrayList<UUID>();
	}

	/**
	 *  Read the content of the File.
	 * @throws FileTooBigException
	 * @throws IOException
	 */
	public void readContent() throws FileTooBigException, IOException {
		DataInputStream dis = null;
		try {
			java.io.File file = new java.io.File(this.filename);

			// Check the max allowed size
			if (file.length() > MemoryCache.MAX_FILE_SIZE) {
				throw new FileTooBigException(this.filename);
			}
			dis = new DataInputStream(new FileInputStream(file));
			
			int blockRequired = GetBlockCount(file.length());
			HeapDisk disk =  HeapDisk.getInstance();
			
			//Add block to the file. Preallocated and filled in later  
			for(int i= 0; i<blockRequired;i++)
			{
				byte [] rbyte = new byte[1024]; 
				dis.read(rbyte);
				BlockStore block = new BlockStore(rbyte);
				disk.AddBlockToDisk(block);
				this.diskBlockIds.add(block.getBlockID());
			}

		} catch (IOException e) {
			log.log(Level.SEVERE, "Fail to read the file :: " + filename);
			throw e;
		} finally {
			if (dis != null)
				Utils.CloseStreamIgnoreException(dis);
		}
		log.log(Level.INFO, "Read complete :: " + filename);
	}

	private int GetBlockCount(long length) {
		return (int) (length/1024 + (length%1024==0?0:1)) ;
	}

	/**
	 * Flushes the content of the file to disk.
	 */
	public void flushContent() {
		BufferedOutputStream bs = null;
		try {

			FileOutputStream fs = new FileOutputStream(new java.io.File(this.filename));
			bs = new BufferedOutputStream(fs);
			bs.write(filebytes.array());
		} catch (Exception e) {
			log.severe("Unable to flush the content of the file: " + filename);
		} finally {
			if (bs != null)
				Utils.CloseStreamIgnoreException(bs);
		}
		log.info("Flushed the content of file:" + filename + " to the disk");
	}

	public Boolean getIsPinned() {
		return isPinned;
	}

	public void setIsPinned(Boolean isPinned) {
		this.isPinned = isPinned;
	}

	synchronized public String getFilename() {
		return filename;
	}

	synchronized public void setFilename(String filename) {
		this.filename = filename;
	}

	synchronized public ByteBuffer getBytes() {
		this.lastAccessedTime = LocalDateTime.now();
		return filebytes;
	}

	synchronized public void setBytes(ByteBuffer bytes) {
		LocalDateTime now= LocalDateTime.now();;
		this.lastModifiedTime = now;
		this.lastAccessedTime =now;
		this.filebytes = bytes;
	}

	synchronized public Boolean getIsDirty() {
		return isDirty;
	}

	synchronized public void setIsDirty(Boolean isDirty) {
		this.isDirty = isDirty;
	}

	synchronized public LocalDateTime getCreateTime() {
		return createTime;
	}

	synchronized public void setCreateTime(LocalDateTime createTime) {
		this.createTime = createTime;
	}

	synchronized public LocalDateTime getLastModifiedTime() {
		return lastModifiedTime;
	}

	synchronized public void setLastModifiedTime(LocalDateTime lastModifiedTime) {
		this.lastModifiedTime = lastModifiedTime;
	}

	synchronized public LocalDateTime getLastAccessedTime() {
		return lastAccessedTime;
	}

	synchronized public void setLastAccessedTime(LocalDateTime lastAccessedTime) {
		this.lastAccessedTime = lastAccessedTime;
	}

	public UUID getId() {
		return id;
	}

	public boolean canEvict() {
		return !isDirty && !isPinned;
	}

}
