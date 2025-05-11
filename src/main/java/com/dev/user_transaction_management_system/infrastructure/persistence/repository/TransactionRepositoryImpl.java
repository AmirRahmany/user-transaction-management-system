package com.dev.user_transaction_management_system.infrastructure.persistence.repository;

import com.dev.user_transaction_management_system.domain.transaction.TransactionRepository;
import com.dev.user_transaction_management_system.infrastructure.persistence.model.TransactionEntity;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class TransactionRepositoryImpl implements TransactionRepository {

    private final EntityManager entityManager;

    public TransactionRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
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
}
