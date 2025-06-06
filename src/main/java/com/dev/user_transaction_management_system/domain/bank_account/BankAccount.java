package com.dev.user_transaction_management_system.domain.bank_account;

import com.dev.user_transaction_management_system.domain.Date;
import com.dev.user_transaction_management_system.domain.event.NotifiableEvent;
import com.dev.user_transaction_management_system.domain.exceptions.CouldNotProcessTransaction;
import com.dev.user_transaction_management_system.domain.transaction.Amount;
import com.dev.user_transaction_management_system.domain.user.User;
import com.dev.user_transaction_management_system.infrastructure.persistence.model.BankAccountEntity;
import com.dev.user_transaction_management_system.use_case.open_bank_account.AccountOpenedResponse;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

import static java.lang.String.format;

@EqualsAndHashCode
@ToString
public class BankAccount {

    private static final int MINIMUM_BALANCE = 100;
    public static final String MINIMUM_BALANCE_MESSAGE =
            format("a bank account can't be opened unless the user deposits at least {%s}",MINIMUM_BALANCE);

    private final AccountId accountId;
    private final AccountNumber accountNumber;
    private final User user;
    private Amount balance;
    private final Date createdAt;
    private AccountStatus status;
    private final List<NotifiableEvent> events;

    private BankAccount(AccountId accountId,
                        AccountNumber accountNumber,
                        User user, Amount balance,
                        Date createdAt,
                        AccountStatus status) {

        Assert.notNull(accountId, "account id cannot be null");
        Assert.notNull(accountNumber, "account number cannot be null");
        Assert.notNull(user, "user cannot be null");
        Assert.notNull(balance, "balance cannot be null");
        Assert.notNull(createdAt, "created at cannot be null");

        ensureUserHasMinimumBalance(balance);

        this.accountId = accountId;
        this.accountNumber = accountNumber;
        this.user = user;
        this.balance = balance;
        this.createdAt = createdAt;
        this.status = status;
        this.events = new ArrayList<>();
        this.events.add(
                new BankAccountWasOpened(user.fullName(), accountNumber.asString(), user.email().asString(), user.phoneNumber())
        );
    }

    private void ensureUserHasMinimumBalance(Amount balance) {
        if (!hasMinimumBalance(balance))
            throw new IllegalArgumentException(MINIMUM_BALANCE_MESSAGE);
    }

    private boolean hasMinimumBalance(Amount balance) {
        return balance.asDouble() >= MINIMUM_BALANCE;
    }

    public static BankAccount open(AccountId accountId,
                                   AccountNumber accountNumber,
                                   User user,
                                   Amount balance,
                                   AccountStatus status,
                                   Date createdAt) {
        return new BankAccount(accountId, accountNumber, user, balance, createdAt, status);
    }

    public void increaseAmount(Amount amount) {
        if (isAccountDisable())
            throw CouldNotProcessTransaction.withDisabledAccount();

        final double increaseValue = this.balance.asDouble() + amount.asDouble();
        this.balance = Amount.of(increaseValue);
        this.events.add(fundsDeposited(amount.asDouble()));
    }

    private FundsWereDeposited fundsDeposited(double increaseValue) {
        return new FundsWereDeposited(increaseValue,
                accountNumber.last4Ending(),
                user.email().asString(),
                user.phoneNumber(),
                balance.asDouble(),
                createdAt.asString());
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

    private FundsWereWithdrawn fundsWithdrawn(Amount amount) {
        return new FundsWereWithdrawn(amount.asDouble(),
                accountNumber.last4Ending(),
                user.email().asString(),
                user.phoneNumber(),
                createdAt.asString(), balance.asDouble());
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
        this.events.add(new BankAccountWasActivated(user.fullName(), accountNumber.asString(), user.email().asString()));
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
        bankAccountEntity.setCreatedAt(createdAt.asLocalDateTime());
        return bankAccountEntity;
    }

    public AccountOpenedResponse toResponse(String fullName) {
        return new AccountOpenedResponse(accountNumber.toString(), fullName, balance.asDouble(), createdAt.asString(), status);
    }

    public List<NotifiableEvent> releaseEvents() {
        return events;
    }
}
