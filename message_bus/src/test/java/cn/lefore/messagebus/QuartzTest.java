package cn.lefore.messagebus;

import cn.lefore.messagebus.common.utils.QuartzManager;
import org.quartz.*;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * author: lefore
 * date: 2017/8/6
 * email: 862080515@qq.com
 */
public class QuartzTest {

    public static void main(String[] args) {
        try {
            String jobName = "MyJob";
            System.out.println("【系统启动】开始(每1秒输出一次)...");
            QuartzManager.addJob(jobName, MyJob.class, "0/1 * * * * ?");

            Thread.sleep(5000);
            System.out.println("【修改时间】开始(每2秒输出一次)...");
            QuartzManager.modifyJobTime(jobName, "0/2 * * * * ?");

            Thread.sleep(6000);
            System.out.println("【移除定时】开始...");
            QuartzManager.removeJob(jobName);
            System.out.println("【移除定时】成功");

            System.out.println("【再次添加定时任务】开始(每10秒输出一次)...");
            QuartzManager.addJob(jobName, MyJob.class, "*/10 * * * * ?");
            Thread.sleep(60000);
            System.out.println("【移除定时】开始...");
            QuartzManager.removeJob(jobName);
            System.out.println("【移除定时】成功");

            QuartzManager.shutdownJobs();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }


    public static class MyJob implements Job {

        public void execute(JobExecutionContext context) throws JobExecutionException {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS");
            System.out.println(sdf.format(new Date()));
        }
    }
}
