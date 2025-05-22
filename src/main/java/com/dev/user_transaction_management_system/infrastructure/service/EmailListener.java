package com.dev.user_transaction_management_system.infrastructure.service;

import com.dev.user_transaction_management_system.domain.event.NotifiableEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Profile("!test")
@Component
public class EmailListener {

    private final EmailNotifier notifier;

    public EmailListener(EmailNotifier notifier) {
        this.notifier = notifier;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Async
    public void onNotifiableEvent(NotifiableEvent event) {
        this.notifier.send(event);
    }
}
