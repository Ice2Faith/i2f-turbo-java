package i2f.extension.quartz.driven.job;


import i2f.extension.quartz.QuartzUtil;
import i2f.extension.quartz.driven.model.QuartzJobMeta;
import i2f.reflect.ReflectResolver;
import lombok.SneakyThrows;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class QuartzAnnotationJob implements Job {
    @SneakyThrows
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        QuartzJobMeta meta = (QuartzJobMeta) QuartzUtil.getJobData(context, "meta");
        Method method = meta.getRunMethod();
        if (method == null) {
            Class<?> clazz = ReflectResolver.loadClass(meta.getRunClassName());
            method = ReflectResolver.getMethod(clazz, meta.getRunMethodName());
            meta.setRunClass(clazz);
            meta.setRunMethod(method);
        }
        if (Modifier.isStatic(method.getModifiers())) {
            ReflectResolver.invokeStaticMethod(method);
        } else {
            Class clazz = method.getDeclaringClass();
            Object obj = meta.getInvokeObj();
            if (obj == null) {
                obj = ReflectResolver.getInstance(clazz);
            }
            ReflectResolver.invokeMethodeDirect(obj, method);
        }
    }
}
