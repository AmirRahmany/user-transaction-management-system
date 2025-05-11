package com.dev.user_transaction_management_system.fake;

import com.dev.user_transaction_management_system.domain.transaction.TransactionRepository;
import com.dev.user_transaction_management_system.infrastructure.persistence.model.TransactionEntity;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class TransactionRepositoryFake implements TransactionRepository {

    private final List<TransactionEntity> transactions = new LinkedList<>();

    @Override
    public void save(TransactionEntity transactionEntity) {
        transactions.add(transactionEntity);
    }

    @Override
    public Optional<TransactionEntity> findById(Integer transactionId) {
        return transactions.stream().filter(transactionEntity -> transactionEntity.getTransactionId().equals(transactionId)).findFirst();
    }
}
