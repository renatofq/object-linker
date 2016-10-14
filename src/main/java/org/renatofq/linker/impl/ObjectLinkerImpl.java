package org.renatofq.linker.impl;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.renatofq.linker.LinkNotFoundException;
import org.renatofq.linker.ObjectLinker;

public class ObjectLinkerImpl implements ObjectLinker {
	
	static class ObjectData implements Serializable {

		private static final long serialVersionUID = 1L;

		private String className;
		private String content;

		
		public ObjectData(String className, String content) {
			this.className = className;
			this.content = content;
		}
		
		public String getClassName() {
			return className;
		}

		public String getContent() {
			return content;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((content == null) ? 0 : content.hashCode());
			return Math.abs(result); // ensure positive hashCode
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}

			if (obj == null) {
				return false;
			}

			if (getClass() != obj.getClass()) {
				return false;
			}

			ObjectData other = (ObjectData) obj;
			if (content == null) {
				if (other.content != null) {
					return false;
				}
			} else if (!content.equals(other.content)) {
				return false;
			}

			return true;
		}

		public <O> void fillObject(O object) {
			ObjectStringer.fillFromString(content, object);
		}
		
		public static <O> ObjectData fromObject(O object) {
			String className = object.getClass().getSimpleName();
			String content = ObjectStringer.toString(object);
			return new ObjectData(className, content);
		}
	}
	
	private Map<Integer, ObjectData> objectMap = new ConcurrentHashMap<>();

	@Override
	public <A, B> void link(A a, B b) {
		ObjectData dataA = ObjectData.fromObject(a);
		ObjectData dataB = ObjectData.fromObject(b);
		
		objectMap.put(dataA.hashCode(), dataB);
	}

	@Override
	public <A, B> void fill(A a, B b) throws LinkNotFoundException {
		ObjectData dataA = ObjectData.fromObject(a);

		ObjectData dataB = objectMap.get(dataA.hashCode());
		if (dataB != null) {
			dataB.fillObject(b);
		} else {
			throw new LinkNotFoundException("Object A isn't linked");
		}
	}

	void sync(ObjectDataSource datasource) {
		Map<Integer, ObjectData> newMap = new HashMap<>(objectMap);

		try {
			datasource.saveObjects(newMap);
		} catch (IOException e) {
			throw new RuntimeException("Error while syncing objects", e);
		}
	}
	
	void load(ObjectDataSource datasource) {
		try {
			datasource.loadObjects(objectMap);
		} catch (IOException e) {
			throw new RuntimeException("Error while loading objects", e);
		}
	}
	
}
