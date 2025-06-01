package com.dev.user_transaction_management_system.domain.transaction;

import com.dev.user_transaction_management_system.domain.bank_account.AccountNumber;
import com.dev.user_transaction_management_system.infrastructure.persistence.model.TransactionEntity;

import java.util.List;

public interface TransactionRepository {
    void save(TransactionEntity transactionEntity);

    ReferenceNumber generateReferenceNumber();

    List<TransactionEntity> findByAccountNumber(AccountNumber bankAccountNumber);
}
