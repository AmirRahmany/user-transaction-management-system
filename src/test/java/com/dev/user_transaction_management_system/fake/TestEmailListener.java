package com.dev.user_transaction_management_system.fake;

import com.dev.user_transaction_management_system.domain.NotifiableEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Profile("test")
@Component
public class TestEmailListener {

    private final FakeEmailNotifier emailNotifier;

    public TestEmailListener(FakeEmailNotifier emailNotifier) {
        this.emailNotifier = emailNotifier;
    }

    @EventListener
    public void onAllEvents(NotifiableEvent event) {
        this.emailNotifier.send(event);
    }

}
