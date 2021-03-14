package com.zws.utils.tools;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zws.utils.exception.AppException;

public class JsonUtils {
    private static ObjectMapper objectMapper = new ObjectMapper();

    static {
        //只将非空的字段序列化.
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    /**
     * 对象转字符串
     *
     * @param value
     * @return
     */
    public static String serialize(Object value) {
        if (value instanceof String) {
            return (String) value;
        }
        try {
            return objectMapper.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            throw new AppException(e);
        }
    }

    /**
     * 字符串转对象
     *
     * @param value
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T deserialize(String value, Class<T> clazz) {
        try {
            return objectMapper.readValue(value, clazz);
        } catch (JsonProcessingException e) {
            throw new AppException(e);
        }
    }

    public static <T> T deserialize(String value, TypeReference<T> reference) {
        try {
            return objectMapper.readValue(value, reference);
        } catch (JsonProcessingException e) {
            throw new AppException(e);
        }
    }
}