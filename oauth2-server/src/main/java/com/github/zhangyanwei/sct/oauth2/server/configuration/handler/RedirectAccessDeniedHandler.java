package com.github.zhangyanwei.sct.oauth2.server.configuration.handler;

import com.github.zhangyanwei.sct.oauth2.server.configuration.HttpEntityProcessor;
import com.github.zhangyanwei.sct.oauth2.server.vo.RepresentationMessage;
import com.github.zhangyanwei.sct.service.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.springframework.http.HttpStatus.FORBIDDEN;

public class RedirectAccessDeniedHandler implements AccessDeniedHandler {

    @Autowired
    private HttpEntityProcessor httpEntityProcessor;

    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException, ServletException {
        if (!response.isCommitted()) {
            // Put exception into request scope (perhaps of use to a view)
            request.setAttribute(WebAttributes.ACCESS_DENIED_403, accessDeniedException);

            ResponseEntity<RepresentationMessage> entity = ResponseEntity.status(FORBIDDEN)
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .body(new RepresentationMessage(new AccessException(accessDeniedException)));

            httpEntityProcessor.writeEntity(entity, response);
        }
    }
}
