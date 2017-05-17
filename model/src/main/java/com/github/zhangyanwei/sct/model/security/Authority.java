package com.github.zhangyanwei.sct.model.security;

public interface Authority {

    String authority();

    enum Role implements Authority {

        USER, ADMINISTRATOR;

        @Override
        public String authority() {
            return "ROLE_" + this.name();
        }
    }

}

