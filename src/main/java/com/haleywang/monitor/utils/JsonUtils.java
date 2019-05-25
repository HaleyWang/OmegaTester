package com.haleywang.monitor.utils;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import com.fasterxml.jackson.databind.MapperFeature;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.json.JsonSanitizer;
import com.haleywang.monitor.common.Constants;
import com.haleywang.monitor.common.ReqException;
import org.json.JSONObject;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JsonUtils {


	public static final String JSON_STRING = "json string:";
	public static final String CAN_T_CONVERT_BEAN = " can't convert bean";

	public static JSONObject extend(JSONObject jsonOne, JSONObject jsonTwo) {

		for(Object key : jsonTwo.keySet()) {
			String key1 = (String) key;
			jsonOne.put(key1, jsonTwo.get(key1));
		}

		return jsonOne;
	}


	/**
	 * use default date pattern
	 * 
	 * @param json
	 * @param clazz
	 * @return
	 */
	public static <T> T fromJson(String json, Class<T> clazz) {
		return fromJson(json, clazz, Constants.DEFAULT_DATE_PATTERN);
	}

	private static ObjectMapper createDefaultObjectMapper() {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
        mapper.enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS);
        mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS, true);

        return mapper;
	}

	/**
	 * parse json to bean with date pattern
	 * 
	 * @param json
	 * @param clazz
	 * @return
	 */
	public static <T> T fromJson(String json, Class<T> clazz, String pattern) {
		if (StringUtils.isEmpty(json)) {
			return null;
		}
		json = JsonSanitizer.sanitize(json);
		ObjectMapper mapper = createDefaultObjectMapper();
		DateFormat dateFormat = new SimpleDateFormat(pattern);
		mapper.setDateFormat(dateFormat);

		try {
			return mapper.readValue(json, clazz);
		} catch (IOException e) {
			throw new ReqException(JSON_STRING + json + " can't convert to " + clazz.getName(), e);
		}

	}

	/**
	 * 
	 * @author Bruce.Wang create May 28, 2015
	 * 
	 * @param json
	 * @param t
	 * @return
	 */
	public static <T> T fromJson(String json, TypeReference<T> t) {
        if(!StringUtils.startsWith(StringUtils.trim(json), "{")) {
            return null;
        }
		ObjectMapper mapper = createDefaultObjectMapper();
		try {
			return mapper.readValue(json, t);
		} catch (IOException e) {
			throw new ReqException(JSON_STRING + json + CAN_T_CONVERT_BEAN, e);
		}

	}

	public static <T> T fromJson(JsonNode node, Class<T> clazz) {
		ObjectMapper mapper = createDefaultObjectMapper();
		try {
			return mapper.convertValue(node, clazz);
		} catch (IllegalArgumentException e) {
			throw new ReqException(JSON_STRING + node + CAN_T_CONVERT_BEAN, e);
		}

	}

	public static <T> T fromJson(JsonNode node, TypeReference<T> t) {
		ObjectMapper mapper = createDefaultObjectMapper();
		try {
			return mapper.convertValue(node, t);
		} catch (IllegalArgumentException e) {
			throw new ReqException(JSON_STRING + node + CAN_T_CONVERT_BEAN, e);
		}

	}

	/**
	 * convert stream to object
	 *
     * @param jsonStream
     * @param t
     * @param <T>
     * @return
     */
	public static <T> T fromJson(InputStream jsonStream, TypeReference<T> t) {
		ObjectMapper mapper = createDefaultObjectMapper();
		try {
			return mapper.readValue(jsonStream, t);
		} catch (IOException e) {
			throw new ReqException("json stream can't be converted to bean", e);
		}

	}

	/**
	 * parse bean to string
	 * 
	 * @param bean
	 * @return
	 */
	public static String toJson(Object bean) {
		return toJson(bean, Constants.DEFAULT_DATE_PATTERN);
	}

	/**
	 * parse bean to string with date pattern, default is yyyy-MM-dd
	 * 
	 * @param bean
	 * @param datePattern
	 * @return
	 */
	public static String toJson(Object bean, String datePattern) {
		ObjectMapper mapper = createDefaultObjectMapper();
		DateFormat dateFormat = new SimpleDateFormat(datePattern);
		mapper.setDateFormat(dateFormat);
		try {
			return mapper.writeValueAsString(bean);
		} catch (JsonProcessingException e) {
			throw new ReqException("bean to json error: " + bean, e);
		}

	}

	public static Object val(JSONObject reqJsonObject, String key) {
		return reqJsonObject.has(key) ? reqJsonObject.get(key) : null;
	}

    public static String val(JSONObject reqJsonObject, String key, String defaultVal) {
        Object obj = reqJsonObject.has(key) ? reqJsonObject.get(key) : null;
        if(obj == null) {
            return defaultVal;
        }
        return StringUtils.defaultIfBlank(obj.toString(), defaultVal);
    }

	public static class DefaultTypeReference<T> extends TypeReference<T> {

	}

	public static ObjectNode createObjectNode() {
		return new ObjectMapper().createObjectNode();
	}

	public static ArrayNode createArrayNode() {
		return new ObjectMapper().createArrayNode();
	}

	public static String toStandardJson(String jsonStr) {
		return new JSONObject(jsonStr).toString();
	}


}
