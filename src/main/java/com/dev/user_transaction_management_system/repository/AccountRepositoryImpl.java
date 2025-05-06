package com.dev.user_transaction_management_system.repository;

import com.dev.user_transaction_management_system.domain.transaction.AccountNumber;
import com.dev.user_transaction_management_system.domain.transaction.Amount;
import com.dev.user_transaction_management_system.exceptions.CouldNotFindAccount;
import com.dev.user_transaction_management_system.model.AccountEntity;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class AccountRepositoryImpl implements AccountRepository {

    private final EntityManager entityManager;

    public AccountRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void save(AccountEntity accountEntity) {
        entityManager.merge(accountEntity);
    }

    @Override
    public Optional<AccountEntity> findByAccountNumber(AccountNumber accountNumber) {
        final String sql = "FROM AccountEntity WHERE accountNumber=:accountNumber";

        final AccountEntity accountEntity = entityManager.createQuery(sql,AccountEntity.class)
                .setParameter("accountNumber",accountNumber.toString())
                .getSingleResult();

        return Optional.ofNullable(accountEntity);
    }

    @Override
    public void increaseBalance(AccountNumber accountNumber, Amount amount) {
        final AccountEntity account = getAccountBy(accountNumber);

        account.setBalance(account.getBalance() + amount.toValue());

        entityManager.merge(account);
    }

    @Override
    public void decreaseBalance(AccountNumber accountNumber, Amount amount) {
        final AccountEntity account = getAccountBy(accountNumber);

        account.setBalance(account.getBalance() - amount.toValue());
        entityManager.merge(account);
    }

    @Override
    public boolean accountNumberExists(AccountNumber accountNumber) {
        final int isExist = entityManager.createNativeQuery("SELECT EXISTS(SELECT 1 FROM account where accountNumber=:accountNumber)")
                .setParameter("accountNumber", accountNumber)
                .getFirstResult();
        return isExist == 1;

    }

    private AccountEntity getAccountBy(AccountNumber accountNumber) {
        final Optional<AccountEntity> account = findByAccountNumber(accountNumber);

        return account.orElseThrow(() -> CouldNotFindAccount.withId(accountNumber.toString()));
    }
}
