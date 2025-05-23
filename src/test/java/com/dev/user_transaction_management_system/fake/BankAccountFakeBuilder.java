package com.dev.user_transaction_management_system.fake;

import com.dev.user_transaction_management_system.domain.Date;
import com.dev.user_transaction_management_system.domain.bank_account.AccountStatus;
import com.dev.user_transaction_management_system.domain.bank_account.BankAccount;
import com.dev.user_transaction_management_system.domain.bank_account.AccountId;
import com.dev.user_transaction_management_system.domain.bank_account.AccountNumber;
import com.dev.user_transaction_management_system.domain.transaction.Amount;
import com.dev.user_transaction_management_system.domain.user.User;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

import static com.dev.user_transaction_management_system.domain.bank_account.AccountStatus.DISABLE;
import static com.dev.user_transaction_management_system.domain.bank_account.AccountStatus.ENABLE;
import static com.dev.user_transaction_management_system.fake.UserFakeBuilder.aUser;

public class BankAccountFakeBuilder {
    private UUID accountId = UUID.randomUUID();
    private String accountNumber = "0300123002145";
    private User user = aUser().build();
    private Amount balance = Amount.of(5000.50);
    private AccountStatus accountStatus = DISABLE;
    private final Date createdAt = (new FakeCalendar()).today();

    public static BankAccountFakeBuilder anAccount() {
        return new BankAccountFakeBuilder();
    }

    public BankAccountFakeBuilder withAccountId(String accountId) {
        this.accountId = UUID.fromString(accountId);
        return this;
    }

    public BankAccountFakeBuilder withUser(User user) {
        this.user = user;
        return this;
    }

    public BankAccountFakeBuilder withAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
        return this;
    }

    public BankAccountFakeBuilder withBalance(double balance) {
        this.balance = Amount.of(balance);
        return this;
    }

    public BankAccountFakeBuilder withInsufficientBalance() {
        this.balance = Amount.of(200);
        return this;
    }

    public BankAccountFakeBuilder enabled(){
        this.accountStatus = ENABLE;
        return this;
    }

    public BankAccountFakeBuilder disabled() {
        this.accountStatus = DISABLE;
        return this;
    }

    public BankAccount open() {
        final AccountId id = AccountId.fromUUID(UUID.fromString(accountId.toString()));
        final AccountNumber bankAccountNumber = AccountNumber.of(accountNumber);
        return BankAccount.open(id, bankAccountNumber, user, balance, accountStatus, createdAt);
    }
}
