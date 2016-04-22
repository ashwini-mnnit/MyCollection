package com.cache;

import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public class File {

	private Logger log;

	private final UUID id;

	private Boolean isPinned;
	private Boolean isDirty;

	// File
	private String filename;
	private ByteBuffer filebytes;

	// Timestamp
	private LocalDateTime createTime;
	private LocalDateTime lastModifiedTime;
	private LocalDateTime lastAccessedTime;

	// TODO: implementation of the storage.

	public File(String filename) throws SecurityException, IOException {
		log = Utils.GetLogger(File.class.getName());

		id = UUID.randomUUID();
		this.isPinned = false;
		this.filename = filename;
		this.filebytes = null;
		LocalDateTime now = LocalDateTime.now();
		this.createTime = now;
		this.lastModifiedTime = now;
	}

	public void readContent() throws FileTooBigException, IOException {
		byte[] bytes;
		DataInputStream dis = null;
		try {
			java.io.File file = new java.io.File(this.filename);

			// Check the max allowed size
			if (file.length() > FileCacheImpl.MAX_FILE_SIZE) {
				throw new FileTooBigException(this.filename);
			}

			// Read file
			bytes = new byte[(int) file.length()];
			dis = new DataInputStream(new FileInputStream(file));
			dis.readFully(bytes);

		} catch (IOException e) {
			log.log(Level.SEVERE, "Fail to read the file :: " + filename);
			throw e;
		} finally {
			if (dis != null)
				Utils.CloseStreamIgnoreException(dis);
		}

		this.filebytes = ByteBuffer.wrap(bytes);
		log.log(Level.INFO, "Read complete :: " + filename);
	}

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
		this.lastModifiedTime = LocalDateTime.now();
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

}
