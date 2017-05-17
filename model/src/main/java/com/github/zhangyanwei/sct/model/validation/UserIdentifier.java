package com.github.zhangyanwei.sct.model.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Constraint(validatedBy = {UserIdentifierValidator.class})
@Target({TYPE, FIELD, PARAMETER})
@Retention(value = RUNTIME)
@Documented
public @interface UserIdentifier {

    String message() default "{UserIdentifier.message}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    String emailMethod() default "getEmail";
    String phoneNumberMethod() default "getPhoneNumber";

}