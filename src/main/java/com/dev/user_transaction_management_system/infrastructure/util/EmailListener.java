package com.dev.user_transaction_management_system.infrastructure.util;

import com.dev.user_transaction_management_system.domain.Event;
import com.dev.user_transaction_management_system.use_case.event.RegisteredUserAccount;
import com.dev.user_transaction_management_system.use_case.event.UserAccountActivated;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class EmailListener {

    private final EmailNotifier notifier;

    public EmailListener(EmailNotifier notifier) {
        this.notifier = notifier;
    }

    @EventListener
    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void onRegisteredUserEvent(RegisteredUserAccount event) {
        this.notifier.send(event.getMessage(), event.email());
    }

    @EventListener
    //@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onActivatedUserAccountEvent(UserAccountActivated event) {
        this.notifier.send(event.getMessage(), event.email());
    }
}
