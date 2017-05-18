package com.github.zhangyanwei.sct.oauth2.server.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class HttpEntityProcessor {

    @Autowired
    private ObjectMapper objectMapper;

    private HttpMessageConverter<Object> messageConverter;

    @PostConstruct
    private void init() {
        messageConverter = new MappingJackson2HttpMessageConverter(objectMapper);
    }

    public void writeEntity(ResponseEntity<?> entity, HttpServletResponse response) throws IOException {
        if (response == null) {
            return;
        }

        ServletServerHttpResponse httpResponse = new ServletServerHttpResponse(response);
        HttpHeaders entityHeaders = entity.getHeaders();
        if (!entityHeaders.isEmpty()) {
            httpResponse.getHeaders().putAll(entityHeaders);
        }

        httpResponse.setStatusCode(entity.getStatusCode());
        this.messageConverter.write(entity.getBody(), null, httpResponse);
    }

}
