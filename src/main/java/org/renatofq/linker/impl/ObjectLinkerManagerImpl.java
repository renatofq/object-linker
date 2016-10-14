package org.renatofq.linker.impl;

import org.renatofq.linker.ObjectLinker;
import org.renatofq.linker.ObjectLinkerManager;

public class ObjectLinkerManagerImpl implements ObjectLinkerManager {
	
	private ObjectDataSource dataSource = new ObjectDataSource();

	@Override
	public synchronized void sync(ObjectLinker linker) {

		if (!(linker instanceof ObjectLinkerImpl)) {
			throw new IllegalArgumentException("Invalid implementation of ObjectLinker");
		}
		ObjectLinkerImpl linkerImpl = (ObjectLinkerImpl) linker;
		
		linkerImpl.sync(dataSource);
	}

	@Override
	public synchronized ObjectLinker load() {

		ObjectLinkerImpl linkerImpl = new ObjectLinkerImpl();
		
		linkerImpl.load(dataSource);
		
		return linkerImpl;
	}

}
