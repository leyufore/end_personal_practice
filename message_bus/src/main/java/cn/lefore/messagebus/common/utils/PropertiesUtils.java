package cn.lefore.messagebus.common.utils;

import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.IOException;
import java.util.Properties;

/**
 * author: lefore
 * date: 2017/8/8
 * email: 862080515@qq.com
 */
public class PropertiesUtils {

    public static String getProperty(String name) {
        Properties props = new Properties();
        try {
            props = PropertiesLoaderUtils.loadAllProperties("jpush.properties");
            if (props.containsKey(name)) {
                return props.getProperty(name);
            } else {
                return "";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}
