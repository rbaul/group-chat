package com.github.rbaul.groupchat.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

@Slf4j
@UtilityClass
public class ObjectMapperUtils {
	public ObjectMapper JSON = new Jackson2ObjectMapperBuilder()
//			.serializers(LOCAL_DATETIME_SERIALIZER)
			.serializationInclusion(JsonInclude.Include.NON_NULL)
			.build();
	
	public String prettyString(Object o) {
		try {
			return JSON.writerWithDefaultPrettyPrinter().writeValueAsString(o);
		} catch (JsonProcessingException e) {
			log.error("Failed write object to string", e);
			return null;
		}
	}
}
