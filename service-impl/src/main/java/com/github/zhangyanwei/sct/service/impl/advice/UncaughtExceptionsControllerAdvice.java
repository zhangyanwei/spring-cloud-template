package com.github.zhangyanwei.sct.service.impl.advice;

import com.github.zhangyanwei.sct.common.exception.AbstractException;
import com.github.zhangyanwei.sct.common.exception.AbstractRuntimeException;
import com.github.zhangyanwei.sct.common.exception.Message;
import com.github.zhangyanwei.sct.common.exception.WrappedRuntimeException;
import com.github.zhangyanwei.sct.service.exception.RequestValidateException;
import com.github.zhangyanwei.sct.service.http.MediaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.UnsatisfiedServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@ControllerAdvice
public class UncaughtExceptionsControllerAdvice {

    private static final Logger logger = LoggerFactory.getLogger(UncaughtExceptionsControllerAdvice.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    @ResponseStatus(BAD_REQUEST)
    public Message handleArgumentErrors(MethodArgumentNotValidException exception) {
        logger.debug(exception.getLocalizedMessage(), exception);
        return new Message(new RequestValidateException(exception));
    }

    @ExceptionHandler({
            HttpRequestMethodNotSupportedException.class,
            HttpMessageNotReadableException.class,
            UnsatisfiedServletRequestParameterException.class,
            IllegalArgumentException.class})
    @ResponseBody
    @ResponseStatus(BAD_REQUEST)
    public Message handleArgumentErrors(Exception exception) {
        logger.debug(exception.getLocalizedMessage(), exception);
        return new Message(new RequestValidateException(exception));
    }

//    @ExceptionHandler(IllegalStateException.class)
//    @ResponseBody
//    @ResponseStatus(BAD_REQUEST)
//    public Message handleArgumentErrors(IllegalStateException exception) {
//        logger.debug(exception.getLocalizedMessage(), exception);
//        return new Message(new BadRequestException(exception));
//    }

    @ExceptionHandler(WrappedRuntimeException.class)
    @ResponseBody
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    public Message runtimeException(WrappedRuntimeException exception) {
        logger.debug(exception.getLocalizedMessage(), exception);
        return new Message(exception);
    }

    @ExceptionHandler(AbstractRuntimeException.class)
    @ResponseBody
    public ResponseEntity<Message> serviceException(AbstractRuntimeException exception) {
        logger.debug(exception.getLocalizedMessage(), exception);
        return ResponseEntity.status(BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(new Message(exception));
    }

    @ExceptionHandler(AbstractException.class)
    @ResponseBody
    @ResponseStatus(BAD_REQUEST)
    public Message serviceException(AbstractException exception) {
        logger.debug(exception.getLocalizedMessage(), exception);
        return new Message(exception);
    }

}