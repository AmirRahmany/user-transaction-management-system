package com.dev.user_transaction_management_system.domain.event;

import com.dev.user_transaction_management_system.domain.user.Email;

import java.time.LocalDateTime;

import static java.lang.String.format;

public record FundsWithdrawn(double decreaseAmount,
                             String from,
                             String receiverEmail,
                             String phoneNumber,
                             LocalDateTime createdAt,
                             double availableBalance) implements NotifiableEvent {
    @Override
    public Subject subject() {
        return Subject.of("Funds Withdrawn");
    }

    @Override
    public Message message() {
        return Message.of(body());
    }

    private String body() {
        return format(" Rs.%s debited from your saving account ending" +
                        " in %s on %s at %s." +
                        " Available balance: Rs.%s.",
                decreaseAmount, from, createdAt.toLocalDate(), createdAt.toLocalTime(), availableBalance);
    }

    @Override
    public Email to(){
        return Email.of(receiverEmail);
    }
}
