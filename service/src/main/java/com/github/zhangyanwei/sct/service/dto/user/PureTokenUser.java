package com.github.zhangyanwei.sct.service.dto.user;

import org.hibernate.validator.constraints.NotBlank;

public class PureTokenUser extends PureUser {

    private static final long serialVersionUID = -8576163053260088893L;

    @NotBlank
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @NotBlank
    public String getPassword() {
        return super.getPassword();
    }

}
