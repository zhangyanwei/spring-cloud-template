package com.github.zhangyanwei.sct.model.converter;

import com.fasterxml.jackson.core.type.TypeReference;

import javax.persistence.AttributeConverter;

import static com.github.zhangyanwei.sct.common.utils.Json.readValue;
import static com.github.zhangyanwei.sct.common.utils.Json.writeValueAsString;

abstract class JsonTypeReferenceConverter<T> implements AttributeConverter<T, String> {

    @Override
    public String convertToDatabaseColumn(T value) {
        return writeValueAsString(value, typeReference());
    }

    @Override
    public T convertToEntityAttribute(String content) {
        return content == null ? null : readValue(content, typeReference());
    }

    abstract protected TypeReference<T> typeReference();
}
