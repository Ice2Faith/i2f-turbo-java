package i2f.springboot.spring.holder;

import i2f.spring.core.SpringUtil;
import i2f.spring.enviroment.EnvironmentUtil;
import i2f.spring.event.EventManager;

import java.util.concurrent.CountDownLatch;

/**
 * @author Ice2Faith
 * @date 2024/6/12 10:04
 * @desc
 */
public class SpringContextHolder {
    private static SpringUtil springUtil;
    private static CountDownLatch latchSpringUtil = new CountDownLatch(1);

    private static EnvironmentUtil environmentUtil;
    private static CountDownLatch latchEnvironmentUtil = new CountDownLatch(1);

    private static EventManager eventManager;
    private static CountDownLatch latchEventManager = new CountDownLatch(1);

    public static SpringUtil getSpringUtil() {
        try {
            latchSpringUtil.await();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return springUtil;
    }

    public static void setSpringUtil(SpringUtil springUtil) {
        SpringContextHolder.springUtil = springUtil;
        latchSpringUtil.countDown();
    }

    public static EnvironmentUtil getEnvironmentUtil() {
        try {
            latchEnvironmentUtil.await();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return environmentUtil;
    }

    public static void setEnvironmentUtil(EnvironmentUtil environmentUtil) {
        SpringContextHolder.environmentUtil = environmentUtil;
        latchEnvironmentUtil.countDown();
    }

    public static EventManager getEventManager() {
        try {
            latchEventManager.await();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return eventManager;
    }

    public static void setEventManager(EventManager eventManager) {
        SpringContextHolder.eventManager = eventManager;
        latchEventManager.countDown();
    }
}
