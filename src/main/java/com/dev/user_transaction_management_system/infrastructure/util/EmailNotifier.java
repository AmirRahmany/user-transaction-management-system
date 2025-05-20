package com.dev.user_transaction_management_system.infrastructure.util;

import com.dev.user_transaction_management_system.domain.Notifier;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class EmailNotifier implements Notifier {

    @Override
    public void send(String message,String toEmail) {
        final String emailBody = String.format("##Sending Email... : %s to %s", message, toEmail);
        log.info(emailBody);
    }
}
