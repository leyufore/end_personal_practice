package cn.lefore.messagebus.common.utils;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * author: lefore
 * date: 2017/8/6
 * email: 862080515@qq.com
 */
public class QuartzManager {

    private static SchedulerFactory gSchedulerFactory = new StdSchedulerFactory();
    private static String JOB_GROUP_NAME = "EXTJWEB_JOBGROUP_NAME";
    private static String TRIGGER_GROUP_NAME = "EXTJWEB_TRIGGERGROUP_NAME";

    /**
     * 添加一个定时任务，使用默认的任务组名，触发器名，触发器组名
     *
     * @param jobName  任务名
     * @param jobClass 任务
     * @param cronTime 时间设置，参考quartz说明文档
     * @param map
     */
    public static void addJob(String jobName, Class jobClass, String cronTime, JobDataMap map) {
        try {
            Scheduler scheduler = gSchedulerFactory.getScheduler();
            // 作业
            JobDetail jobDetail = JobBuilder.newJob(jobClass)
                    .withIdentity(jobName, JOB_GROUP_NAME)
                    .setJobData(map)
                    .build();
            // 触发器
            // Job 的 name 和 Trigger 的 name 可不一样
            Trigger trigger = TriggerBuilder.newTrigger()
                    .withIdentity(jobName, TRIGGER_GROUP_NAME)
                    .withSchedule(CronScheduleBuilder
                            .cronSchedule(cronTime)
                            .withMisfireHandlingInstructionDoNothing())
                    .build();
            scheduler.scheduleJob(jobDetail, trigger);
            if (!scheduler.isShutdown() && !scheduler.isStarted()) {
                scheduler.start();
            }

        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    /**
     * 添加一个定时任务，使用默认的任务组名，触发器名，触发器组名
     *
     * @param jobName  任务名
     * @param jobClass 任务
     * @param cronTime 时间设置，参考quartz说明文档
     */
    public static void addJob(String jobName, Class jobClass, String cronTime) {
        addJob(jobName, jobClass, cronTime, new JobDataMap());
    }

    /**
     * 修改一个任务的触发时间(使用默认的任务组名，触发器名，触发器组名)
     *
     * @param triggerName
     * @param cronTime
     */
    public static void modifyJobTime(String triggerName, String cronTime) {
        try {
            Scheduler scheduler = gSchedulerFactory.getScheduler();
            CronTrigger trigger = (CronTrigger) scheduler.getTrigger(TriggerKey.triggerKey(triggerName, TRIGGER_GROUP_NAME));
            if (trigger == null) {
                return;
            }
            String oldTime = trigger.getCronExpression();
            if (!oldTime.equalsIgnoreCase(cronTime)) {
                Trigger newTrigger = trigger.getTriggerBuilder()
                        .withSchedule(CronScheduleBuilder
                                .cronSchedule(cronTime))
                        .build();
                // 此处会出现在开始的一瞬间，自动触发的情形，原因未明。
                // 故，若是在部分需要准确的执行的场景，采用先删除再重新添加的方式
                scheduler.rescheduleJob(trigger.getKey(), newTrigger);
            }

        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    /**
     * 移除一个任务(使用默认的任务组名，触发器名，触发器组名)
     *
     * @param jobName
     */
    public static void removeJob(String jobName) {
        try {
            Scheduler scheduler = gSchedulerFactory.getScheduler();
            scheduler.deleteJob(new JobKey(jobName, JOB_GROUP_NAME));
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    /**
     * 启动所有定时任务
     */
    public static void startJobs() {
        try {
            Scheduler sched = gSchedulerFactory.getScheduler();
            sched.start();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * 关闭所有定时任务
     */
    public static void shutdownJobs() {
        try {
            Scheduler scheduler = gSchedulerFactory.getScheduler();
            if (!scheduler.isShutdown()) {
                scheduler.shutdown();
            }
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }
}
