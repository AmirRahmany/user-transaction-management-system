package com.dev.user_transaction_management_system.infrastructure.persistence.repository;

import com.dev.user_transaction_management_system.domain.bank_account.AccountNumber;
import com.dev.user_transaction_management_system.domain.bank_account.BankAccountRepository;
import com.dev.user_transaction_management_system.infrastructure.persistence.model.BankAccountEntity;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class BankAccountRepositoryImpl implements BankAccountRepository {

    private final EntityManager entityManager;
    private final UUIDIdentifierGenerator identifierGenerator;

    public BankAccountRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.identifierGenerator = new UUIDIdentifierGenerator();
    }

    @Override
    public void save(BankAccountEntity bankAccountEntity) {
        entityManager.merge(bankAccountEntity);
    }

    @Override
    public Optional<BankAccountEntity> findByAccountNumber(AccountNumber accountNumber) {
        final String sql = "FROM BankAccountEntity WHERE accountNumber=:accountNumber";

        final BankAccountEntity bankAccountEntity = entityManager.createQuery(sql, BankAccountEntity.class)
                .setParameter("accountNumber",accountNumber.toString())
                .getSingleResult();

        return Optional.ofNullable(bankAccountEntity);
    }

    @Override
    public boolean accountExists(AccountNumber accountNumber) {
        final int isExist = entityManager.createNativeQuery("SELECT EXISTS(SELECT 1 FROM account where accountNumber=:accountNumber)")
                .setParameter("accountNumber", accountNumber)
                .getFirstResult();
        return isExist == 1;

    }

    @Override
    public UUID nextIdentify() {
        return identifierGenerator.generate();
    }

}
