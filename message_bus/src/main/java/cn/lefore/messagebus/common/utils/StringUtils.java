package cn.lefore.messagebus.common.utils;

import java.util.Random;

/**
 * author: lefore
 * date: 2017/8/9
 * email: 862080515@qq.com
 */
public class StringUtils {

    /**
     * @param length 生成字符串的长度
     * @return
     */
    public static String getRandomString(int length) {
        String base = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }
}
