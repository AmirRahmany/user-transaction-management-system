package com.dev.user_transaction_management_system.infrastructure.service;

import com.dev.user_transaction_management_system.domain.Clock;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ClockUseSystemClock implements Clock {

    @Override
    public LocalDateTime currentTime() {
        return LocalDateTime.now();
    }
}
