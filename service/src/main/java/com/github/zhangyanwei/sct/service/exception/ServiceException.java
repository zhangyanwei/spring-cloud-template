package com.github.zhangyanwei.sct.service.exception;

import com.github.zhangyanwei.sct.common.exception.AbstractException;
import com.github.zhangyanwei.sct.common.exception.Message;

import static java.lang.String.format;

public abstract class ServiceException extends AbstractException {

    private static final long serialVersionUID = -6045759006673606027L;

    public ServiceException(Message message) {
        super(message);
    }

    public ServiceException(Enum<?> code) {
        super(code);
    }

    public ServiceException(Enum<?> code, String message) {
        super(code, message);
    }

    public ServiceException(Enum<?> code, Throwable cause) {
        super(code, cause);
    }

    @Override
    protected final String messageResourceBaseName() {
        return "exception.service";
    }

    @Override
    protected final String moduleName() {
        return format("SRV_%s", reasonCode());
    }

    protected abstract String reasonCode();
}
