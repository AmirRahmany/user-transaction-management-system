package com.dev.user_transaction_management_system.repository;

import com.dev.user_transaction_management_system.domain.transaction.AccountNumber;
import com.dev.user_transaction_management_system.domain.transaction.Amount;
import com.dev.user_transaction_management_system.model.AccountEntity;

import java.util.Optional;

public interface AccountRepository {

    void save(AccountEntity accountEntity);

    Optional<AccountEntity> findByAccountNumber(AccountNumber accountNumber);

    void increaseBalance(AccountNumber accountNumber, Amount amount) throws IllegalAccessException;

    void decreaseBalance(AccountNumber accountNumber, Amount amount);

    boolean accountNumberExists(AccountNumber accountNumber);
}
