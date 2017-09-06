package cn.lefore.messagebus.common.utils;

import cn.lefore.messagebus.common.constants.ConstantMess;
import cn.lefore.messagebus.manager.ILongConnectManage;
import cn.lefore.messagebus.manager.impl.LongConnectManage;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * author: lefore
 * date: 2017/8/7
 * email: 862080515@qq.com
 */
public class Myjob implements Job {

    private static final Logger logger = LogHelper.getLogger();

    public void execute(JobExecutionContext context) throws JobExecutionException {
        // 获取 Job 和 Trigger 的并集的 map
        JobDataMap jobDataMap = context.getMergedJobDataMap();
        Object message = jobDataMap.get("message");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS");
        logger.info(sdf.format(new Date()));
        logger.info(message.toString());
        ILongConnectManage connectManage = new LongConnectManage();
        connectManage.sendMessage(message.toString(), "", ConstantMess.FORALLCLIENTS);
    }
}
