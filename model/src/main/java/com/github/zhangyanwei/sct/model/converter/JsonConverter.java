package com.github.zhangyanwei.sct.model.converter;

import javax.persistence.AttributeConverter;

import static com.github.zhangyanwei.sct.common.utils.Json.readValue;
import static com.github.zhangyanwei.sct.common.utils.Json.writeValueAsString;
import static com.google.common.base.Strings.isNullOrEmpty;

abstract class JsonConverter<T> implements AttributeConverter<T, String> {

    @Override
    public String convertToDatabaseColumn(T attribute) {
        return writeValueAsString(attribute);
    }

    @Override
    public T convertToEntityAttribute(String data) {
        return isNullOrEmpty(data) ? null : readValue(data, type());
    }

    abstract protected Class<T> type();
}
