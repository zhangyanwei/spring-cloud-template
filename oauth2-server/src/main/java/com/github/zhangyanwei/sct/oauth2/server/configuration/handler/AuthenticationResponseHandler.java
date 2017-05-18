package com.github.zhangyanwei.sct.oauth2.server.configuration.handler;

import org.springframework.security.core.AuthenticationException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface AuthenticationResponseHandler {
    void handle(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException;
}
