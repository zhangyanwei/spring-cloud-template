package com.github.zhangyanwei.sct.service.dto.user;

import org.hibernate.validator.constraints.NotBlank;

import java.util.Date;

import static com.github.zhangyanwei.sct.model.entity.User.UserStatus.ACTIVE;

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

    public com.github.zhangyanwei.sct.model.entity.User toUser() {
        com.github.zhangyanwei.sct.model.entity.User user = new com.github.zhangyanwei.sct.model.entity.User();
        user.setNickname(this.getNickname());
        user.setName(this.getName());
        user.setPhoneNumber(this.getMobile());
        user.setAuthorities(this.getAuthorities());
        user.setPassword(this.getPassword());
        user.setStatus(ACTIVE);
        user.setRegistrationTime(new Date());
        return user;
    }

}
