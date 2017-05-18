package com.github.zhangyanwei.sct.oauth2.server.configuration.handler;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AuthenticationResponseEntryPoint extends LoginUrlAuthenticationEntryPoint {

    private AuthenticationResponseRegister authenticationResponseRegister;

    /**
     * @param loginFormUrl URL where the login page can be found. Should either be
     *                     relative to the web-app context path (include a leading {@code /}) or an absolute
     *                     URL.
     */
    public AuthenticationResponseEntryPoint(String loginFormUrl) {
        super(loginFormUrl);
    }

    public AuthenticationResponseRegister apiMatcher(RequestMatcher matcher) {
        authenticationResponseRegister = new AuthenticationResponseRegister(matcher);
        return authenticationResponseRegister;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        if (authenticationResponseRegister != null && authenticationResponseRegister.getMatcher().matches(request)) {
            AuthenticationResponseHandler authenticationResponseHandler = authenticationResponseRegister.getAuthenticationResponseHandler();
            authenticationResponseHandler.handle(request, response, authException);
        } else {
            super.commence(request, response, authException);
        }
    }

    public class AuthenticationResponseRegister {

        private RequestMatcher matcher;
        private AuthenticationResponseHandler authenticationResponseHandler;

        private AuthenticationResponseRegister(RequestMatcher matcher) {
            this.matcher = matcher;
        }

        public AuthenticationResponseEntryPoint authenticationResponseHandler(AuthenticationResponseHandler responseHandler) {
            this.authenticationResponseHandler = responseHandler;
            return AuthenticationResponseEntryPoint.this;
        }

        private RequestMatcher getMatcher() {
            return matcher;
        }

        private AuthenticationResponseHandler getAuthenticationResponseHandler() {
            return authenticationResponseHandler;
        }
    }
}
