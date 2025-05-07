package com.dev.user_transaction_management_system.use_case;

import com.dev.user_transaction_management_system.domain.account.Account;
import com.dev.user_transaction_management_system.domain.account.AccountNumber;
import com.dev.user_transaction_management_system.domain.transaction.Amount;
import com.dev.user_transaction_management_system.use_case.dto.AccountRequest;
import com.dev.user_transaction_management_system.use_case.dto.AccountResponse;
import com.dev.user_transaction_management_system.domain.exceptions.CouldNotFoundUser;
import com.dev.user_transaction_management_system.infrastructure.persistence.model.UserEntity;
import com.dev.user_transaction_management_system.domain.account.AccountRepository;
import com.dev.user_transaction_management_system.domain.user.UserRepository;
import com.dev.user_transaction_management_system.domain.account.IAccountNumberGenerator;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static java.time.LocalDateTime.now;

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
        final AccountNumber accountNumber = generateAccountNumber();

        final Account account = openAccount(accountRequest, accountNumber);

        accountRepository.save(account.toEntity());
        UserEntity userEntity = userRepository.findById(accountRequest.userId())
                .orElseThrow(() -> CouldNotFoundUser.withId(accountRequest.userId()));

        return account.toResponse(userEntity.fullName());
    }

    private AccountNumber generateAccountNumber() {
        final AccountNumber accountNumber = accountNumberGenerator.generate();
        if (accountRepository.accountNumberExists(accountNumber)) {
            throw new IllegalArgumentException("Account Number must be unique");
        }
        return accountNumber;
    }

    private static Account openAccount(AccountRequest accountRequest, AccountNumber accountNumber) {
        final double balance = accountRequest.balance();
        final int new_account = 0;
        final LocalDateTime createdAt = now();
        return Account.open(new_account, accountNumber, accountRequest.userId(), Amount.of(balance), createdAt);
    }

}
