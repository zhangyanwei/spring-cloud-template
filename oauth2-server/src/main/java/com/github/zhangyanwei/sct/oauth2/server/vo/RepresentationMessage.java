package com.github.zhangyanwei.sct.oauth2.server.vo;

import com.github.zhangyanwei.sct.common.exception.AbstractException;
import com.github.zhangyanwei.sct.common.exception.AbstractRuntimeException;
import com.github.zhangyanwei.sct.common.exception.IException;
import com.github.zhangyanwei.sct.common.exception.Message;
import com.github.zhangyanwei.sct.oauth2.server.configuration.handler.AccessException;
import org.springframework.security.core.AuthenticationException;

import java.io.Serializable;

public class RepresentationMessage implements Serializable {

    private static final long serialVersionUID = -5825177710860167621L;

    private Message message;

    public RepresentationMessage(IException exception) {
        this.message = new Message(exception);
    }

    public String getCode() {
        return message.getCode();
    }

    public String getMessage() {
        return message.getMessage();
    }

    public String getDetail() {
        return message.getDetail();
    }

    public static RepresentationMessage from(AuthenticationException exception) {
        Throwable cause = exception.getCause();
        if (cause instanceof AbstractException) {
            return new RepresentationMessage((AbstractException) cause);
        }

        if (cause instanceof AbstractRuntimeException) {
            return new RepresentationMessage((AbstractRuntimeException) cause);
        }

        return new RepresentationMessage(new AccessException(exception));
    }
}
