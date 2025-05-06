package com.dev.user_transaction_management_system.fake;

import com.dev.user_transaction_management_system.domain.transaction.AccountNumber;
import com.dev.user_transaction_management_system.domain.transaction.Amount;
import com.dev.user_transaction_management_system.model.AccountEntity;
import com.dev.user_transaction_management_system.repository.AccountRepository;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class AccountRepositoryFake implements AccountRepository {

    private final List<AccountEntity> records = new LinkedList<>();

    @Override
    public void save(AccountEntity accountEntity) {
        if (accountEntity == null)
            throw new IllegalArgumentException("account must not be empty");
        records.add(accountEntity);
    }

    @Override
    public Optional<AccountEntity> findByAccountNumber(AccountNumber accountNumber) {
        return records.stream()
                .filter(account -> account.hasSameAccountNumber(accountNumber.toString())).findFirst();
    }

    @Override
    public void increaseBalance(AccountNumber accountNumber, Amount amount) {

    }

    @Override
    public void decreaseBalance(AccountNumber accountNumber, Amount amount) {

    }

    @Override
    public boolean accountNumberExists(AccountNumber accountNumber) {
        return records.stream().anyMatch(accountEntity -> accountEntity.getAccountNumber().equals(accountNumber.toString()));
    }
}
