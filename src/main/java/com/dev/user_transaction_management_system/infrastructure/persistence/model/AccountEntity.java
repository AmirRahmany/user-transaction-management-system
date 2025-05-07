package com.dev.user_transaction_management_system.infrastructure.persistence.model;

import com.dev.user_transaction_management_system.domain.account.AccountStatus;
import com.dev.user_transaction_management_system.domain.transaction.Amount;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "account")
@NoArgsConstructor
@Data
public class AccountEntity {

    @Id
    @Column(name = "account_id")
    private Integer accountId;

    @Column(name = "account_number")
    private String accountNumber;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "balance")
    private Double balance;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Enumerated(value = EnumType.STRING)
    private AccountStatus status;

    public AccountEntity(Integer accountId, String accountNumber, Integer userId, Amount balance) {
        this.accountId = accountId;
        this.accountNumber = accountNumber;
        this.userId = userId;
        this.balance = balance.toValue();
        this.createdAt = LocalDateTime.now();
        this.status = AccountStatus.DISABLE;
    }

    public static AccountEntity openWith(Integer accountId, String accountNumber, Integer userId, Amount balance) {
        return new AccountEntity(accountId, accountNumber, userId, balance);
    }

    public boolean hasSameAccountId(Integer accountId) {
        return this.accountId.equals(accountId);
    }

    public boolean hasSameAccountNumber(String accountNumber) {
        return this.accountNumber.equals(accountNumber);
    }
}
