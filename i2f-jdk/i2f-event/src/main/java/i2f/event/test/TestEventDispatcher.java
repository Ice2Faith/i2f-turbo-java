package i2f.event.test;

import i2f.event.EventDispatcher;
import i2f.event.IEventSubscriber;

/**
 * @author Ice2Faith
 * @date 2024/8/9 22:00
 * @desc
 */
public class TestEventDispatcher {
    public static void main(String[] args) {
        EventDispatcher<Object> publisher = new EventDispatcher<>();
        publisher.setWaitSubscriber(true);
        publisher.setDispatcherThreadConsumer((thread, dispatcher) -> {
            thread.setDaemon(false);
            thread.setPriority(10);
        });
        new Thread(() -> {
            for (int i = 0; i < 2; i++) {
                try {
                    Thread.sleep((int) (Math.random() * 300));
                } catch (Exception e) {

                }
                String handlerName = "handler-" + i + "-";
                publisher.subscribe(new IEventSubscriber<Object>() {
                    @Override
                    public void handle(Object event) {
                        String name = Thread.currentThread().getName();
                        long id = Thread.currentThread().getId();
                        String threadName = name + "-" + id;
                        System.out.println(threadName + ": " + handlerName + event);
                    }
                });
            }
        }).start();
        for (int i = 0; i < 10; i++) {
            String taskName = "publisher-" + i + "-";
            new Thread(() -> {
                for (int j = 0; j < 10; j++) {
                    try {
                        Thread.sleep((int) (Math.random() * 30));
                    } catch (Exception e) {

                    }
                    String name = Thread.currentThread().getName();
                    long id = Thread.currentThread().getId();
                    String threadName = name + "-" + id;
                    System.out.println(threadName + ": " + taskName + j);
                    publisher.publish(taskName + j);
                }
            }).start();
        }


    }
}
