package com.dev.user_transaction_management_system.domain.bank_account;

import com.dev.user_transaction_management_system.domain.NotifiableEvent;
import com.dev.user_transaction_management_system.domain.exceptions.CouldNotProcessTransaction;
import com.dev.user_transaction_management_system.domain.transaction.Amount;
import com.dev.user_transaction_management_system.domain.user.User;
import com.dev.user_transaction_management_system.infrastructure.persistence.model.BankAccountEntity;
import com.dev.user_transaction_management_system.use_case.dto.OpeningAccountResponse;
import com.dev.user_transaction_management_system.use_case.event.BankAccountActivated;
import com.dev.user_transaction_management_system.use_case.event.BankAccountOpened;
import com.dev.user_transaction_management_system.use_case.event.FundsDeposited;
import com.dev.user_transaction_management_system.use_case.event.FundsWithdrawn;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.util.Assert;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode
@ToString
public class BankAccount {

    private static final int MINIMUM_BALANCE = 100;

    private final AccountId accountId;
    private final AccountNumber accountNumber;
    private final User user;
    private Amount balance;
    private final LocalDateTime createdAt;
    private AccountStatus status;
    private final List<NotifiableEvent> events;

    private BankAccount(AccountId accountId,
                        AccountNumber accountNumber,
                        User user, Amount balance,
                        LocalDateTime createdAt,
                        AccountStatus status) {

        Assert.notNull(accountId, "account id cannot be null");
        Assert.notNull(accountNumber, "account number cannot be null");
        Assert.notNull(user, "user id cannot be null");
        Assert.notNull(balance, "balance cannot be null");
        Assert.notNull(createdAt, "created at cannot be null");

        if (!hasMinimumBalance(balance))
            throw new IllegalArgumentException("a bank account can't be opened unless the user deposits at least $100");

        this.accountId = accountId;
        this.accountNumber = accountNumber;
        this.user = user;
        this.balance = balance;
        this.createdAt = createdAt;
        this.status = status;
        this.events = new ArrayList<>();
        this.events.add(
                new BankAccountOpened(user.fullName(), accountNumber.asString(), user.email(), user.phoneNumber())
        );
    }

    private boolean hasMinimumBalance(Amount balance) {
        return balance.asDouble() >= MINIMUM_BALANCE;
    }

    public static BankAccount open(AccountId accountId,
                                   AccountNumber accountNumber,
                                   User user,
                                   Amount balance,
                                   AccountStatus status,
                                   LocalDateTime createdAt) {
        return new BankAccount(accountId, accountNumber, user, balance, createdAt, status);
    }

    public void increaseAmount(Amount amount) {
        if (isAccountDisable())
            throw CouldNotProcessTransaction.withDisabledAccount();

        final double increaseValue = this.balance.asDouble() + amount.asDouble();
        this.balance = Amount.of(increaseValue);
        this.events.add(fundsDeposited(amount.asDouble()));
    }

    private FundsDeposited fundsDeposited(double increaseValue) {
        return new FundsDeposited(increaseValue,
                accountNumber.last4Ending(),
                user.email(),
                user.phoneNumber(),
                balance.asDouble(),
                createdAt);
    }

    private boolean isAccountDisable() {
        return status == AccountStatus.DISABLE;
    }

    public void decreaseBalance(Amount amount) {
        Assert.notNull(amount, "amount cannot be null");
        ensureSufficientBalanceFor(amount);

        final double decreasedValue = this.balance.asDouble() - amount.asDouble();
        this.balance = Amount.of(decreasedValue);
        events.add(fundsWithdrawn(amount));
    }

    private FundsWithdrawn fundsWithdrawn(Amount amount) {
        return new FundsWithdrawn(amount.asDouble(),
                accountNumber.last4Ending(),
                user.email(),
                user.phoneNumber(),
                createdAt, balance.asDouble());
    }

    private void ensureSufficientBalanceFor(Amount amount) {
        if (!isBalanceSufficient(amount)) {
            throw CouldNotProcessTransaction.becauseInsufficientBalance();
        }
    }

    private boolean isBalanceSufficient(Amount amount) {
        return balance.asDouble() >= amount.asDouble();
    }

    public void enable() {
        this.status = AccountStatus.ENABLE;
        this.events.add(new BankAccountActivated(user.fullName(), accountNumber.asString(), user.email()));
    }

    public AccountNumber accountNumber() {
        return accountNumber;
    }

    public String accountNumberAsString() {
        return accountNumber.toString();
    }

    public BankAccountEntity toEntity() {
        final BankAccountEntity bankAccountEntity = new BankAccountEntity();
        bankAccountEntity.setAccountId(accountId.asString());
        bankAccountEntity.setAccountNumber(accountNumberAsString());
        bankAccountEntity.setUser(user.toEntity());
        bankAccountEntity.setBalance(balance.asDouble());
        bankAccountEntity.setStatus(status);
        bankAccountEntity.setCreatedAt(createdAt);
        return bankAccountEntity;
    }

    public OpeningAccountResponse toResponse(String fullName) {
        return new OpeningAccountResponse(accountNumber.toString(), fullName, balance.asDouble(), createdAt, status);
    }

    public List<NotifiableEvent> recordEvents() {
        return events;
    }
}
