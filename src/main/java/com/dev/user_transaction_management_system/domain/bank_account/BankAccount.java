package com.dev.user_transaction_management_system.domain.bank_account;

import com.dev.user_transaction_management_system.domain.exceptions.CouldNotProcessTransaction;
import com.dev.user_transaction_management_system.domain.transaction.Amount;
import com.dev.user_transaction_management_system.domain.user.UserId;
import com.dev.user_transaction_management_system.infrastructure.persistence.model.BankAccountEntity;
import com.dev.user_transaction_management_system.use_case.dto.OpeningAccountResponse;
import org.springframework.util.Assert;

import java.time.LocalDateTime;
import java.util.Objects;

public class BankAccount {

    private static final int MINIMUM_BALANCE = 100;

    private final AccountId accountId;
    private final AccountNumber accountNumber;
    private final UserId userId;
    private Amount balance;
    private final LocalDateTime createdAt;
    private AccountStatus status;

    private BankAccount(AccountId accountId,
                        AccountNumber accountNumber,
                        UserId userId, Amount balance,
                        LocalDateTime createdAt) {

        Assert.notNull(accountId,"account id cannot be null");
        Assert.notNull(accountNumber,"account number cannot be null");
        Assert.notNull(userId,"user id cannot be null");
        Assert.notNull(balance,"balance cannot be null");
        Assert.notNull(createdAt,"created at cannot be null");

        if (!hasMinimumBalance(balance))
            throw new IllegalArgumentException("a bank account can't be opened unless the user deposits at least $100");

        this.accountId = accountId;
        this.accountNumber = accountNumber;
        this.userId = userId;
        this.balance = balance;
        this.createdAt = createdAt;
        this.status = AccountStatus.DISABLE;
    }

    public static BankAccount open(AccountId accountId,
                                   AccountNumber accountNumber,
                                   UserId userId,
                                   Amount balance,
                                   LocalDateTime createdAt) {
        return new BankAccount(accountId, accountNumber, userId, balance, createdAt);
    }

    public void increaseAmount(Amount amount) {
        final double decreasedValue = this.balance.toValue() + amount.toValue();
        this.balance = Amount.of(decreasedValue);
    }

    public void decreaseBalance(Amount amount) {
        Assert.notNull(amount,"amount cannot be null");
        ensureSufficientBalanceFor(amount);

        final double decreasedValue = this.balance.toValue() - amount.toValue();
        this.balance = Amount.of(decreasedValue);
    }

    private void ensureSufficientBalanceFor(Amount amount) {
        if (!isBalanceSufficient(amount)) {
            throw CouldNotProcessTransaction.becauseInsufficientBalance();
        }
    }

    private boolean isBalanceSufficient(Amount amount) {
        return balance.toValue() >= amount.toValue();
    }

    public void enable() {
        this.status = AccountStatus.ENABLE;
    }

    private boolean hasMinimumBalance(Amount balance) {
        return balance.toValue() >= MINIMUM_BALANCE;
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
        bankAccountEntity.setUserId(userId.asString());
        bankAccountEntity.setBalance(balance.toValue());
        bankAccountEntity.setStatus(status);
        bankAccountEntity.setCreatedAt(createdAt);
        return bankAccountEntity;
    }

    public OpeningAccountResponse toResponse(String fullName) {
        return new OpeningAccountResponse(accountNumber.toString(), fullName, balance.toValue(), createdAt, status);
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BankAccount account = (BankAccount) o;
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
        return "BankAccount{" +
                "accountId='" + accountId + '\'' +
                ", accountNumber='" + accountNumber + '\'' +
                ", userId='" + userId + '\'' +
                ", balance=" + balance +
                ", createdAt=" + createdAt +
                ", status=" + status +
                '}';
    }
}
