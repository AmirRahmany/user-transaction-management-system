package com.dev.user_transaction_management_system.use_case;

import com.dev.user_transaction_management_system.domain.exceptions.CouldNotFoundUser;
import com.dev.user_transaction_management_system.domain.exceptions.CouldNotOpenAnAccount;
import com.dev.user_transaction_management_system.domain.user.UserRepository;
import com.dev.user_transaction_management_system.fake.AccountNumberGeneratorStub;
import com.dev.user_transaction_management_system.fake.BankAccountRepositoryFake;
import com.dev.user_transaction_management_system.fake.PasswordEncoderStub;
import com.dev.user_transaction_management_system.fake.UserRepositoryFake;
import com.dev.user_transaction_management_system.helper.UserAccountRegistrationTestHelper;
import com.dev.user_transaction_management_system.infrastructure.persistence.model.UserEntity;
import com.dev.user_transaction_management_system.use_case.dto.AccountRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import static com.dev.user_transaction_management_system.fake.AccountRequestFakeBuilder.accountRequest;
import static com.dev.user_transaction_management_system.fake.UserFakeBuilder.aUser;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class OpeningBankAccountTests  {
    private OpeningBankAccount openingBankAccount;

    private UserAccountRegistrationTestHelper helper;

    @Mock
    private ApplicationEventPublisher publisher;

    @BeforeEach
    void setUp() {
        UserRepository userRepository = new UserRepositoryFake();
        helper = new UserAccountRegistrationTestHelper(userRepository,new PasswordEncoderStub(),publisher);
        openingBankAccount = new OpeningBankAccount(new BankAccountRepositoryFake(),
                new AccountNumberGeneratorStub(), userRepository);
    }

    @Test
    void open_an_account_successfully() {
        final UserEntity entity = helper.havingEnabledUser();
        assertThatNoException().isThrownBy(() ->
                openingBankAccount.open(accountRequest().withUsername(entity.getUsername()).open()));
    }

    @Test
    void cannot_open_account_with_deposit_less_than_100_dolor() {
        final UserEntity entity = helper.havingEnabledUser();
        final AccountRequest accountRequest = accountRequest().withUsername(entity.getUsername()).withBalance(80).open();

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
        final UserEntity user = helper.havingEnabledUser();

        final AccountRequest accountRequest = accountRequest().withUsername(user.getUsername()).open();
        openingBankAccount.open(accountRequest);

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> openingBankAccount.open(accountRequest));
    }

    @Test
    void can_not_open_an_account_for_disable_user() {
        final UserEntity disableUser = helper.havingRegistered(aUser().withDisabledStatus());

        final AccountRequest accountRequest = accountRequest().withUsername(disableUser.getUsername()).open();

        assertThatExceptionOfType(CouldNotOpenAnAccount.class).isThrownBy(() -> openingBankAccount.open(accountRequest));
    }
}
