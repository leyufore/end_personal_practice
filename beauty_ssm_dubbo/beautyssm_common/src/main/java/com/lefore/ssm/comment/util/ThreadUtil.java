package com.lefore.ssm.comment.util;

import java.util.HashMap;
import java.util.Map;

/**
 * 功能描述：ThreadUtil
 * <p>
 * author: lefore
 * date: 2017/8/29
 * email: 862080515@qq.com
 */
public class ThreadUtil {

    private static final ThreadLocal ctx = new ThreadLocal();

    public static void put(Object key, Object value) {
        Map m = (Map) ctx.get();
        if (m == null) {
            m = new HashMap();
        }
        m.put(key, value);
        ctx.set(m);
    }

    public static Object get(Object key) {
        Map m = (Map) ctx.get();
        if (m != null) {
            return m.get(key);
        }
        return null;
    }
}
