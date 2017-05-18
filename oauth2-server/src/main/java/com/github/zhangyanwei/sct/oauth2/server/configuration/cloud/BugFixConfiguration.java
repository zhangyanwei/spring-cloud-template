package com.github.zhangyanwei.sct.oauth2.server.configuration.cloud;

import org.springframework.cloud.context.scope.refresh.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BugFixConfiguration {

    @Bean
    public static RefreshScope refreshScope() {
        RefreshScope refresh = new RefreshScope();
        refresh.setId("web-api:1");
        return refresh;
    }

}
