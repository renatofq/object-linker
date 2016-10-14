package org.renatofq.linker.impl;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ObjectStringer {
	
	public static <O> String toString(O object) {

		try {
			ObjectMapper om = new ObjectMapper();
			return om.writeValueAsString(object);
		} catch (JsonProcessingException e) {
			throw new IllegalArgumentException("Cannot convert object to json", e);
		}
	}
	
	public static <O> void fillFromString(String content, O object) {
		try {
			ObjectMapper om = new ObjectMapper();
			om.readerForUpdating(object).readValue(content);
		} catch (IOException e) {
			throw new RuntimeException("Failed to process json content", e);
		} 
	}
	
	public static <T> T fromString(String content) {
		ObjectMapper om = new ObjectMapper();
		try {
			return om.readValue(content, new TypeReference<T>(){});
		} catch (IOException e) {
			throw new RuntimeException("Failed to process json content", e);
		}
	}

}
