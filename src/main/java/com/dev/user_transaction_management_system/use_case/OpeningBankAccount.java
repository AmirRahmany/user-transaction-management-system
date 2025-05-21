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
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

import static com.dev.user_transaction_management_system.domain.bank_account.AccountStatus.DISABLE;
import static java.time.LocalDateTime.now;

@Service
@Transactional
public class OpeningBankAccount {

    private final BankAccountRepository bankAccountRepository;
    private final AccountNumberGenerator accountNumberGenerator;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final ApplicationEventPublisher eventPublisher;

    public OpeningBankAccount(BankAccountRepository accountRepository,
                              AccountNumberGenerator accountNumberGenerator,
                              UserRepository userRepository, ApplicationEventPublisher eventPublisher) {
        this.bankAccountRepository = accountRepository;
        this.accountNumberGenerator = accountNumberGenerator;
        this.userRepository = userRepository;
        this.eventPublisher = eventPublisher;
        this.userMapper = new UserMapper();
    }

    public OpeningAccountResponse open(AccountRequest accountRequest) {
        final User user = findUserBy(accountRequest.username());
        user.ensureUserIsEnabled();
        final UUID accountId = bankAccountRepository.nextIdentify();
        final AccountNumber accountNumber = generateAccountNumber();

        var bankAccount = openBankAccount(accountRequest, accountNumber, accountId,user);

        bankAccountRepository.save(bankAccount.toEntity());
        bankAccount.recordEvents().forEach(eventPublisher::publishEvent);
        return bankAccount.toResponse(user.fullName());
    }

    private User findUserBy(String email) {
        UserEntity userEntity = userRepository.findByEmail(email)
                .orElseThrow(() -> CouldNotFoundUser.withEmail(email));

        return userMapper.toDomain(userEntity);
    }

    private AccountNumber generateAccountNumber() {
        final AccountNumber accountNumber = accountNumberGenerator.generate();
        if (bankAccountRepository.accountExists(accountNumber)) {
            throw new IllegalArgumentException("BankAccount Number must be unique");
        }
        return accountNumber;
    }

    private static BankAccount openBankAccount(AccountRequest accountRequest,
                                               AccountNumber accountNumber,
                                               UUID accountUUID,
                                               User user) {
        final double balance = accountRequest.balance();
        final LocalDateTime createdAt = now();
        final AccountId accountId = AccountId.fromUUID(accountUUID);

        return BankAccount.open(accountId, accountNumber, user, Amount.of(balance), DISABLE, createdAt);
    }

}
