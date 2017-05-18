package com.github.zhangyanwei.sct.oauth2.server.configuration.handler;

import com.github.zhangyanwei.sct.common.exception.AbstractRuntimeException;
import com.github.zhangyanwei.sct.oauth2.server.configuration.HttpEntityProcessor;
import com.github.zhangyanwei.sct.oauth2.server.configuration.userdetails.UserDetails;
import com.github.zhangyanwei.sct.oauth2.server.vo.Me;
import com.github.zhangyanwei.sct.oauth2.server.vo.RepresentationMessage;
import com.github.zhangyanwei.sct.service.UserService;
import com.github.zhangyanwei.sct.service.exception.ServiceException;
import com.github.zhangyanwei.sct.service.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.OK;

public class WebAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler implements AjaxAuthenticationHandler<WebAuthenticationSuccessHandler> {

    @Autowired
    private HttpEntityProcessor httpEntityProcessor;

    @Autowired
    private UserService userService;

    private String ajaxParam = DEFAULT_AJAX_PARAM;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
        try {
            // update the last access time.
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            // TODO update it in async way
            Long userId = userDetails.getId();
            if (userId != null) {
                userService.updateLastAccessTime(userId, new Date());
            }

            // write user info into response

            // redirect previous url
            if (!Boolean.valueOf(request.getParameter(ajaxParam))) {
                super.onAuthenticationSuccess(request, response, authentication);
            }

            ResponseEntity<Me> entity = ResponseEntity.status(OK)
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .body(Me.from(userDetails));
            httpEntityProcessor.writeEntity(entity, response);

        } catch (AbstractRuntimeException | ServiceException e) {
            ResponseEntity<RepresentationMessage> entity = ResponseEntity.status(INTERNAL_SERVER_ERROR)
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .body(new RepresentationMessage(e));
            httpEntityProcessor.writeEntity(entity, response);
        }
    }

    public WebAuthenticationSuccessHandler ajaxParam(String name) {
        this.ajaxParam = name;
        return this;
    }

    public WebAuthenticationSuccessHandler targetUrlParam(String name) {
        super.setTargetUrlParameter(name);
        return this;
    }

}
