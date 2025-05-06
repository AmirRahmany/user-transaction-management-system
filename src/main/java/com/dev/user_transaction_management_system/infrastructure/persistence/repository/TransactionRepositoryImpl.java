package com.dev.user_transaction_management_system.infrastructure.persistence.repository;

import com.dev.user_transaction_management_system.domain.transaction.TransactionRepository;
import com.dev.user_transaction_management_system.infrastructure.persistence.model.AccountEntity;
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
    public Optional<AccountEntity> findAccountById(Integer accountId) {
        final AccountEntity account = entityManager
                .createQuery("FROM AccountEntity WHERE accountId=:accountId", AccountEntity.class)
                .setParameter("accountId", accountId)
                .getSingleResult();

        return Optional.ofNullable(account);
    }
}
