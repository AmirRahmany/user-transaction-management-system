package com.dev.user_transaction_management_system.infrastructure.util;

import com.dev.user_transaction_management_system.domain.NotifiableEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Profile("!test")
@Component
public class EmailListener {

    private final EmailNotifierWithGmail notifier;

    public EmailListener(EmailNotifierWithGmail notifier) {
        this.notifier = notifier;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onNotifiableEvent(NotifiableEvent event) {
        this.notifier.send(event);
    }
}
