package com.dev.user_transaction_management_system.fake;

import com.dev.user_transaction_management_system.domain.event.Message;
import com.dev.user_transaction_management_system.domain.event.Subject;
import com.dev.user_transaction_management_system.domain.event.Notifier;
import com.dev.user_transaction_management_system.domain.user.Email;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service("fakeEmailNotifier")
@Slf4j
@Profile("test")
public class FakeEmailNotifier implements Notifier {

    @Override
    public void sendSimpleMessage(Subject subject , Message message, Email to) {
        log.info(String.format("### Sending Email (%s) to %s..: %s",subject,to,message.body()));
    }
}
