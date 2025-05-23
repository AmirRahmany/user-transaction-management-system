package com.dev.user_transaction_management_system.infrastructure.service;

import com.dev.user_transaction_management_system.domain.Clock;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
@Service
public class ClockUseLocalDateTime implements Clock {

    @Override
    public LocalDateTime currentTime() {
        return LocalDateTime.now();
    }
}
