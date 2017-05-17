package com.github.zhangyanwei.sct.model.data;

import org.springframework.data.annotation.Id;

import java.io.Serializable;

public abstract class Base implements Serializable {

    private static final long serialVersionUID = 7508112864040416085L;

    @Id
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}