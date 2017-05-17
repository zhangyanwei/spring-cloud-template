package com.github.zhangyanwei.sct.common.exception;

import org.springframework.context.MessageSource;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

import java.util.Locale;

import static java.lang.String.format;

public abstract class AbstractRuntimeException extends RuntimeException implements IException {

    private static final long serialVersionUID = 8134582353485375449L;

    private Enum<?> code;
    private String codeValue;
    private Object[] arguments;

    protected AbstractRuntimeException() {
    }

    public AbstractRuntimeException(Message message) {
        super(message.getMessage());
    }

    public AbstractRuntimeException(Enum<?> code) {
        this.code = code;
        this.codeValue = errorCode(code);
    }

    public AbstractRuntimeException(Enum<?> code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
        this.codeValue = errorCode(code);
    }

    public AbstractRuntimeException(Enum<?> code, String message) {
        super(message);
        this.code = code;
        this.codeValue = errorCode(code);
    }

    public AbstractRuntimeException(Enum<?> code, Throwable cause) {
        super(cause);
        this.code = code;
        this.codeValue = errorCode(code);
    }

    public AbstractRuntimeException(String code) {
        this.codeValue = code;
    }

    public AbstractRuntimeException(String code, String message) {
        super(message);
        this.codeValue = code;
    }

    public AbstractRuntimeException(String code, String message, Object ... arguments) {
        super(message);
        this.codeValue = code;
        this.arguments = arguments;
    }

    protected void setCode(Enum<?> code) {
        this.code = code;
        this.codeValue = errorCode(code);
    }

    protected void setCodeValue(String codeValue) {
        this.codeValue = codeValue;
    }

    protected void setArguments(Object ... arguments) {
        this.arguments = arguments;
    }

    @SuppressWarnings("unchecked")
    public <C extends Enum<C>> C getCode() {
        return (C) code;
    }

    public String getCodeValue() {
        return codeValue;
    }

    @Override
    public String getMessage() {
        String message = super.getMessage();
        return message == null ? codeValue : message;
    }

    @Override
    public String getLocalizedMessage() {
        return messageSource().getMessage(codeValue, arguments, super.getLocalizedMessage(), Locale.getDefault());
    }

    abstract protected String messageResourceBaseName();

    abstract protected String moduleName();

    private MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:" + messageResourceBaseName());
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setUseCodeAsDefaultMessage(true);
        return messageSource;
    }

    private String errorCode(Enum<?> errorEnum) {
        return format("%s_%s", moduleName(), errorEnum.name().toUpperCase());
    }

}
