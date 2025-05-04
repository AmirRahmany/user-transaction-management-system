package com.dev.user_transaction_management_system.repository;

import com.dev.user_transaction_management_system.domain.transaction.Account;
import com.dev.user_transaction_management_system.model.AccountEntity;
import com.dev.user_transaction_management_system.model.TransactionEntity;

import java.util.Optional;

public interface TransactionRepository {
    void save(TransactionEntity transactionEntity);

    Optional<AccountEntity> findAccountById(Integer accountId);
}
