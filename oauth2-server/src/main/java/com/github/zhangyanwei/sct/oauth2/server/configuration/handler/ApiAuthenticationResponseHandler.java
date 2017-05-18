package com.github.zhangyanwei.sct.oauth2.server.configuration.handler;

import com.github.zhangyanwei.sct.oauth2.server.configuration.HttpEntityProcessor;
import com.github.zhangyanwei.sct.oauth2.server.vo.RepresentationMessage;
import com.github.zhangyanwei.sct.service.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.springframework.http.HttpStatus.FORBIDDEN;

public class ApiAuthenticationResponseHandler implements AuthenticationResponseHandler {

    @Autowired
    private HttpEntityProcessor httpEntityProcessor;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        ResponseEntity<RepresentationMessage> entity = ResponseEntity.status(FORBIDDEN)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(RepresentationMessage.from(authException));

        httpEntityProcessor.writeEntity(entity, response);
    }
}
