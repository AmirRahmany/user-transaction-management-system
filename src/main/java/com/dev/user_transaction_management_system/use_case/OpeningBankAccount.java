package com.dev.user_transaction_management_system.use_case;

import com.dev.user_transaction_management_system.domain.bank_account.*;
import com.dev.user_transaction_management_system.domain.transaction.Amount;
import com.dev.user_transaction_management_system.domain.user.User;
import com.dev.user_transaction_management_system.domain.user.UserId;
import com.dev.user_transaction_management_system.infrastructure.util.UserMapper;
import com.dev.user_transaction_management_system.use_case.dto.AccountRequest;
import com.dev.user_transaction_management_system.use_case.dto.OpeningAccountResponse;
import com.dev.user_transaction_management_system.domain.exceptions.CouldNotFoundUser;
import com.dev.user_transaction_management_system.infrastructure.persistence.model.UserEntity;
import com.dev.user_transaction_management_system.domain.user.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static java.time.LocalDateTime.now;

@Service
@Transactional
public class OpeningBankAccount {

    private final BankAccountRepository bankAccountRepository;
    private final IAccountNumberGenerator accountNumberGenerator;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public OpeningBankAccount(BankAccountRepository accountRepository,
                              IAccountNumberGenerator accountNumberGenerator,
                              UserRepository userRepository) {
        this.bankAccountRepository = accountRepository;
        this.accountNumberGenerator = accountNumberGenerator;
        this.userRepository = userRepository;
        this.userMapper = new UserMapper();
    }

    public OpeningAccountResponse open(AccountRequest accountRequest) {
        final User user = findUserBy(accountRequest.userId());
        user.ensureUserIsEnabled();

        final AccountNumber accountNumber = generateAccountNumber();
        final BankAccount account = openAccount(accountRequest, accountNumber);

        bankAccountRepository.save(account.toEntity());
        return account.toResponse(user.fullName());
    }

    private User findUserBy(Integer userId) {
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> CouldNotFoundUser.withId(userId));

        return userMapper.toDomain(userEntity);
    }

    private AccountNumber generateAccountNumber() {
        final AccountNumber accountNumber = accountNumberGenerator.generate();
        if (bankAccountRepository.accountExists(accountNumber)) {
            throw new IllegalArgumentException("BankAccount Number must be unique");
        }
        return accountNumber;
    }

    private static BankAccount openAccount(AccountRequest accountRequest, AccountNumber accountNumber) {
        final double balance = accountRequest.balance();
        final LocalDateTime createdAt = now();
        final UserId userId = UserId.fromInt(accountRequest.userId());
        return BankAccount.open(AccountId.newAccount(), accountNumber, userId, Amount.of(balance), createdAt);
    }

}
