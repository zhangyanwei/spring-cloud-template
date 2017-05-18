package com.github.zhangyanwei.sct.oauth2.server.configuration;

import com.github.zhangyanwei.sct.oauth2.server.Header;
import com.github.zhangyanwei.sct.oauth2.server.configuration.handler.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import static org.springframework.http.HttpMethod.GET;

@Configuration
@EnableOAuth2Client
@EnableAuthorizationServer
@Order(6)
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    @Qualifier("formLogin")
    private PersistentTokenBasedRememberMeServices formLoginRememberMeServices;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                    .antMatchers(
                            "/login",
                            "/login/**",
                            "/error",
                            "/error_*.html",
                            "/web_*.txt",
                            "/oauth/**",
                            "/api/**",
                            "/**/*.html",
                            "/sitemap*.xml",
                            "/**/*.js",
                            "/**/*.css"
                    )
                    .permitAll()
                    //.anyRequest().hasRole(USER.name())
                    .and()
                .exceptionHandling()
                    // .accessDeniedHandler(new RedirectAccessDeniedHandler("/error_403.html"))
                    .accessDeniedHandler(redirectAccessDeniedHandler())
                    .authenticationEntryPoint(
                            new AuthenticationResponseEntryPoint("/login")
                                    .apiMatcher(new AntPathRequestMatcher("/api/**"))
                                    .authenticationResponseHandler(authenticationResponseHandler())
                    )
                    .and()
                .formLogin()
                    .loginPage("/login")
                    .defaultSuccessUrl("/index.html")
                    .successHandler(authenticationSuccessHandler().targetUrlParam("request"))
                    .failureHandler(authenticationFailureHandler().errorPage("/login?authentication_error=true"))
                    .and()
                .logout()
                    // TODO should using constant instead, and also should support the ajax logout
                    .deleteCookies("SESSION")
                    .invalidateHttpSession(true)
                    .logoutUrl("/logout")
                    .logoutRequestMatcher(new AntPathRequestMatcher("/logout", GET.name()))
                    .logoutSuccessHandler(webLogoutSuccessHandler())
                    .permitAll()
                    .and()
                .csrf()
                    .disable()
                .headers()
                    .frameOptions().disable()
                    .and()
                .rememberMe()
                    .key(formLoginRememberMeServices.getKey())
                    .rememberMeServices(formLoginRememberMeServices);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .parentAuthenticationManager(authenticationManager)
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder);
    }

    @Bean
    public WebAuthenticationSuccessHandler authenticationSuccessHandler() {
        return new WebAuthenticationSuccessHandler();
    }

    @Bean(name = "formLogin")
    public WebAuthenticationFailureHandler authenticationFailureHandler() {
        return new WebAuthenticationFailureHandler();
    }

    @Bean
    public ApiAuthenticationResponseHandler authenticationResponseHandler() {
        return new ApiAuthenticationResponseHandler();
    }

    @Bean
    public LogoutSuccessHandler webLogoutSuccessHandler() {
        return new WebLogoutSuccessHandler();
    }

    @Bean
    public RedirectAccessDeniedHandler redirectAccessDeniedHandler() {
        return new RedirectAccessDeniedHandler();
    }

    @Bean
    public CsrfTokenRepository csrfTokenRepository() {
        HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
        repository.setHeaderName(Header.HEADER_X_XSRF_TOKEN);
        return repository;
    }

    @Bean
    @ConditionalOnMissingBean(PasswordEncoder.class)
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
