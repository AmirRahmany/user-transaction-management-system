package com.dev.user_transaction_management_system.use_case.event;

import com.dev.user_transaction_management_system.domain.NotifiableEvent;

import java.time.LocalDateTime;

import static java.lang.String.format;

public record FundsWithdrawn(double decreaseAmount,
                             String from,
                             String toEmail,
                             String phoneNumber,
                             LocalDateTime createdAt,
                             double availableBalance) implements NotifiableEvent {
    @Override
    public String getMessage() {
        return format(" Rs.%s debited from your saving account ending" +
                " in %s on %s at %s." +
                " Available balance: Rs.%s.",
                decreaseAmount,from,createdAt.toLocalDate(),createdAt.toLocalTime(),availableBalance);
    }

    @Override
    public String getSubject() {
        return "Funds Withdrawn";
    }
}
