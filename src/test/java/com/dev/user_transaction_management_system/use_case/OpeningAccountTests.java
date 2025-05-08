package com.dev.user_transaction_management_system.use_case;

import com.dev.user_transaction_management_system.domain.exceptions.CouldNotFoundUser;
import com.dev.user_transaction_management_system.domain.exceptions.CouldNotOpenAnAccount;
import com.dev.user_transaction_management_system.domain.user.User;
import com.dev.user_transaction_management_system.fake.AccountNumberGeneratorStubs;
import com.dev.user_transaction_management_system.fake.AccountRepositoryFake;
import com.dev.user_transaction_management_system.fake.UserFakeBuilder;
import com.dev.user_transaction_management_system.fake.UserRepositoryFake;
import com.dev.user_transaction_management_system.infrastructure.persistence.model.UserEntity;
import com.dev.user_transaction_management_system.domain.user.UserRepository;
import com.dev.user_transaction_management_system.use_case.dto.AccountRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.dev.user_transaction_management_system.fake.AccountRequestFakeBuilder.accountRequest;
import static com.dev.user_transaction_management_system.fake.UserFakeBuilder.aUser;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class OpeningAccountTests {

    private final OpeningAccount openingAccount;
    private final UserRepository userRepository;

    public OpeningAccountTests() {
        userRepository = new UserRepositoryFake();
        openingAccount = new OpeningAccount(new AccountRepositoryFake(),
                new AccountNumberGeneratorStubs(),
                userRepository);
    }

    @Test
    void open_an_account_successfully() {
        final UserEntity entity = havingRegistered(aUser().withEnabledStatus());
        assertThatNoException().isThrownBy(() ->
                openingAccount.open(accountRequest().withUserId(entity.getId()).open()));
    }

    @Test
    void cannot_open_account_with_deposit_less_than_100_dolor() {
        final UserEntity entity = havingRegistered(aUser().withEnabledStatus());
        final AccountRequest accountRequest = accountRequest().withUserId(entity.getId()).withBalance(80).open();

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> openingAccount.open(accountRequest));

    }

    @Test
    void cannot_open_account_without_any_user() {
        final AccountRequest accountRequest = accountRequest().withNoUser().open();

        assertThatExceptionOfType(CouldNotFoundUser.class)
                .isThrownBy(() -> openingAccount.open(accountRequest));
    }

    @Test
    void cannot_open_account_with_duplicate_account_number() {
        final UserEntity user = havingRegistered(aUser().withEnabledStatus());

        final AccountRequest accountRequest = accountRequest().withUserId(user.getId()).open();
        openingAccount.open(accountRequest);

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> openingAccount.open(accountRequest));
    }

    @Test
    void can_not_open_an_account_for_disable_user() {
        final UserEntity disableUser = havingRegistered(aUser().withDisabledStatus());

        final AccountRequest accountRequest = accountRequest().withUserId(disableUser.getId()).open();

        assertThatExceptionOfType(CouldNotOpenAnAccount.class).isThrownBy(() -> openingAccount.open(accountRequest));
    }

    private UserEntity havingRegistered(UserFakeBuilder userFakeBuilder) {
        final User user = userFakeBuilder.build();
        final UserEntity entity = user.toEntity();
        userRepository.enroll(entity);
        return entity;
    }
}
