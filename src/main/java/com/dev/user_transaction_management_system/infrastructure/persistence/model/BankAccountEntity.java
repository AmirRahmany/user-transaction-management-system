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
    private String accountId;

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

    public boolean hasSameAccountNumber(String accountNumber) {
        return this.accountNumber.equals(accountNumber);
    }
}
