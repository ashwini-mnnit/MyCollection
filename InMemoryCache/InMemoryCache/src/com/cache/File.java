package com.cache;

import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.time.Instant;
import java.util.Date;

public class File {
	private final int id;
	private Boolean isPinned;
	private String filename;
	private ByteBuffer filebytes;
	private Boolean isDirty;
	private Date createTime;
	private Date lastModifiedTime;

	public File(String filename) {
		this.isPinned = false;
		this.filename = filename;
		this.filebytes = null;
		this.createTime= new Date(); 
		this.lastModifiedTime= new Date();
	}

	public void readContent() throws FileTooBigException, IOException {
		byte[] bytes;
		try {
			java.io.File file = new java.io.File(this.filename);

			if (file.length() > FileCacheImpl.MAX_FILE_SIZE) {
				throw new FileTooBigException(this.filename);
			}
			bytes = new byte[(int) file.length()];
			DataInputStream dis = new DataInputStream(new FileInputStream(file));
			dis.readFully(bytes);
			try {
				if (dis != null)
					dis.close();
			} catch (IOException e) {
				throw e;
			}
		} catch (IOException e) {
			throw e;
		}
		
		this.filebytes=ByteBuffer.wrap(bytes);
	}

	public void flushContent() {
		BufferedOutputStream bs = null;

		try {

			FileOutputStream fs = new FileOutputStream(new java.io.File(
					this.filename));
			bs = new BufferedOutputStream(fs);
			bs.write(filebytes.array());

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			if (bs != null)
				try {
					bs.close();
				} catch (Exception e) {
				}
		}

	}

	public Boolean getIsPinned() {
		return isPinned;
	}

	public void setIsPinned(Boolean isPinned) {
		this.isPinned = isPinned;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public ByteBuffer getBytes() {
		return filebytes;
	}

	public void setBytes(ByteBuffer bytes) {
		this.filebytes = bytes;
	}

	public Boolean getIsDirty() {
		return isDirty;
	}

	public void setIsDirty(Boolean isDirty) {
		this.isDirty = isDirty;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getLastModifiedTime() {
		return lastModifiedTime;
	}

	public void setLastModifiedTime(Date lastModifiedTime) {
		this.lastModifiedTime = lastModifiedTime;
	}

}
