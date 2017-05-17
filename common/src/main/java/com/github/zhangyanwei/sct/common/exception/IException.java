package com.github.zhangyanwei.sct.common.exception;

public interface IException {
    String getCodeValue();
    String getMessage();
    String getLocalizedMessage();
    <C extends Enum<C>> C getCode();
}
