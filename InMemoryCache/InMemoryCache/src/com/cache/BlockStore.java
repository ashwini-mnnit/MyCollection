package com.cache;

import java.util.UUID;

public class BlockStore {

	private final UUID blockId;
	private String byteHashCode;
	private byte[] block;

	public BlockStore(UUID fileID) {
		super();
		this.blockId = fileID;
		this.block = new byte[1024]; // 1KB pages :TODO make it configurable.
	}

	public BlockStore(byte [] block) {
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
