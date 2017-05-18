package com.github.zhangyanwei.sct.oauth2.server.configuration.userdetails;

import com.github.zhangyanwei.sct.service.dto.user.User;

import java.util.Map;

public interface XUserDetails extends org.springframework.security.core.userdetails.UserDetails {
    Long getId();
    User getUser();
    String getNickname();
    String getAvatar();
    String getMobile();
    Map<String, ?> getDetails();
}
