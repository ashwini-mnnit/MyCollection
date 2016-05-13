package com.cache;

import java.util.UUID;

public class BlockStore {

	private final UUID blockId;
	private String byteHashCode;
	private byte[] block;
	private int refCount;

	public BlockStore(UUID fileID) {
		super();
		this.refCount = 0;
		this.blockId = fileID;
		this.block = new byte[1024]; // 1KB pages :TODO make it configurable.
	}

	public int getRefCount() {
		return refCount;
	}

	public void incrementRefCount() {
		this.refCount++;
	}

	public void decrementRefCount() {
		this.refCount--;
	}

	public BlockStore(byte[] block) {
		super();
		this.blockId = UUID.randomUUID();
		setStore(block);
	}

	public BlockStore() {
		super();
		this.blockId = UUID.randomUUID();
		this.block = new byte[1024]; // 1KB pages :TODO make it configurable.
	}

	public String getByteHashCode() {
		return byteHashCode;
	}

	public UUID getBlockID() {
		return blockId;
	}

	public synchronized void setStore(byte[] block) {
		this.byteHashCode = Utils.CreateHash(block);
		this.block = block;
	}

}
