package com.dev.user_transaction_management_system.domain.transaction;

import com.dev.user_transaction_management_system.infrastructure.persistence.model.AccountEntity;
import com.dev.user_transaction_management_system.infrastructure.persistence.model.TransactionEntity;

import java.util.Optional;

public interface TransactionRepository {
    void save(TransactionEntity transactionEntity);

    Optional<AccountEntity> findAccountById(Integer accountId);
}
