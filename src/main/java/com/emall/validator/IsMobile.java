package com.emall.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.NotEmpty;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = {IsMobileValidator.class})
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
public @interface IsMobile {

    boolean require() default true;

    String message() default "com.emall.validator.IsMobile.message";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
