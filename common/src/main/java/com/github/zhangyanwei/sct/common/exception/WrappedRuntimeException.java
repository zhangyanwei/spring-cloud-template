package com.github.zhangyanwei.sct.common.exception;

import com.google.common.base.Strings;

import static com.github.zhangyanwei.sct.common.exception.WrappedRuntimeException.Error.RUNTIME;

public class WrappedRuntimeException extends AbstractRuntimeException {

    private static final long serialVersionUID = -4535023489178353587L;

    public enum Error {
        RUNTIME
    }

    private IException exception;
    private Message message;

    public WrappedRuntimeException(Message message) {
        super(message);
        this.message = message;
        String partial = message.getPartial();
        setCode(Strings.isNullOrEmpty(partial) ? RUNTIME : Error.valueOf(partial));
    }

    public WrappedRuntimeException(RuntimeException exception) {
        super(RUNTIME, exception);
    }

    public WrappedRuntimeException(IException exception) {
        this.exception = exception;
    }

    public WrappedRuntimeException(AbstractException exception) {
        this.exception = exception;
    }

    @Override
    public String getMessage() {
        if (exception != null) {
            return this.exception.getMessage();
        }

        if (this.message != null) {
            String detail = this.message.getDetail();
            if (!Strings.isNullOrEmpty(detail)) {
                return detail;
            }
        }

        return super.getMessage();
    }

    @Override
    public String getLocalizedMessage() {
        return this.exception != null ? this.exception.getLocalizedMessage() : super.getLocalizedMessage();
    }

    @Override
    public <C extends Enum<C>> C getCode() {
        return this.exception != null ? this.exception.getCode() : super.getCode();
    }

    @Override
    public String getCodeValue() {
        return this.exception != null ? this.exception.getCodeValue() : super.getCodeValue();
    }

    @Override
    public synchronized Throwable getCause() {
        return this.exception != null ? (Throwable) exception : super.getCause();
    }

    @Override
    protected String messageResourceBaseName() {
        return "exception.service";
    }

    @Override
    protected String moduleName() {
        return "RT";
    }
}
