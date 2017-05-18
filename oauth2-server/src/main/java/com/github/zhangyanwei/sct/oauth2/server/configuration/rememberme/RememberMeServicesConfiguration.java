package com.github.zhangyanwei.sct.oauth2.server.configuration.rememberme;

import com.github.zhangyanwei.sct.oauth2.server.configuration.WebMvcConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;

@Configuration
public class RememberMeServicesConfiguration {

    private static final String PARAM_REMEMBER_ME = "remember-me";

    @Value("${yijubao.remember-me.token.key:53e8dfb2c50b4bcf}")
    private String rememberMeTokenKey;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private WebMvcConfiguration.WebSettings webSettings;

    @Bean
    @Qualifier("formLogin")
    public PersistentTokenBasedRememberMeServices formLoginRememberMeServices() {
        PersistentTokenBasedRememberMeServices rememberMeServices = new TokenRememberMeServices(
                rememberMeTokenKey,
                userDetailsService,
                persistentTokenRepository(),
                webSettings.getRememberMeCookieDomain()
        );

        rememberMeServices.setParameter(PARAM_REMEMBER_ME);
        rememberMeServices.setAlwaysRemember(true);
        return rememberMeServices;
    }

    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl db = new JdbcTokenRepositoryImpl();
        db.setDataSource(dataSource);
        return db;
    }

}
