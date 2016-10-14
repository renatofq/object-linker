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
