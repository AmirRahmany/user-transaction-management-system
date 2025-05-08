package com.dev.user_transaction_management_system.use_case;

import com.dev.user_transaction_management_system.domain.account.*;
import com.dev.user_transaction_management_system.domain.exceptions.CouldNotOpenAnAccount;
import com.dev.user_transaction_management_system.domain.transaction.Amount;
import com.dev.user_transaction_management_system.domain.user.User;
import com.dev.user_transaction_management_system.domain.user.UserId;
import com.dev.user_transaction_management_system.infrastructure.util.UserMapper;
import com.dev.user_transaction_management_system.use_case.dto.AccountRequest;
import com.dev.user_transaction_management_system.use_case.dto.AccountResponse;
import com.dev.user_transaction_management_system.domain.exceptions.CouldNotFoundUser;
import com.dev.user_transaction_management_system.infrastructure.persistence.model.UserEntity;
import com.dev.user_transaction_management_system.domain.user.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static java.time.LocalDateTime.now;

@Service
public class OpeningAccount {

    private final AccountRepository accountRepository;
    private final IAccountNumberGenerator accountNumberGenerator;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public OpeningAccount(AccountRepository accountRepository,
                          IAccountNumberGenerator accountNumberGenerator,
                          UserRepository userRepository) {
        this.accountRepository = accountRepository;
        this.accountNumberGenerator = accountNumberGenerator;
        this.userRepository = userRepository;
        this.userMapper = new UserMapper();
    }

    @Transactional
    public AccountResponse open(AccountRequest accountRequest) {
        final User user = findUserBy(accountRequest.userId());
        ensureUserIsEnabled(user);

        final AccountNumber accountNumber = generateAccountNumber();
        final Account account = openAccount(accountRequest, accountNumber);

        accountRepository.save(account.toEntity());
        return account.toResponse(user.fullName());
    }

    private User findUserBy(Integer userId) {
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> CouldNotFoundUser.withId(userId));

        return userMapper.toDomain(userEntity);
    }

    private void ensureUserIsEnabled(User user) {
        if (user.isDisable())
            throw CouldNotOpenAnAccount.withDisableUser();
    }

    private AccountNumber generateAccountNumber() {
        final AccountNumber accountNumber = accountNumberGenerator.generate();
        if (accountRepository.accountExists(accountNumber)) {
            throw new IllegalArgumentException("Account Number must be unique");
        }
        return accountNumber;
    }

    private static Account openAccount(AccountRequest accountRequest, AccountNumber accountNumber) {
        final double balance = accountRequest.balance();
        final LocalDateTime createdAt = now();
        final UserId userId = UserId.fromInt(accountRequest.userId());
        return Account.open(AccountId.newAccount(), accountNumber, userId, Amount.of(balance), createdAt);
    }

}
