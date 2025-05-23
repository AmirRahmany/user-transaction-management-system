package com.dev.user_transaction_management_system.fake;

import com.dev.user_transaction_management_system.domain.Calendar;
import com.dev.user_transaction_management_system.domain.Date;

import java.time.LocalDateTime;

import static java.time.LocalDateTime.of;

public class FakeCalendar implements Calendar {

    @Override
    public Date today() {
        final var localDateTime = of(2025, 5, 23, 18, 25);
        return Date.fromLocalDateTime(localDateTime);
    }
}
