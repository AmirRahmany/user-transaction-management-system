package com.dev.user_transaction_management_system.fake;

import com.dev.user_transaction_management_system.domain.account.AccountNumber;
import com.dev.user_transaction_management_system.infrastructure.persistence.model.AccountEntity;
import com.dev.user_transaction_management_system.domain.account.BankAccountRepository;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class BankAccountRepositoryFake implements BankAccountRepository {

    private final List<AccountEntity> records = new LinkedList<>();

    @Override
    public void save(AccountEntity accountEntity) {
        if (accountEntity == null)
            throw new IllegalArgumentException("anAccount must not be empty");
        records.add(accountEntity);
    }

    @Override
    public Optional<AccountEntity> findByAccountNumber(AccountNumber accountNumber) {
        return records.stream()
                .filter(account -> account.hasSameAccountNumber(accountNumber.toString())).findFirst();
    }

    @Override
    public boolean accountExists(AccountNumber accountNumber) {
        return records.stream().anyMatch(accountEntity -> accountEntity.getAccountNumber().equals(accountNumber.toString()));
    }
}
