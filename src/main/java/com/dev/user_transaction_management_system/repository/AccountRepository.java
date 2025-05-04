package com.dev.user_transaction_management_system.repository;

import com.dev.user_transaction_management_system.domain.transaction.Amount;
import com.dev.user_transaction_management_system.model.AccountEntity;

import java.util.Optional;

public interface AccountRepository {

    void save(AccountEntity accountEntity);

    Optional<AccountEntity> findById(Integer accountId);

    void increaseBalance(Integer accountId, Amount amount) throws IllegalAccessException;

    void decreaseBalance(Integer accountId, Amount amount);
}
