package com.dev.user_transaction_management_system.application;

import com.dev.user_transaction_management_system.domain.transaction.Account;
import com.dev.user_transaction_management_system.domain.transaction.AccountNumber;
import com.dev.user_transaction_management_system.domain.transaction.Amount;
import com.dev.user_transaction_management_system.dto.AccountRequest;
import com.dev.user_transaction_management_system.repository.AccountRepository;
import com.dev.user_transaction_management_system.util.IAccountNumberGenerator;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;

public class AccountOpening {


    private final AccountRepository accountRepository;
    private final IAccountNumberGenerator accountNumberGenerator;

    public AccountOpening(AccountRepository accountRepository, IAccountNumberGenerator accountNumberGenerator) {
        this.accountRepository = accountRepository;
        this.accountNumberGenerator = accountNumberGenerator;
    }

    @Transactional
    public void open(AccountRequest accountRequest) {
        final AccountNumber accountNumber = accountNumberGenerator.generate();
        if (accountRepository.accountNumberExists(accountNumber))
            throw new IllegalArgumentException("Account Number must be unique");

        final double balance = accountRequest.balance();
        final int new_account = 0;
        final Account account = Account.open(
                new_account,
                accountNumber,
                accountRequest.userId(),
                Amount.of(balance),
                LocalDateTime.now()
        );

        accountRepository.save(account.toEntity());
    }

}
