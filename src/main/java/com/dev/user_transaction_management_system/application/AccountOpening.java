package com.dev.user_transaction_management_system.application;

import com.dev.user_transaction_management_system.domain.transaction.Account;
import com.dev.user_transaction_management_system.domain.transaction.AccountNumber;
import com.dev.user_transaction_management_system.domain.transaction.Amount;
import com.dev.user_transaction_management_system.dto.AccountRequest;
import com.dev.user_transaction_management_system.dto.AccountResponse;
import com.dev.user_transaction_management_system.exceptions.CouldNotFoundUser;
import com.dev.user_transaction_management_system.model.UserEntity;
import com.dev.user_transaction_management_system.repository.AccountRepository;
import com.dev.user_transaction_management_system.repository.UserRepository;
import com.dev.user_transaction_management_system.util.IAccountNumberGenerator;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AccountOpening {

    private final AccountRepository accountRepository;
    private final IAccountNumberGenerator accountNumberGenerator;
    private final UserRepository userRepository;

    public AccountOpening(AccountRepository accountRepository,
                          IAccountNumberGenerator accountNumberGenerator,
                          UserRepository userRepository) {
        this.accountRepository = accountRepository;
        this.accountNumberGenerator = accountNumberGenerator;
        this.userRepository = userRepository;
    }

    @Transactional
    public AccountResponse open(AccountRequest accountRequest) {
        final AccountNumber accountNumber = accountNumberGenerator.generate();
        if (accountRepository.accountNumberExists(accountNumber))
            throw new IllegalArgumentException("Account Number must be unique");

        final Account account = openAccount(accountRequest, accountNumber);

        accountRepository.save(account.toEntity());
        final Optional<UserEntity> userEntity = userRepository.findById(accountRequest.userId());
        if (userEntity.isEmpty()) throw CouldNotFoundUser.withId(accountRequest.userId());

        return account.toResponse(userEntity.get().fullName());
    }

    private static Account openAccount(AccountRequest accountRequest, AccountNumber accountNumber) {
        final double balance = accountRequest.balance();
        final int new_account = 0;
        final LocalDateTime createdAt = LocalDateTime.now();
        return Account.open(
                new_account,
                accountNumber,
                accountRequest.userId(),
                Amount.of(balance),
                createdAt
        );
    }

}
