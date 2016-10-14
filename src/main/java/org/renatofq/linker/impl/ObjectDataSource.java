package org.renatofq.linker.impl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Map;

import org.renatofq.linker.impl.ObjectLinkerImpl.ObjectData;

public class ObjectDataSource {
	
	// TODO create a dynamic setting
	private Path path = Paths.get("./obj/");

	public void saveObjects(Map<Integer, ObjectData> objectMap) throws IOException {
		// FIXME: if some exception occurs during this 
		// method the base will be corrupted
		for (Map.Entry<Integer, ObjectData> entry : objectMap.entrySet()) {
			Path file = path.resolve(generateObjectFileName(entry.getKey(), entry.getValue()));
			try (BufferedWriter writer = getBufferWriter(file)) {
				writer.write(entry.getValue().getContent());
			} 
		}
	}
	
	public void loadObjects(Map<Integer, ObjectData> objectMap) throws IOException {

		try (DirectoryStream<Path> stream = Files.newDirectoryStream(path)) {
	        for (Path pathEntry : stream) {
	            if (Files.isRegularFile(pathEntry)){
	            	String filename = pathEntry.getFileName().toString();
					IdClassNamePair idClassNamePair = validateObjectFileName(filename);
					if (idClassNamePair != null) {
						objectMap.put(idClassNamePair.getId(),
								loadObject(idClassNamePair.getClassName(), pathEntry));
					}
	            }
	        }
		}
	}

	private static ObjectData loadObject(String className, Path path) throws IOException {
		try (BufferedReader reader = getBufferedReader(path)) {
			StringBuilder strBuilder = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				strBuilder.append(line);
			}
			return new ObjectData(className, strBuilder.toString());
		}
	}
	
	private static IdClassNamePair validateObjectFileName(String filename) {
		String[] nameParts = filename.split("[\\-\\.]");
    	if (nameParts.length == 3 
    			&& nameParts[0].length() == 36 
    			&& nameParts[1].length() > 1 
    			&& "json".equalsIgnoreCase(nameParts[2])
    			) {
    
    		try {
    			return new IdClassNamePair(fromHexString(nameParts[0]), nameParts[1]);
    		} catch (IllegalArgumentException e) {
    			return null;
    		}
    	}
    	return null;
	}

	private static String generateObjectFileName(Integer key, ObjectData object) {
		StringBuilder strBuilder = new StringBuilder();
		strBuilder.append(toHexString(key))
			.append("-")
			.append(object.getClassName())
			.append(".json");
		return strBuilder.toString();
	}

	private static BufferedWriter getBufferWriter(Path file) throws IOException {
		return Files.newBufferedWriter(file, 
					StandardCharsets.UTF_8, 
					StandardOpenOption.CREATE, 
					StandardOpenOption.TRUNCATE_EXISTING);
	}
	
	private static BufferedReader getBufferedReader(Path file) throws IOException {
		return Files.newBufferedReader(file, 
					StandardCharsets.UTF_8);
	}
	
	private static String toHexString(int value) {
		return String.format("%08X", value);
	}
	
	private static int fromHexString(String value) {
		return Integer.parseUnsignedInt(value, 16);
	}
	
	private static class IdClassNamePair {
		private int id;
		private String className;
		
		IdClassNamePair (int id, String className) {
			this.id = id;
			this.className = className;
		}

		public int getId() {
			return id;
		}

		public String getClassName() {
			return className;
		}
	}

}
