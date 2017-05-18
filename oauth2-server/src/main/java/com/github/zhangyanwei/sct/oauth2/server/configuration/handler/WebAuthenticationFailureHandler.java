package com.github.zhangyanwei.sct.oauth2.server.configuration.handler;

import com.github.zhangyanwei.sct.oauth2.server.configuration.HttpEntityProcessor;
import com.github.zhangyanwei.sct.oauth2.server.vo.RepresentationMessage;
import com.github.zhangyanwei.sct.service.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.google.common.base.Preconditions.checkArgument;
import static javax.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;

public class WebAuthenticationFailureHandler implements AuthenticationFailureHandler, AjaxAuthenticationHandler<WebAuthenticationFailureHandler> {

    @Autowired
    private HttpEntityProcessor httpEntityProcessor;

    private String ajaxParam = DEFAULT_AJAX_PARAM;

    private String errorPage;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {

        // redirect previous url
        if (!Boolean.valueOf(request.getParameter(ajaxParam))) {
            // super.onAuthenticationFailed(request, response, authentication);
            if (errorPage != null) {
                // Put exception into session scope (perhaps of use to a view)
                request.getSession().setAttribute(WebAttributes.AUTHENTICATION_EXCEPTION, RepresentationMessage.from(exception));
                // Set the 401 status code.
                response.setStatus(SC_UNAUTHORIZED);
                // redirect to error page.
                response.sendRedirect(errorPage);
            } else {
                response.sendError(SC_UNAUTHORIZED, exception.getMessage());
            }
            return;
        }

        // write user info into response
        ResponseEntity<RepresentationMessage> entity = ResponseEntity.status(SC_UNAUTHORIZED)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(RepresentationMessage.from(exception));
        httpEntityProcessor.writeEntity(entity, response);
    }

    @Override
    public WebAuthenticationFailureHandler ajaxParam(String name) {
        this.ajaxParam = name;
        return this;
    }

    public WebAuthenticationFailureHandler errorPage(String errorPage) {
        checkArgument(errorPage == null || errorPage.startsWith("/"), "errorPage must begin with '/'");
        this.errorPage = errorPage;
        return this;
    }

}
