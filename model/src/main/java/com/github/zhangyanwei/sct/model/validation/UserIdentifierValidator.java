package com.github.zhangyanwei.sct.model.validation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static com.google.common.base.Strings.isNullOrEmpty;
import static org.springframework.beans.BeanUtils.findDeclaredMethod;

public class UserIdentifierValidator implements ConstraintValidator<UserIdentifier, Object> {

    private final Logger logger = LoggerFactory.getLogger(UserIdentifierValidator.class);

    private String emailMethodName;
    private String phoneNumberMethodName;

    @Override
    public void initialize(UserIdentifier constraintAnnotation) {
        this.emailMethodName = constraintAnnotation.emailMethod();
        this.phoneNumberMethodName = constraintAnnotation.phoneNumberMethod();
    }

    @Override
    public boolean isValid(Object userEntity, ConstraintValidatorContext context) {
        String email = invoke(userEntity, findDeclaredMethod(userEntity.getClass(), emailMethodName));
        String phoneNumber = invoke(userEntity, findDeclaredMethod(userEntity.getClass(), phoneNumberMethodName));
        return !isNullOrEmpty(email) || !isNullOrEmpty(phoneNumber);
    }

    private <T> T invoke(Object userEntity, Method emailMethod) {
        Object result = null;
        try {
            result = emailMethod.invoke(userEntity);
        } catch (IllegalAccessException | InvocationTargetException e) {
            logger.error(e.getLocalizedMessage());
        }

        //noinspection unchecked
        return (T) result;
    }
}