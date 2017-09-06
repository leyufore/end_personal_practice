package com.lefore.ssm.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * author: lefore
 * date: 2017/8/23
 * email: 862080515@qq.com
 */
public class Not999Validator implements ConstraintValidator<Not999, Long> {

    @Override
    public void initialize(Not999 not999) {

    }

    @Override
    public boolean isValid(Long value, ConstraintValidatorContext constraintValidatorContext) {
        if (value == 999) {
            return false;
        } else {
            return true;
        }
    }
}
