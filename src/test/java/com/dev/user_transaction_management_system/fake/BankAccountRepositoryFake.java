package com.dev.user_transaction_management_system.fake;

import com.dev.user_transaction_management_system.domain.bank_account.AccountNumber;
import com.dev.user_transaction_management_system.infrastructure.persistence.model.BankAccountEntity;
import com.dev.user_transaction_management_system.domain.bank_account.BankAccountRepository;
import io.jsonwebtoken.lang.Assert;

import java.util.*;

public class BankAccountRepositoryFake implements BankAccountRepository {

    private final Map<String, BankAccountEntity> records = new LinkedHashMap<>();

    private final String accountId = UUID.randomUUID().toString();

    @Override
    public void save(BankAccountEntity bankAccountEntity) {
        Assert.notNull(bankAccountEntity, "anAccount must not be empty");
        if (bankAccountEntity.getAccountId() == null) {
            bankAccountEntity.setAccountId(accountId);
        }
        records.put(bankAccountEntity.getAccountId(), bankAccountEntity);
    }

    @Override
    public Optional<BankAccountEntity> findByAccountNumber(AccountNumber accountNumber) {
        return records.values().stream()
                .filter(account -> account.hasSameAccountNumber(accountNumber.toString())).findFirst();
    }

    @Override
    public boolean accountExists(AccountNumber accountNumber) {
        return records.values().stream().anyMatch(accountEntity -> accountEntity.getAccountNumber().equals(accountNumber.toString()));
    }
}
