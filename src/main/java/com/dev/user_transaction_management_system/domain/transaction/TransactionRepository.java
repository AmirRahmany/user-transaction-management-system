package com.dev.user_transaction_management_system.domain.transaction;

import com.dev.user_transaction_management_system.domain.bank_account.AccountNumber;
import com.dev.user_transaction_management_system.infrastructure.persistence.model.TransactionEntity;

import java.util.List;
import java.util.Optional;

public interface TransactionRepository {
    void save(TransactionEntity transactionEntity);

    Optional<TransactionEntity> findById(Integer transactionId);

    String generateReferenceNumber();

    List<TransactionEntity> findByAccountNumber(AccountNumber bankAccountNumber);
}
