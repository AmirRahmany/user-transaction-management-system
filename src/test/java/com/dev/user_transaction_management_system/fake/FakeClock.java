package com.dev.user_transaction_management_system.fake;

import com.dev.user_transaction_management_system.domain.Clock;
import com.dev.user_transaction_management_system.domain.Date;

import java.time.LocalDateTime;

import static java.time.LocalDateTime.of;

public class FakeClock implements Clock {

    @Override
    public LocalDateTime currentTime() {
        return of(2025, 5, 23, 18, 25);
    }
}
