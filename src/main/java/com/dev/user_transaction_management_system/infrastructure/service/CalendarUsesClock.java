package com.dev.user_transaction_management_system.infrastructure.service;

import com.dev.user_transaction_management_system.domain.Calendar;
import com.dev.user_transaction_management_system.domain.Clock;
import com.dev.user_transaction_management_system.domain.Date;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service
public class CalendarUsesClock implements Calendar {

    private final Clock clock;

    public CalendarUsesClock(@NonNull Clock clock) {
        Assert.notNull(clock,"clock dependency cannot be null");

        this.clock = clock;
    }

    @Override
    public Date today() {
        return Date.fromLocalDateTime(clock.currentTime());
    }
}
