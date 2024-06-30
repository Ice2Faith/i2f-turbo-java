package i2f.extension.quartz;

import i2f.extension.quartz.driven.QuartzScanner;
import i2f.extension.quartz.driven.model.QuartzJobMeta;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * @Ref https://blog.csdn.net/qq_37561309/article/details/94460115
 */
public class QuartzUtil {
    public static SchedulerFactory getStdSchedulerFactory() {
        return new StdSchedulerFactory();
    }

    public static Scheduler getScheduler() throws SchedulerException {
        return getScheduler(getStdSchedulerFactory());
    }

    public static Scheduler getScheduler(SchedulerFactory factory) throws SchedulerException {
        return factory.getScheduler();
    }

    public static JobDetail getJobDetail(Class<? extends Job> job,
                                         String name) {
        return getJobDetail(job, name, null, null);
    }

    public static JobDetail getJobDetail(Class<? extends Job> job,
                                         String name, String group) {
        return getJobDetail(job, name, group, null);
    }

    public static JobDetail getJobDetail(Class<? extends Job> job,
                                         String name, String group,
                                         JobDataMap datas) {
        JobBuilder builder = JobBuilder.newJob(job)
                .withIdentity(name, group);
        if (datas != null) {
            builder.usingJobData(datas);
        }
        return builder.build();
    }

    public static Trigger getIntervalTrigger(String name,
                                             int milliSecond) {
        return getIntervalTrigger(name, null, milliSecond, -1, null);
    }

    public static Trigger getIntervalTrigger(String name, String group,
                                             int milliSecond) {
        return getIntervalTrigger(name, group, milliSecond, -1, null);
    }

    public static Trigger getIntervalTrigger(String name, String group,
                                             long milliSecond, int count) {
        return getIntervalTrigger(name, group, milliSecond, count, null);
    }

    public static Trigger getIntervalTrigger(String name, String group,
                                             long milliSecond, int count,
                                             JobDataMap datas) {
        SimpleScheduleBuilder builder = SimpleScheduleBuilder.simpleSchedule()
                .withIntervalInMilliseconds(milliSecond);
        if (count >= 0) {
            builder.withRepeatCount(count);
        } else {
            builder.repeatForever();
        }
        TriggerBuilder triggerBuilder = TriggerBuilder.newTrigger()
                .withIdentity(name, group)
                .startNow()
                .withSchedule(builder);
        if (datas != null) {
            triggerBuilder.usingJobData(datas);
        }
        return triggerBuilder.build();
    }

    public static Trigger getCronTrigger(String name, String cronExp) {
        return getCronTrigger(name, null, cronExp, null);
    }

    public static Trigger getCronTrigger(String name, String group, String cronExp) {
        return getCronTrigger(name, group, cronExp, null);
    }

    //线生成Cron表达式的工具：http://cron.qqe2.com/
    public static Trigger getCronTrigger(String name, String group,
                                         String cronExp,
                                         JobDataMap datas) {
        CronScheduleBuilder builder = CronScheduleBuilder.cronSchedule(cronExp);
        TriggerBuilder triggerBuilder = TriggerBuilder.newTrigger()
                .withIdentity(name, group)
                .startNow()
                .withSchedule(builder);
        if (datas != null) {
            triggerBuilder.usingJobData(datas);
        }
        return triggerBuilder.build();
    }

    public static Scheduler doSchedule(JobDetail jobDetail, Trigger trigger) throws SchedulerException {
        Scheduler scheduler = getScheduler();
        doSchedule(scheduler, jobDetail, trigger);
        return scheduler;
    }

    public static void doSchedule(Scheduler scheduler, JobDetail jobDetail, Trigger trigger) throws SchedulerException {
        scheduler.scheduleJob(jobDetail, trigger);
        scheduler.start();
    }

    public static Object getJobData(JobExecutionContext context, Object key) {
        return context.getJobDetail().getJobDataMap().get(key);
    }

    public static Object getTriggerData(JobExecutionContext context, Object key) {
        return context.getTrigger().getJobDataMap().get(key);
    }

    public static JobKey jobKey(String name) {
        return JobKey.jobKey(name, null);
    }

    public static JobKey jobKey(String name, String group) {
        return JobKey.jobKey(name, group);
    }

    public static boolean deleteJob(Scheduler scheduler, JobKey key) throws SchedulerException {
        return scheduler.deleteJob(key);
    }

    public static boolean deleteJob(Scheduler scheduler, String name) throws SchedulerException {
        return deleteJob(scheduler, jobKey(name, null));
    }

    public static boolean deleteJob(Scheduler scheduler, String name, String group) throws SchedulerException {
        return deleteJob(scheduler, jobKey(name, group));
    }

    public static boolean deleteJob(Scheduler scheduler, QuartzJobMeta meta) throws SchedulerException {
        return deleteJob(scheduler, meta.getName(), meta.getGroup());
    }

    public static TriggerKey triggerKey(String name) {
        return TriggerKey.triggerKey(name, null);
    }

    public static TriggerKey triggerKey(String name, String group) {
        return TriggerKey.triggerKey(name, group);
    }

    public static Date updateTrigger(Scheduler scheduler,
                                     TriggerKey key,
                                     Trigger trigger) throws SchedulerException {
        return scheduler.rescheduleJob(key, trigger);
    }

    public static void pauseJob(Scheduler scheduler, JobKey key) throws SchedulerException {
        scheduler.pauseJob(key);
    }

    public static void resumeJob(Scheduler scheduler, JobKey key) throws SchedulerException {
        scheduler.resumeJob(key);
    }

    public static void runOnce(Scheduler scheduler, JobKey key) throws SchedulerException {
        scheduler.triggerJob(key);
    }

    public static List<QuartzJobMeta> bootByScanner(Scheduler scheduler, String... basePackages) throws IOException, SchedulerException {
        List<QuartzJobMeta> meta = QuartzScanner.scanBasePackage(basePackages);
        for (QuartzJobMeta item : meta) {
            QuartzScanner.makeSchedule(scheduler, item);
        }
        return meta;
    }
}
