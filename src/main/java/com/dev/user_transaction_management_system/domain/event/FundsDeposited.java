package com.dev.user_transaction_management_system.domain.event;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static java.lang.String.format;

public record FundsDeposited(double increaseAmount,
                             String accountNumber,
                             String toEmail,
                             String phoneNumber,
                             double availableBalance,
                             LocalDateTime createdAt) implements NotifiableEvent {

    @Override
    public String getMessage() {
        return format("Deposit: Rs. %s received in your saving " +
                "account ending in %s on %s at %s. " +
                "Your available balance is Rs. %s.",
                increaseAmount, accountNumber,createdAt.toLocalDate(), timeFormat(),availableBalance);
    }

    @Override
    public String getSubject() {
        return "Funds Deposit";
    }

    private String timeFormat() {
        return createdAt.toLocalTime()
                .format(DateTimeFormatter.ofPattern("HH:mm"));
    }
}
