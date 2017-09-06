package com.lefore.ssm.comment.util.validate;

import java.util.regex.Pattern;

/**
 * 正则表达式验证
 * <p>
 * author: lefore
 * date: 2017/8/29
 * email: 862080515@qq.com
 */
public class RegularUtil {

    /**
     * @param regular 正则表达式
     * @param input   需要验证的语句
     * @return 匹配返回true
     */
    public static boolean validatePattern(String regular, String input) {
        Pattern p = Pattern.compile(regular);
        return p.matcher(input).matches();
    }
}
