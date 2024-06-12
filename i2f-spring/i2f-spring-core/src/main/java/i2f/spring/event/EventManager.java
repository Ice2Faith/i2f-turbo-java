package i2f.spring.event;

import lombok.NoArgsConstructor;
import org.springframework.beans.BeansException;
import org.springframework.context.*;
import org.springframework.stereotype.Component;

/**
 * @author ltb
 * @date 2022/4/15 9:22
 * @desc
 */
@NoArgsConstructor
public class EventManager implements ApplicationContextAware, ApplicationEventPublisherAware {

    private ApplicationEventPublisher publisher;
    private ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.publisher = applicationEventPublisher;
    }


    ///////////////////////////////////////////

    public EventManager(ApplicationContext context) {
        this.context = context;
    }

    public EventManager(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    public void publish(ApplicationEvent event) {
        if (publisher != null) {
            publisher.publishEvent(event);
        }
        context.publishEvent(event);
    }

    public void publish(int code, String msg, Object data) {
        publish(Event.builder(code, msg, data));
    }

    public void publish(int code, String msg) {
        publish(Event.builder(code, msg));
    }

    public void publish(int code, Object data) {
        publish(Event.builder(code, data));
    }
}
