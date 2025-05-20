package com.dev.user_transaction_management_system.helper;

import com.dev.user_transaction_management_system.domain.Event;
import com.dev.user_transaction_management_system.infrastructure.util.EmailNotifier;
import com.dev.user_transaction_management_system.use_case.event.BankAccountActivated;
import com.dev.user_transaction_management_system.use_case.event.RegisteredUserAccount;
import com.dev.user_transaction_management_system.use_case.event.UserAccountActivated;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Profile("test")
@Component
public class TestEmailListener {

    private final EmailNotifier emailNotifier;

    public TestEmailListener(EmailNotifier emailNotifier) {
        this.emailNotifier = emailNotifier;
    }

    @EventListener
    public void onAllEvents(Event event) {
        this.emailNotifier.send(event.getMessage(), event.email());
    }

}
