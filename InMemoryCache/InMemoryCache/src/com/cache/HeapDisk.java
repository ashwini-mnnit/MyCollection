package com.cache;

import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Logger;

/**
 * This is in virtual In-memory storage disk to store file content. Singleton as
 * the system supports only one copy of disk.
 * 
 * @author Ashwini Singh
 *
 */
public class HeapDisk {
	private Logger log = Utils.GetLogger(File.class.getName());

	private HashMap<UUID, BlockStore> idToByteMap;
	private HashMap<String, BlockStore> hashToByteMap;
	private static HeapDisk diskInstance = null;

	private HeapDisk() {
		idToByteMap = new HashMap<UUID, BlockStore>();
		hashToByteMap = new HashMap<String, BlockStore>();
	}

	// synchronized. Only one thread can create the first once
	public static synchronized HeapDisk getInstance() {
		if (diskInstance == null) {
			diskInstance = new HeapDisk();
		}
		return diskInstance;
	}

	public void AddBlockToDisk(BlockStore block) {
		if (IsBlockAlreadyPresent(block)) {
			block.incrementRefCount();
			return;
		}
		idToByteMap.put(block.getBlockID(), block);
		hashToByteMap.put(block.getByteHashCode(), block);
	}

	public void RemoveBlockToDisk(UUID id) {
		BlockStore block = idToByteMap.get(id);
		RemoveBlockToDisk(block);
	}

	public void RemoveBlockToDisk(BlockStore block) {
		block.decrementRefCount();
		if (block.getRefCount() == 0) {
			idToByteMap.remove(block.getBlockID());
			hashToByteMap.remove(block.getByteHashCode());
		}
	}

	private boolean IsBlockAlreadyPresent(BlockStore block) {
		return IsBlockAlreadyPresent(block.getByteHashCode());
	}

	private boolean IsBlockAlreadyPresent(String hash) {
		return hashToByteMap.containsKey(hash);
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		log.severe("Not allowed to create multiple copy of virtual in memory storage disk");
		throw new CloneNotSupportedException("Can not create multiple copy of virtual in memory storage disk");
	}
}
