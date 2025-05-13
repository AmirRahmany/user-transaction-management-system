package com.dev.user_transaction_management_system.domain.bank_account;

import com.dev.user_transaction_management_system.infrastructure.persistence.model.BankAccountEntity;

import java.util.Optional;

public interface BankAccountRepository {

    void save(BankAccountEntity bankAccountEntity);

    Optional<BankAccountEntity> findByAccountNumber(AccountNumber accountNumber);

    boolean accountExists(AccountNumber accountNumber);
}
