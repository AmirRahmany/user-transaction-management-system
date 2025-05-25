package com.dev.user_transaction_management_system.domain.bank_account;

import com.dev.user_transaction_management_system.domain.event.Message;
import com.dev.user_transaction_management_system.domain.event.NotifiableEvent;
import com.dev.user_transaction_management_system.domain.event.Subject;
import com.dev.user_transaction_management_system.domain.user.Email;

import static java.lang.String.format;

public record FundsWereDeposited(double increaseAmount,
                                 String accountNumber,
                                 String receiverEmail,
                                 String phoneNumber,
                                 double availableBalance,
                                 String createdAt) implements NotifiableEvent {

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
        final String message = """
                Rs. %s received in your saving account ending in %s at %s.
                Your available balance is Rs. %s.""";

        return format(message,
                increaseAmount, accountNumber, createdAt, availableBalance);
    }


}
