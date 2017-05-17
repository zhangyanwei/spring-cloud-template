package com.github.zhangyanwei.sct.common.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;
import static com.fasterxml.jackson.databind.PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES;

public final class Json {

    private static final Logger LOGGER = LoggerFactory.getLogger(Json.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.setSerializationInclusion(NON_NULL);
        objectMapper.configure(FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.setPropertyNamingStrategy(CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES);
    }

    @Nullable
    public static <T> T readValue(@Nonnull InputStream content, Class<T> type) {
        try {
            return objectMapper.readValue(content, type);
        } catch (IOException e) {
            LOGGER.warn(e.getLocalizedMessage());
            return null;
        }
    }

    @Nullable
    public static <T> T readValue(@Nonnull Reader content, Class<T> type) {
        try {
            return objectMapper.readValue(content, type);
        } catch (IOException e) {
            LOGGER.warn(e.getLocalizedMessage());
            return null;
        }
    }

    @Nullable
    public static <T> T readValue(@Nonnull String content, Class<T> type) {
        try {
            return objectMapper.readValue(content, type);
        } catch (IOException e) {
            LOGGER.warn(e.getLocalizedMessage());
            return null;
        }
    }

    @Nullable
    public static <T> T readValue(@Nonnull String content, TypeReference<T> typeReference) {
        try {
            return objectMapper.readValue(content, typeReference);
        } catch (IOException e) {
            LOGGER.warn(e.getLocalizedMessage());
            return null;
        }
    }

    @Nullable
    public static String readNode(@Nonnull String content, @Nonnull String fieldName) {
        try {
            JsonNode jsonNode = objectMapper.readTree(content).get(fieldName);
            return jsonNode == null ? null : jsonNode.toString();
        } catch (IOException e) {
            LOGGER.warn(e.getLocalizedMessage());
            return null;
        }
    }

    @Nullable
    public static String writeValueAsString(@Nullable Object value) {
        if (value != null) {
            try {
                return objectMapper.writeValueAsString(value);
            } catch (JsonProcessingException e) {
                LOGGER.warn(e.getLocalizedMessage());
            }
        }
        return null;
    }

    @Nullable
    public static <T> String writeValueAsString(@Nullable Object value, TypeReference<T> typeReference) {
        if (value != null) {
            try {
                ObjectWriter writer = objectMapper.writerWithType(typeReference);
                return writer.writeValueAsString(value);
            } catch (JsonProcessingException e) {
                LOGGER.warn(e.getLocalizedMessage());
            }
        }
        return null;
    }

    public static <T> T convertValue(Object from, Class<T> type) {
        return objectMapper.convertValue(from, type);
    }

    public static ObjectMapper objectMapper() {
        return objectMapper.copy();
    }
}
