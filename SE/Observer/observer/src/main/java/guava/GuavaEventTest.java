package guava;

import com.google.common.eventbus.EventBus;
import org.springframework.web.context.ContextLoaderListener;


public class GuavaEventTest {

    public static void main(String[] args) {
        //消息总线
        EventBus eventBus = new EventBus();
        GuavaEvent guavaEvent = new GuavaEvent();
        eventBus.register(guavaEvent);
        eventBus.post("Tom");
    }
}
