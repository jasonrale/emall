package com.emall.annotation;

import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 手机格式注解校验器
 */
public class IsMobileValidator implements ConstraintValidator<IsMobile, String> {
    private static final Pattern MOBILE_PATTERN = Pattern.compile("^(1[3-9])\\d{9}$");

    private boolean required = false;

    @Override
    public void initialize(IsMobile constraintAnnotation) {
        required = constraintAnnotation.require();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (required) {
            return isMobile(value);
        }else {
            if (StringUtils.isEmpty(value)) {
                return true;
            }else {
                return isMobile(value);
            }
        }
    }

    private boolean isMobile(String src) {
        if (StringUtils.isEmpty(src)) {
            return false;
        }
        Matcher matcher = MOBILE_PATTERN.matcher(src);
        return matcher.matches();
    }
}
