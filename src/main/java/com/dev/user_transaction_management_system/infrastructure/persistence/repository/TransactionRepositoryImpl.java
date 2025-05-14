package com.dev.user_transaction_management_system.infrastructure.persistence.repository;

import com.dev.user_transaction_management_system.domain.transaction.TransactionRepository;
import com.dev.user_transaction_management_system.infrastructure.persistence.model.TransactionEntity;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Random;

@Repository
public class TransactionRepositoryImpl implements TransactionRepository {

    private final EntityManager entityManager;
    private final Random random;

    public TransactionRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.random = new Random();
    }

    @Override
    public void save(TransactionEntity transactionEntity) {
        entityManager.merge(transactionEntity);
    }

    @Override
    public Optional<TransactionEntity> findById(Integer transactionId) {
        final TransactionEntity transaction = entityManager
                .createQuery("FROM TransactionEntity WHERE transactionId=:transactionId", TransactionEntity.class)
                .setParameter("transactionId", transactionId)
                .getSingleResult();

        return Optional.ofNullable(transaction);
    }

    @Override
    public String generateReferenceNumber() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < 16; i++) {
            stringBuilder.append(random.nextInt(0,10));
        }
        return stringBuilder.toString();
    }
}
