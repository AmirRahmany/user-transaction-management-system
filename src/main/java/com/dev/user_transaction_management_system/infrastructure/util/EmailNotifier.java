package com.dev.user_transaction_management_system.infrastructure.util;

import com.dev.user_transaction_management_system.domain.NotifiableEvent;
import com.dev.user_transaction_management_system.domain.Notifier;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class EmailNotifier implements Notifier {

    @Override
    public void send(NotifiableEvent event) {
        final String emailBody = String.format("##Sending Email... : %s to %s", event.getMessage(), event.email());
        log.info(emailBody);
    }
}
