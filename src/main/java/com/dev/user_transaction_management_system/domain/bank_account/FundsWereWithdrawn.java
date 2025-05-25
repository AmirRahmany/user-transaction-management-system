package com.dev.user_transaction_management_system.domain.bank_account;

import com.dev.user_transaction_management_system.domain.event.Message;
import com.dev.user_transaction_management_system.domain.event.NotifiableEvent;
import com.dev.user_transaction_management_system.domain.event.Subject;
import com.dev.user_transaction_management_system.domain.user.Email;

import static java.lang.String.format;

public record FundsWereWithdrawn(double decreaseAmount,
                                 String from,
                                 String receiverEmail,
                                 String phoneNumber,
                                 String createdAt,
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
        final String message = """
                Rs.%s debited from your saving account ending  in %s at %s. Available balance: Rs. %s.
                """;

        return format(message, decreaseAmount, from, createdAt, availableBalance);
    }

    @Override
    public Email to() {
        return Email.of(receiverEmail);
    }
}
