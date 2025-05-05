package com.dev.user_transaction_management_system.domain.transaction;

import com.dev.user_transaction_management_system.model.AccountEntity;

import java.time.LocalDateTime;
import java.util.Objects;

public class Account {
    public static final int MINIMUM_BALANCE = 100;

    private final Integer accountId;
    private final AccountNumber accountNumber;
    private final Integer userId;
    private Amount balance;
    private final LocalDateTime createdAt;
    private final AccountStatus status;

    private Account(Integer accountId, AccountNumber accountNumber, Integer userId, Amount balance, LocalDateTime createdAt) {
        if (!hasMinimumBalance(balance))
            throw new IllegalArgumentException("an account can't be opened unless the user deposits at least $100");

        if (!isAssociateToAnyUser(userId)){
            throw new IllegalArgumentException("each account should be associate to a user");
        }

        this.accountId = accountId;
        this.accountNumber = accountNumber;
        this.userId = userId;
        this.balance = balance;
        this.createdAt = createdAt;
        this.status = AccountStatus.DISABLE;
    }

    public static Account open(Integer accountId, AccountNumber accountNumber, Integer userId, Amount balance, LocalDateTime createdAt) {
        return new Account(accountId, accountNumber, userId, balance, createdAt);
    }

    public boolean isBalanceSufficient(Amount amount) {
        return balance.toValue() >= amount.toValue();
    }

    public boolean isBalanceInsufficient(Amount amount) {
        return !isBalanceSufficient(amount);
    }

    public double amount() {
        return balance.toValue();
    }

    public Integer accountId() {
        return accountId;
    }

    public AccountEntity toEntity() {
        return AccountEntity.openWith(
                accountId,
                accountNumber.toString(),
                userId,
                balance
        );
    }

    public void increaseAmount(Amount amount) {
        final double decreasedValue = this.balance.toValue() + amount.toValue();
        this.balance = Amount.of(decreasedValue);
    }

    public void decreaseBalance(Amount amount) {
        final double decreasedValue = this.balance.toValue() - amount.toValue();
        this.balance = Amount.of(decreasedValue);
    }

    public boolean isTheSameAccountWith(Account to) {
        return accountId.equals(to.accountId);
    }

    private boolean hasMinimumBalance(Amount balance) {
        return balance.toValue() >= MINIMUM_BALANCE;
    }

    private boolean isAssociateToAnyUser(Integer userId) {
        return userId != null;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return Objects.equals(accountId, account.accountId) &&
                Objects.equals(accountNumber, account.accountNumber) &&
                Objects.equals(userId, account.userId) && Objects.equals(balance, account.balance) &&
                Objects.equals(createdAt, account.createdAt) && status == account.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountId, accountNumber, userId, balance, createdAt, status);
    }

    @Override
    public String toString() {
        return "Account{" +
                "accountId='" + accountId + '\'' +
                ", accountNumber='" + accountNumber + '\'' +
                ", userId='" + userId + '\'' +
                ", balance=" + balance +
                ", createdAt=" + createdAt +
                ", status=" + status +
                '}';
    }

}
