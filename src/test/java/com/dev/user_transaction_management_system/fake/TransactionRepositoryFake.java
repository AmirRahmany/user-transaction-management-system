package com.dev.user_transaction_management_system.fake;

import com.dev.user_transaction_management_system.domain.transaction.TransactionRepository;
import com.dev.user_transaction_management_system.infrastructure.persistence.model.TransactionEntity;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

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

    @Override
    public String generateReferenceNumber() {
        final Random random = new Random();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < 16; i++) {
            stringBuilder.append(random.nextInt(0,10));
        }
        return stringBuilder.toString();
    }
}
