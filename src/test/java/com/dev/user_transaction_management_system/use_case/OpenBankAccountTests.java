package com.dev.user_transaction_management_system.use_case;

import com.dev.user_transaction_management_system.domain.exceptions.CouldNotFoundUser;
import com.dev.user_transaction_management_system.domain.exceptions.CouldNotOpenAnAccount;
import com.dev.user_transaction_management_system.domain.user.UserRepository;
import com.dev.user_transaction_management_system.fake.*;
import com.dev.user_transaction_management_system.helper.UserAccountRegistrationTestHelper;
import com.dev.user_transaction_management_system.infrastructure.persistence.model.UserEntity;
import com.dev.user_transaction_management_system.use_case.open_bank_account.AccountRequest;
import com.dev.user_transaction_management_system.use_case.open_bank_account.OpenBankAccount;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.dev.user_transaction_management_system.test_builder.AccountRequestTestBuilder.accountRequest;
import static com.dev.user_transaction_management_system.test_builder.UserTestBuilder.aUser;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class OpenBankAccountTests {
    private OpenBankAccount openBankAccount;

    private UserAccountRegistrationTestHelper helper;

    @BeforeEach
    void setUp() {
        UserRepository userRepository = new UserRepositoryFake();
        helper = new UserAccountRegistrationTestHelper(userRepository, new PasswordEncoderStub());
        openBankAccount = new OpenBankAccount(
                new BankAccountRepositoryFake(),
                new AccountNumberProviderStub(),
                userRepository,
                new FakeEventPublisher(),
                new FakeClock()
        );
    }

    @Test
    void open_an_account_successfully() {
        final UserEntity entity = helper.havingEnabledUser();
        assertThatNoException().isThrownBy(() ->
                openBankAccount.open(accountRequest().withUsername(entity.getUsername()).open()));
    }

    @Test
    void cannot_open_account_with_deposit_less_than_100_dolor() {
        final UserEntity entity = helper.havingEnabledUser();
        final AccountRequest accountRequest = accountRequest().withUsername(entity.getUsername()).withBalance(80).open();

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> openBankAccount.open(accountRequest));
    }

    @Test
    void cannot_open_account_without_any_user() {
        assertThatExceptionOfType(CouldNotFoundUser.class)
                .isThrownBy(() -> openBankAccount.open(accountRequest().withNoUser().open()));
    }

    @Test
    void cannot_open_account_with_duplicate_account_number() {
        final UserEntity user = helper.havingEnabledUser();

        final AccountRequest accountRequest = accountRequest().withUsername(user.getUsername()).open();
        openBankAccount.open(accountRequest);

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> openBankAccount.open(accountRequest));
    }

    @Test
    void can_not_open_an_account_for_disable_user() {
        final UserEntity disableUser = helper.havingRegistered(aUser().withDisabledStatus());

        final AccountRequest accountRequest = accountRequest().withUsername(disableUser.getUsername()).open();

        assertThatExceptionOfType(CouldNotOpenAnAccount.class).isThrownBy(() -> openBankAccount.open(accountRequest));
    }
}
