package com.github.zhangyanwei.sct.service.exception;

import com.github.zhangyanwei.sct.common.exception.Message;

public class NotFoundException extends ServiceException {

    private static final long serialVersionUID = -2909833311527921881L;

    public enum Error {
        USER
    }

    public NotFoundException(Message message) {
        super(message);
        setCode(Error.valueOf(message.getPartial()));
    }

    public NotFoundException(Error error) {
        super(error);
    }

    @Override
    protected String reasonCode() {
        return "NOT_FOUND";
    }
}
