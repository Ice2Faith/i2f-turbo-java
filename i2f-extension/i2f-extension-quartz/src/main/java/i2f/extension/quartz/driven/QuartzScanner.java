package i2f.extension.quartz.driven;

import i2f.extension.quartz.QuartzUtil;
import i2f.extension.quartz.driven.anntation.QuartzSchedule;
import i2f.extension.quartz.driven.enums.ScheduleType;
import i2f.extension.quartz.driven.job.QuartzAnnotationJob;
import i2f.extension.quartz.driven.model.QuartzJobMeta;
import i2f.reflect.ReflectResolver;
import i2f.resources.ResourcesLoader;
import org.quartz.*;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class QuartzScanner {
    public static List<QuartzJobMeta> scanBasePackage(String... pkgs) throws IOException {
        Map<String, Class<?>> map = ResourcesLoader.scanClassNamesBasePackages(null, pkgs);
        List<Class> classes = new ArrayList<>();
        for (Map.Entry<String, Class<?>> entry : map.entrySet()) {
            classes.add(entry.getValue());
        }
        return scans(classes);
    }

    public static List<QuartzJobMeta> scans(List<Class> classes) {
        List<QuartzJobMeta> ret = new ArrayList<>();
        for (Class item : classes) {
            List<QuartzJobMeta> list = scan(item);
            ret.addAll(list);
        }
        return ret;
    }

    public static List<QuartzJobMeta> scans(Class... clazzes) {
        List<QuartzJobMeta> ret = new ArrayList<>();
        for (Class item : clazzes) {
            List<QuartzJobMeta> list = scan(item);
            ret.addAll(list);
        }
        return ret;
    }

    public static List<QuartzJobMeta> scan(Class clazz) {
        List<QuartzJobMeta> ret = new ArrayList<>();
        Map<Method, Class<?>> map = ReflectResolver.getMethods(clazz, (method) -> {
            QuartzSchedule ann = ReflectResolver.getAnnotation(method, QuartzSchedule.class);
            if (ann == null) {
                return false;
            }
            return true;
        });
        if (map.isEmpty()) {
            return ret;
        }
        for (Method item : map.keySet()) {
            QuartzSchedule ann = ReflectResolver.getAnnotation(item, QuartzSchedule.class);
            if (ann == null || !ann.value()) {
                continue;
            }
            QuartzJobMeta meta = QuartzJobMeta.build()
                    .buildByAnnotation(ann)
                    .buildByMethod(item);
            ret.add(meta);
        }
        return ret;
    }


    public static Scheduler makeSchedule(Scheduler scheduler, QuartzJobMeta meta) throws SchedulerException {
        if (meta == null) {
            return null;
        }
        JobDataMap dataMap = new JobDataMap();
        dataMap.put("meta", meta);
        JobDetail jobDetail = QuartzUtil.getJobDetail(QuartzAnnotationJob.class, meta.getName(), meta.getGroup(), dataMap);
        Trigger trigger = null;
        if (meta.getType() == ScheduleType.Interval) {
            trigger = QuartzUtil.getIntervalTrigger(meta.getName(), meta.getGroup(), meta.getIntervalTimeUnit().toMillis(meta.getIntervalTime()), meta.getIntervalCount());
        } else if (meta.getType() == ScheduleType.Cron) {
            trigger = QuartzUtil.getCronTrigger(meta.getName(), meta.getGroup(), meta.getCron());
        }
        TriggerKey triggerKey = QuartzUtil.triggerKey(meta.getName(), meta.getGroup());
        Trigger hasTrigger = scheduler.getTrigger(triggerKey);
        if (hasTrigger != null) {
            QuartzUtil.updateTrigger(scheduler, triggerKey, trigger);
        } else {
            QuartzUtil.doSchedule(scheduler, jobDetail, trigger);
        }
        return scheduler;
    }
}
