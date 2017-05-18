package com.github.zhangyanwei.sct.oauth2.server.configuration.filter;

import com.github.zhangyanwei.sct.oauth2.server.Header;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.github.zhangyanwei.sct.oauth2.server.Cookie.COOKIE_XSRF_TOKEN;
import static com.google.common.base.Strings.isNullOrEmpty;

public class CsrfCookieFilter extends OncePerRequestFilter {

    @Autowired
    private CsrfTokenRepository csrfTokenRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // load or create the csrf token
        CsrfToken csrfToken = csrfTokenRepository.loadToken(request);
        if (csrfToken == null) {
            csrfToken = csrfTokenRepository.generateToken(request);
            csrfTokenRepository.saveToken(csrfToken, request, response);
        }

        // set csrf token into header
        HttpServletRequest wrappedRequest = request;
        Cookie cookie = WebUtils.getCookie(request, COOKIE_XSRF_TOKEN);
        if (cookie != null) {
            ExtendedHttpServletRequest servletRequest = new ExtendedHttpServletRequest(request);
            servletRequest.addHeader(Header.HEADER_X_XSRF_TOKEN, cookie.getValue());
            wrappedRequest = servletRequest;
        }

        // update cookie
        cookie = new Cookie(COOKIE_XSRF_TOKEN, csrfToken.getToken());
        cookie.setPath("/");
        response.addCookie(cookie);

        filterChain.doFilter(wrappedRequest, response);
    }

    public static class ExtendedHttpServletRequest extends HttpServletRequestWrapper {

        private Map<String, String> headers = new HashMap<>();

        public ExtendedHttpServletRequest(HttpServletRequest request) {
            super(request);
        }

        @Override
        public String getHeader(String name) {
            String header = super.getHeader(name);
            if (isNullOrEmpty(header)) {
                header = headers.get(name);
            }

            return header;
        }

        public void addHeader(String name, String value) {
            headers.put(name, value);
        }
    }
}
