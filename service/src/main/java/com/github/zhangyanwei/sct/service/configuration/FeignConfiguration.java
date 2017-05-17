package com.github.zhangyanwei.sct.service.configuration;

import com.github.zhangyanwei.sct.common.exception.IException;
import com.github.zhangyanwei.sct.common.exception.Message;
import com.github.zhangyanwei.sct.common.exception.WrappedRuntimeException;
import com.github.zhangyanwei.sct.common.utils.Json;
import com.google.common.io.CharStreams;
import feign.Logger;
import feign.RetryableException;
import feign.Retryer;
import feign.codec.ErrorDecoder;
import org.springframework.cloud.netflix.feign.FeignFormatterRegistrar;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.datetime.DateFormatter;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static com.google.common.base.Strings.isNullOrEmpty;

@Configuration
public class FeignConfiguration {

    @Bean
    public Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }

    @Bean
    public ErrorDecoder errorDecoder() {

        return (methodKey, response) -> {
            try {
                String content = CharStreams.toString(response.body().asReader());
                Message message = Json.readValue(content, Message.class);
                if (message == null || isNullOrEmpty(message.getType())) {
                    return new RuntimeException("unknown error: " + content);
                }

                Class<?> causeType = Class.forName(message.getType());
                if (IException.class.isAssignableFrom(causeType)) {
                    Constructor<?> constructor = causeType.getConstructor(Message.class);
                    Exception exception = (Exception) constructor.newInstance(message);
                    return message.isWrappedInRuntimeException() ? new WrappedRuntimeException((IException) exception) : exception;
                }

                return new WrappedRuntimeException(message);
            } catch (IOException | ClassNotFoundException | NoSuchMethodException | InstantiationException | InvocationTargetException | IllegalAccessException e) {
                throw new RuntimeException("can not create exception from response.", e);
            }
        };

    }

    @Bean
    public Retryer retryer() {
        return new Retryer() {

            @Override
            public void continueOrPropagate(RetryableException e) {
                throw e;
            }

            @Override
            public Retryer clone() {
                return this;
            }
        };
    }

    @Bean
    public FeignFormatterRegistrar feignFormatterRegistrar() {
        return registry -> registry.addFormatter(new DateFormatter("yyyy-MM-dd'T'HH:mm:ss.SSSZ"));
    }

}