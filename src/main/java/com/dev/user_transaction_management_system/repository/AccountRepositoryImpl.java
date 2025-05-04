package com.dev.user_transaction_management_system.repository;

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
    public Optional<AccountEntity> findById(Integer accountId) {
        final AccountEntity accountEntity = entityManager.find(AccountEntity.class, accountId);
        return Optional.ofNullable(accountEntity);
    }

    @Override
    public void increaseBalance(Integer accountId, Amount amount) {
        final AccountEntity account = getAccountBy(accountId);

        account.setBalance(account.getBalance() + amount.toValue());

        entityManager.merge(account);
    }

    @Override
    public void decreaseBalance(Integer accountId, Amount amount) {
        final AccountEntity account = getAccountBy(accountId);

        account.setBalance(account.getBalance() - amount.toValue());
        entityManager.merge(account);
    }

    private AccountEntity getAccountBy(Integer accountId) {
        final Optional<AccountEntity> account = findById(accountId);

        return account.orElseThrow(() -> CouldNotFindAccount.withId(accountId));
    }
}
