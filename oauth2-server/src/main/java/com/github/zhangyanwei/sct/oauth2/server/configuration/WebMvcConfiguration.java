package com.github.zhangyanwei.sct.oauth2.server.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.boot.context.embedded.MimeMappings;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.filter.ShallowEtagHeaderFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;

import static com.google.common.base.Strings.isNullOrEmpty;
import static java.util.Collections.*;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Stream.of;
import static org.springframework.http.HttpMethod.*;

@Configuration
public class WebMvcConfiguration extends WebMvcConfigurerAdapter {

    @Autowired
    private WebSettings webSettings;

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/login").setViewName("login");
        registry.addViewController("/oauth/confirm_access").setViewName("authorize");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        String[] apacheUrls = webSettings.getCorsMappings();
        if (apacheUrls != null) {
            registry.addMapping("/**")
                    .allowedOrigins(apacheUrls)
                    .allowedMethods(of(GET, POST, PUT, DELETE, HEAD).map(Enum::name).toArray(String[]::new));
        }
    }

    @Bean
    public EmbeddedServletContainerCustomizer containerCustomizer() {
        return container -> {
            MimeMappings mappings = new MimeMappings(MimeMappings.DEFAULT);
            mappings.add("woff","application/font-woff");
            mappings.add("woff2","application/font-woff2");
            mappings.add("js","application/javascript; charset=utf-8");
            container.setMimeMappings(mappings);
        };
    }

    @Bean
    public FilterRegistrationBean corsPreflightFilterRegistrationBean() {
        FilterRegistrationBean filterBean = new FilterRegistrationBean();
        filterBean.setFilter(new OncePerRequestFilter() {
            @Override
            protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
                if (CorsUtils.isPreFlightRequest(request)) {
                    HttpServletRequest wrappedServletRequest = new HttpServletRequestWrapper(request) {

                        @Override
                        public Enumeration<String> getHeaders(String name) {
                            Enumeration<String> headers = super.getHeaders(name);
                            return enumeration(list(headers).stream().filter(item -> !isNullOrEmpty(item)).collect(toList()));
                        }

                        @Override
                        public String getHeader(String name) {
                            String header = super.getHeader(name);
                            return isNullOrEmpty(header) ? null : header;
                        }
                    };
                    filterChain.doFilter(wrappedServletRequest, response);
                } else {
                    filterChain.doFilter(request, response);
                }
            }
        });
        filterBean.setUrlPatterns(singletonList("/*"));
        filterBean.setOrder(1);
        return filterBean;
    }

    @Configuration
    @ConfigurationProperties("sct.web")
    public static class WebSettings {

        private String[] corsMappings;
        private String cookieDomainNamePattern;
        private String rememberMeCookieDomain;

        public String[] getCorsMappings() {
            return corsMappings;
        }

        public void setCorsMappings(String[] corsMappings) {
            this.corsMappings = corsMappings;
        }

        public String getCookieDomainNamePattern() {
            return cookieDomainNamePattern;
        }

        public void setCookieDomainNamePattern(String cookieDomainNamePattern) {
            this.cookieDomainNamePattern = cookieDomainNamePattern;
        }

        public String getRememberMeCookieDomain() {
            return rememberMeCookieDomain;
        }

        public void setRememberMeCookieDomain(String rememberMeCookieDomain) {
            this.rememberMeCookieDomain = rememberMeCookieDomain;
        }
    }

}