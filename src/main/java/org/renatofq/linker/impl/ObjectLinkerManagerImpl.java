/*
  Copyright 2016 Renato Fernandes de Queiroz

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
*/

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
