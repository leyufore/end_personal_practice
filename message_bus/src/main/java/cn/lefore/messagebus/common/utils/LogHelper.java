package cn.lefore.messagebus.common.utils;

import org.apache.log4j.Logger;

/**
 * Created by wenrule on 2017/7/31.
 */
public class LogHelper {

    protected Logger logger;

    private static volatile LogHelper instance = null;

    private LogHelper() {
        this.logger = Logger.getLogger("lefore_message_bus");
    }

    public static Logger getLogger() {
        if (instance == null) {
            synchronized (LogHelper.class) {
                if (instance == null) {
                    instance = new LogHelper();
                }
            }
        }
        return instance.logger;
    }

    public void setLogger(Logger logger) {
        this.logger = logger;
    }
}
