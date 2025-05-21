package com.dev.user_transaction_management_system.fake;

import com.dev.user_transaction_management_system.domain.NotifiableEvent;
import com.dev.user_transaction_management_system.domain.Notifier;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service("fakeEmailNotifier")
@Slf4j
@Profile("test")
public class FakeEmailNotifier implements Notifier {

    @Override
    public void send(NotifiableEvent event) {
        log.info(String.format("### Sending Email..: %s",event.getMessage()));
    }
}
