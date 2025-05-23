package com.dev.user_transaction_management_system.domain.event;

import com.dev.user_transaction_management_system.domain.user.Email;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static java.lang.String.format;

public record FundsDeposited(double increaseAmount,
                             String accountNumber,
                             String receiverEmail,
                             String phoneNumber,
                             double availableBalance,
                             LocalDateTime createdAt) implements NotifiableEvent {

    @Override
    public Subject subject() {
        return Subject.of("Funds Deposit");
    }

    @Override
    public Message message() {
        return Message.of(body());
    }

    @Override
    public Email to() {
        return Email.of(receiverEmail);
    }

    private String body() {
        return format("Deposit: Rs. %s received in your saving " +
                        "account ending in %s on %s at %s. " +
                        "Your available balance is Rs. %s.",
                increaseAmount, accountNumber, createdAt.toLocalDate(), timeFormat(), availableBalance);
    }


    private String timeFormat() {
        return createdAt.toLocalTime()
                .format(DateTimeFormatter.ofPattern("HH:mm"));
    }
}
