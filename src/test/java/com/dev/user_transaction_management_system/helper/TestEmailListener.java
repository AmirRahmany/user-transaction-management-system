package com.dev.user_transaction_management_system.helper;

import com.dev.user_transaction_management_system.domain.NotifiableEvent;
import com.dev.user_transaction_management_system.infrastructure.util.EmailNotifier;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Profile("test")
@Component
public class TestEmailListener {

    private final EmailNotifier emailNotifier;

    public TestEmailListener(EmailNotifier emailNotifier) {
        this.emailNotifier = emailNotifier;
    }

    @EventListener
    public void onAllEvents(NotifiableEvent event) {
        this.emailNotifier.send(event);
    }

}
