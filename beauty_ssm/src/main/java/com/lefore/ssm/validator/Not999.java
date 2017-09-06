package com.lefore.ssm.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * author: lefore
 * date: 2017/8/23
 * email: 862080515@qq.com
 */
@Constraint(validatedBy = Not999Validator.class)    //具体的实现
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Not999 {

    // 提示信息，可以写死，可以填写国际化的 key
    String message() default "{com.lefore.ssm.validator.not999}";

    // 下面这两个属性必须添加
    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
