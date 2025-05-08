package com.dev.user_transaction_management_system.domain.account;

import com.dev.user_transaction_management_system.infrastructure.persistence.model.AccountEntity;

import java.util.Optional;

public interface AccountRepository {

    void save(AccountEntity accountEntity);

    Optional<AccountEntity> findByAccountNumber(AccountNumber accountNumber);

    boolean accountExists(AccountNumber accountNumber);
}
