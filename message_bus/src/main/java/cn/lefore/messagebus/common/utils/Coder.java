package cn.lefore.messagebus.common.utils;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * author: lefore
 * date: 2017/8/7
 * email: 862080515@qq.com
 */
public class Coder {

    public static String getBASE64(String s) {
        if (s == null) {
            return null;
        }
        return (new BASE64Encoder()).encode(s.getBytes());
    }

    public static String getFromBASE64(String s) {
        if (s == null) {
            return null;
        }
        BASE64Decoder decoder = new BASE64Decoder();
        try {
            byte[] b = decoder.decodeBuffer(s);
            return new String(b);
        } catch (Exception e) {
            return null;
        }
    }
}
