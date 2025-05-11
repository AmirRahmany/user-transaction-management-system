package com.dev.user_transaction_management_system.infrastructure.persistence.repository;

import com.dev.user_transaction_management_system.domain.account.AccountNumber;
import com.dev.user_transaction_management_system.domain.account.BankAccountRepository;
import com.dev.user_transaction_management_system.domain.exceptions.CouldNotFindBankAccount;
import com.dev.user_transaction_management_system.infrastructure.persistence.model.BankAccountEntity;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class BankAccountRepositoryImpl implements BankAccountRepository {

    private final EntityManager entityManager;

    public BankAccountRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
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

    private BankAccountEntity getAccountBy(AccountNumber accountNumber) {
        final Optional<BankAccountEntity> account = findByAccountNumber(accountNumber);

        return account.orElseThrow(() -> CouldNotFindBankAccount.withAccountNumber(accountNumber.toString()));
    }
}
