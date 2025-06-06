package com.dev.user_transaction_management_system.infrastructure.persistence.repository;

import com.dev.user_transaction_management_system.domain.bank_account.AccountNumber;
import com.dev.user_transaction_management_system.domain.transaction.ReferenceNumber;
import com.dev.user_transaction_management_system.domain.transaction.TransactionRepository;
import com.dev.user_transaction_management_system.infrastructure.persistence.model.TransactionEntity;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Random;

@Repository
public class TransactionRepositoryWithJpa implements TransactionRepository {

    private final EntityManager entityManager;
    private final Random random;

    public TransactionRepositoryWithJpa(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.random = new Random();
    }

    @Override
    public void save(TransactionEntity transactionEntity) {
        entityManager.merge(transactionEntity);
    }

    @Override
    public ReferenceNumber generateReferenceNumber() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < 16; i++) {
            stringBuilder.append(random.nextInt(0,10));
        }
        return ReferenceNumber.fromString(stringBuilder.toString());
    }

    @Override
    public List<TransactionEntity> findByAccountNumber(AccountNumber bankAccountNumber) {
        final String sql = "FROM TransactionEntity WHERE accountNumber=:accountNumber";
        return entityManager.createQuery(sql,TransactionEntity.class)
                .setParameter("accountNumber", bankAccountNumber.toString())
                .getResultList();
    }
}
