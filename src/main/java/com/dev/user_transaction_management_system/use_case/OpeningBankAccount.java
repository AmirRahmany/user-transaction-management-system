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
import java.util.UUID;

import static java.time.LocalDateTime.now;

@Service
@Transactional
public class OpeningBankAccount {

    private final BankAccountRepository bankAccountRepository;
    private final AccountNumberGenerator accountNumberGenerator;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public OpeningBankAccount(BankAccountRepository accountRepository,
                              AccountNumberGenerator accountNumberGenerator,
                              UserRepository userRepository) {
        this.bankAccountRepository = accountRepository;
        this.accountNumberGenerator = accountNumberGenerator;
        this.userRepository = userRepository;
        this.userMapper = new UserMapper();
    }

    public OpeningAccountResponse open(AccountRequest accountRequest) {
        final User user = findUserBy(accountRequest.userId());
        user.ensureUserIsEnabled();
        final UUID accountId = bankAccountRepository.nextIdentify();
        final AccountNumber accountNumber = generateAccountNumber();

        final BankAccount account = openBankAccount(accountRequest, accountNumber,accountId);

        bankAccountRepository.save(account.toEntity());
        return account.toResponse(user.fullName());
    }

    private User findUserBy(String userId) {
        UserEntity userEntity = userRepository.findById(UserId.fromString(userId))
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

    private static BankAccount openBankAccount(AccountRequest accountRequest, AccountNumber accountNumber, UUID accountUUID) {
        final double balance = accountRequest.balance();
        final LocalDateTime createdAt = now();
        final UserId userId = UserId.fromUUID(UUID.fromString(accountRequest.userId()));
        final AccountId accountId = AccountId.fromUUID(accountUUID);
        return BankAccount.open(accountId,accountNumber, userId, Amount.of(balance), createdAt);
    }

}
