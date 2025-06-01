package com.dev.user_transaction_management_system.use_case;

import com.dev.user_transaction_management_system.domain.Clock;
import com.dev.user_transaction_management_system.domain.Date;
import com.dev.user_transaction_management_system.domain.bank_account.*;
import com.dev.user_transaction_management_system.domain.transaction.Amount;
import com.dev.user_transaction_management_system.domain.user.Email;
import com.dev.user_transaction_management_system.domain.user.User;
import com.dev.user_transaction_management_system.infrastructure.util.mapper.UserMapper;
import com.dev.user_transaction_management_system.use_case.dto.AccountRequest;
import com.dev.user_transaction_management_system.use_case.dto.OpeningAccountResponse;
import com.dev.user_transaction_management_system.domain.exceptions.CouldNotFoundUser;
import com.dev.user_transaction_management_system.infrastructure.persistence.model.UserEntity;
import com.dev.user_transaction_management_system.domain.user.UserRepository;
import io.jsonwebtoken.lang.Assert;
import jakarta.transaction.Transactional;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import static com.dev.user_transaction_management_system.domain.bank_account.AccountStatus.DISABLE;
import static java.time.LocalDateTime.now;

@Service
@Transactional
public class OpeningBankAccount {

    private final BankAccountRepository bankAccountRepository;
    private final AccountNumberProvider accountNumberProvider;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final ApplicationEventPublisher eventPublisher;
    private final Clock clock;

    public OpeningBankAccount(@NonNull BankAccountRepository accountRepository,
                              @NonNull AccountNumberProvider accountNumberProvider,
                              @NonNull UserRepository userRepository,
                              @NonNull ApplicationEventPublisher eventPublisher,
                              @NonNull Clock clock) {

        this.bankAccountRepository = accountRepository;
        this.accountNumberProvider = accountNumberProvider;
        this.userRepository = userRepository;
        this.eventPublisher = eventPublisher;
        this.clock = clock;
        this.userMapper = new UserMapper();
    }

    public OpeningAccountResponse open(AccountRequest request) {
        Assert.notNull(request,"account request cannot be null");

        final AccountId accountId = bankAccountRepository.nextIdentify();
        final var accountNumber = generateAccountNumber();
        final User user = findUserBy(request.username());
        final Date createAt = Date.fromCurrentTime(clock.currentTime());

        user.ensureUserIsEnabled();
        final var bankAccount =
                BankAccount.open(accountId, accountNumber, user, Amount.of(request.balance()), DISABLE, createAt);

        bankAccountRepository.save(bankAccount.toEntity());
        bankAccount.releaseEvents().forEach(eventPublisher::publishEvent);
        return bankAccount.toResponse(user.fullName());
    }

    private User findUserBy(String email) {
        UserEntity userEntity = userRepository.findByEmail(Email.of(email))
                .orElseThrow(() -> CouldNotFoundUser.withEmail(email));

        return userMapper.toDomain(userEntity);
    }

    private AccountNumber generateAccountNumber() {
        final AccountNumber accountNumber = accountNumberProvider.generateAccountNumber();
        if (bankAccountRepository.accountExists(accountNumber)) {
            throw new IllegalArgumentException("BankAccount Number must be unique");
        }
        return accountNumber;
    }

}
