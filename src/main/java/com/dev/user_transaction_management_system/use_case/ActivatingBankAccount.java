package com.dev.user_transaction_management_system.use_case;

import com.dev.user_transaction_management_system.domain.account.AccountNumber;
import com.dev.user_transaction_management_system.domain.account.BankAccount;
import com.dev.user_transaction_management_system.domain.account.BankAccountRepository;
import com.dev.user_transaction_management_system.domain.exceptions.CouldNotFindBankAccount;
import com.dev.user_transaction_management_system.infrastructure.persistence.model.BankAccountEntity;
import com.dev.user_transaction_management_system.infrastructure.util.BankAccountMapper;
import org.springframework.stereotype.Service;

@Service
public class ActivatingBankAccount {

    private final BankAccountRepository repository;
    private final BankAccountMapper bankAccountMapper;

    public ActivatingBankAccount(BankAccountRepository repository) {
        this.repository = repository;
        this.bankAccountMapper = new BankAccountMapper();
    }

    public void activate(String accountNumber) {
        final AccountNumber bankAccountNumber = AccountNumber.of(accountNumber);
        final BankAccountEntity entity = repository.findByAccountNumber(bankAccountNumber)
                .orElseThrow(() -> CouldNotFindBankAccount.withAccountNumber(accountNumber));
        BankAccount bankAccount = bankAccountMapper.toDomain(entity);


        bankAccount.enable();
        repository.save(bankAccount.toEntity());
    }
}
