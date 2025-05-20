package com.dev.user_transaction_management_system.infrastructure.util;

import com.dev.user_transaction_management_system.use_case.event.BankAccountActivated;
import com.dev.user_transaction_management_system.use_case.event.RegisteredUserAccount;
import com.dev.user_transaction_management_system.use_case.event.UserAccountActivated;
import org.springframework.context.annotation.Profile;
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
    public void onRegisteredUserAccountEvent(RegisteredUserAccount event) {
        this.notifier.send(event.getMessage(), event.email());
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onActivatedUserAccountEvent(UserAccountActivated event) {
        this.notifier.send(event.getMessage(), event.email());
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onBankAccountActivatedEvent(BankAccountActivated event) {
        this.notifier.send(event.getMessage(), event.email());
    }
}
