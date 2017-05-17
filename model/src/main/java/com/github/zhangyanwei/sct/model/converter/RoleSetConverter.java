package com.github.zhangyanwei.sct.model.converter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.github.zhangyanwei.sct.model.security.Authority.Role;

import java.util.EnumSet;

public class RoleSetConverter extends JsonTypeReferenceConverter<EnumSet<Role>> {

    private TypeReference<EnumSet<Role>> typeReference;

    {
        typeReference = new TypeReference<EnumSet<Role>>() {};
    }

    @Override
    protected TypeReference<EnumSet<Role>> typeReference() {
        return typeReference;
    }
}