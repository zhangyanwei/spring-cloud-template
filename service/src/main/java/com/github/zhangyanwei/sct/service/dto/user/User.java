package com.github.zhangyanwei.sct.service.dto.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.github.zhangyanwei.sct.model.entity.User.UserStatus;
import com.github.zhangyanwei.sct.model.security.Authority;

import java.io.Serializable;
import java.util.EnumSet;

import static com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING;
import static com.google.common.base.Strings.isNullOrEmpty;
import static java.util.EnumSet.copyOf;
import static java.util.EnumSet.noneOf;

public class User implements Serializable {

    private static final long serialVersionUID = 1902344541219013304L;

    @JsonFormat(shape = STRING)
    private Long id;
    private String nickname;
    private String name;
    private String mobile;
    private EnumSet<Authority.Role> authorities;
    private UserStatus status;
    private String password;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public EnumSet<Authority.Role> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(EnumSet<Authority.Role> authorities) {
        this.authorities = authorities;
    }

    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    void fill(com.github.zhangyanwei.sct.model.entity.User entity) {
        this.setId(entity.getId());
        this.setNickname(getNickname(entity));
        this.setName(entity.getName());
        this.setMobile(entity.getPhoneNumber());
        this.setPassword(entity.getPassword());
        this.setAuthorities(getAuthorities(entity));
        this.setStatus(entity.getStatus());
    }

    public static User from(com.github.zhangyanwei.sct.model.entity.User entity) {
        User user = new User();
        user.fill(entity);
        return user;
    }

    private static String getNickname(com.github.zhangyanwei.sct.model.entity.User user) {
        String nickname = user.getNickname();
        if (isNullOrEmpty(nickname)) {
            String name = user.getName();
            if (isNullOrEmpty(name)) {
                return user.getPhoneNumber();
            }

            return name;
        }

        return nickname;
    }

    private static EnumSet<Authority.Role> getAuthorities(com.github.zhangyanwei.sct.model.entity.User user) {
        EnumSet<Authority.Role> authorities = user.getAuthorities();
        EnumSet<Authority.Role> roles = (authorities == null ? noneOf(Authority.Role.class) : copyOf(authorities));
        roles.add(Authority.Role.USER);
        return roles;
    }

    public boolean hasRole(Authority.Role role) {
        return this.getAuthorities().stream().anyMatch(role::equals);
    }
}
