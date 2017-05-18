package com.github.zhangyanwei.sct.oauth2.server.configuration.rememberme;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.util.ReflectionUtils;

import javax.annotation.Nullable;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

import static com.google.common.base.Strings.isNullOrEmpty;

public class TokenRememberMeServices extends PersistentTokenBasedRememberMeServices {

    private static final int TOKEN_VALIDITY_ONE_YEAR_SECONDS = 31536000;

    private final Method setHttpOnlyMethod;
    private String cookieDomain;

    public TokenRememberMeServices(String key, UserDetailsService userDetailsService, PersistentTokenRepository tokenRepository, @Nullable String cookieDomain) {
        super(key, userDetailsService, tokenRepository);
        this.setHttpOnlyMethod = ReflectionUtils.findMethod(Cookie.class, "setHttpOnly", boolean.class);
        this.cookieDomain = cookieDomain;
    }

    @Override
    protected void setCookie(String[] tokens, int maxAge, HttpServletRequest request, HttpServletResponse response) {
        String cookieValue = this.encodeCookie(tokens);
        Cookie cookie = new Cookie(this.getCookieName(), cookieValue);
        cookie.setMaxAge(TOKEN_VALIDITY_ONE_YEAR_SECONDS);
        cookie.setPath(this.getCookiePath(request));

        if (!isNullOrEmpty(this.cookieDomain)) {
            cookie.setDomain(this.cookieDomain);
        }
        if (maxAge < 1) {
            cookie.setVersion(1);
        }

        if (this.setHttpOnlyMethod != null) {
            ReflectionUtils.invokeMethod(this.setHttpOnlyMethod, cookie, Boolean.TRUE);
        } else if (this.logger.isDebugEnabled()) {
            this.logger.debug("Note: Cookie will not be marked as HttpOnly because you are not using Servlet 3.0 (Cookie#setHttpOnly(boolean) was not found).");
        }

        response.addCookie(cookie);
    }

    private String getCookiePath(HttpServletRequest request) {
        String contextPath = request.getContextPath();
        return contextPath.length() > 0 ? contextPath : "/";
    }
}
