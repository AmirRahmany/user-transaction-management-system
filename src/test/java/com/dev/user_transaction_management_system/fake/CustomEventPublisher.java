package com.dev.user_transaction_management_system.fake;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;

@Slf4j
public class CustomEventPublisher implements ApplicationEventPublisher {

    @Override
    public void publishEvent(Object event) {
        log.info("an event published");
    }
}
