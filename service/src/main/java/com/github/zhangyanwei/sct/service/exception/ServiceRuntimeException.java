package com.github.zhangyanwei.sct.service.exception;

import com.github.zhangyanwei.sct.common.exception.AbstractRuntimeException;
import com.github.zhangyanwei.sct.common.exception.Message;

import static java.lang.String.format;

public abstract class ServiceRuntimeException extends AbstractRuntimeException {

    private static final long serialVersionUID = -6045759006673606027L;

    public ServiceRuntimeException(Message message) {
        super(message);
    }

    public ServiceRuntimeException(Enum<?> code) {
        super(code);
    }

    public ServiceRuntimeException(Enum<?> code, String message) {
        super(code, message);
    }

    public ServiceRuntimeException(Enum<?> code, Throwable cause) {
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
