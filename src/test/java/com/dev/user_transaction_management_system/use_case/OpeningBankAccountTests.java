package com.dev.user_transaction_management_system.use_case;

import com.dev.user_transaction_management_system.domain.exceptions.CouldNotFoundUser;
import com.dev.user_transaction_management_system.domain.exceptions.CouldNotOpenAnAccount;
import com.dev.user_transaction_management_system.domain.user.UserRepository;
import com.dev.user_transaction_management_system.fake.AccountNumberGeneratorStubs;
import com.dev.user_transaction_management_system.fake.BankAccountRepositoryFake;
import com.dev.user_transaction_management_system.fake.UserRepositoryFake;
import com.dev.user_transaction_management_system.helper.UserAccountTestHelper;
import com.dev.user_transaction_management_system.infrastructure.persistence.model.UserEntity;
import com.dev.user_transaction_management_system.use_case.dto.AccountRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static com.dev.user_transaction_management_system.fake.AccountRequestFakeBuilder.accountRequest;
import static com.dev.user_transaction_management_system.fake.UserFakeBuilder.aUser;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class OpeningBankAccountTests  {
    private OpeningBankAccount openingBankAccount;

    private UserAccountTestHelper helper;
    @BeforeEach
    void setUp() {
        UserRepository userRepository = new UserRepositoryFake();
        helper = new UserAccountTestHelper(userRepository);
        openingBankAccount = new OpeningBankAccount(new BankAccountRepositoryFake(),
                new AccountNumberGeneratorStubs(), userRepository);
    }

    @Test
    void open_an_account_successfully() {
        final UserEntity entity = helper.havingRegistered(aUser().withEnabledStatus());
        assertThatNoException().isThrownBy(() ->
                openingBankAccount.open(accountRequest().withUserId(entity.getId()).open()));
    }

    @Test
    void cannot_open_account_with_deposit_less_than_100_dolor() {
        final UserEntity entity = helper.havingRegistered(aUser().withEnabledStatus());
        final AccountRequest accountRequest = accountRequest().withUserId(entity.getId()).withBalance(80).open();

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> openingBankAccount.open(accountRequest));

    }

    @Test
    void cannot_open_account_without_any_user() {
        final AccountRequest accountRequest = accountRequest().withNoUser().open();

        assertThatExceptionOfType(CouldNotFoundUser.class)
                .isThrownBy(() -> openingBankAccount.open(accountRequest));
    }

    @Test
    void cannot_open_account_with_duplicate_account_number() {
        final UserEntity user = helper.havingRegistered(aUser().withEnabledStatus());

        final AccountRequest accountRequest = accountRequest().withUserId(user.getId()).open();
        openingBankAccount.open(accountRequest);

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> openingBankAccount.open(accountRequest));
    }

    @Test
    void can_not_open_an_account_for_disable_user() {
        final UserEntity disableUser = helper.havingRegistered(aUser().withDisabledStatus());

        final AccountRequest accountRequest = accountRequest().withUserId(disableUser.getId()).open();

        assertThatExceptionOfType(CouldNotOpenAnAccount.class).isThrownBy(() -> openingBankAccount.open(accountRequest));
    }
}
