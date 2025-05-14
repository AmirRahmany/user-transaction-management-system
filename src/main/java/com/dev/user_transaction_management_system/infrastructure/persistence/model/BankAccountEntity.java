package com.dev.user_transaction_management_system.infrastructure.persistence.model;

import com.dev.user_transaction_management_system.domain.bank_account.AccountId;
import com.dev.user_transaction_management_system.domain.bank_account.AccountStatus;
import com.dev.user_transaction_management_system.domain.transaction.Amount;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "account")
@NoArgsConstructor
@Data
public class BankAccountEntity {

    @Id
    @Column(name = "account_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer accountId;

    @Column(name = "account_number")
    private String accountNumber;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "balance")
    private Double balance;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Enumerated(value = EnumType.STRING)
    private AccountStatus status;

    public BankAccountEntity(String accountNumber, String userId, Double balance,AccountStatus accountStatus) {
        this.accountNumber = accountNumber;
        this.userId = userId;
        this.balance = balance;
        this.createdAt = LocalDateTime.now();
        this.status = accountStatus;
    }

    public static BankAccountEntity openWith(String accountNumber, String userId, Double balance, AccountStatus accountStatus) {
        return new BankAccountEntity(accountNumber, userId, balance,accountStatus);
    }

    public boolean hasSameAccountId(Integer accountId) {
        return this.accountId.equals(accountId);
    }

    public boolean hasSameAccountNumber(String accountNumber) {
        return this.accountNumber.equals(accountNumber);
    }
}
