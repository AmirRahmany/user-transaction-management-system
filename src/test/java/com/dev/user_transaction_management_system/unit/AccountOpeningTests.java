package com.dev.user_transaction_management_system.unit;

import com.dev.user_transaction_management_system.application.AccountOpening;
import com.dev.user_transaction_management_system.domain.user.User;
import com.dev.user_transaction_management_system.fake.AccountRepositoryFake;
import com.dev.user_transaction_management_system.fake.UserFake;
import com.dev.user_transaction_management_system.fake.UserRepositoryFake;
import com.dev.user_transaction_management_system.model.UserEntity;
import com.dev.user_transaction_management_system.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.dev.user_transaction_management_system.fake.AccountRequestFakeBuilder.accountRequest;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNoException;

@ExtendWith(MockitoExtension.class)
class AccountOpeningTests {

    private final AccountOpening accountOpening;
    private final UserRepository userRepository;

    public AccountOpeningTests() {
        userRepository = new UserRepositoryFake();
        accountOpening = new AccountOpening(new AccountRepositoryFake(),
                new AccountNumberGeneratorStubs(),
                userRepository);
    }

    @Test
    void open_an_account_successfully() {
        final UserEntity entity = getRegisteredUser();
        assertThatNoException().isThrownBy(() ->
                accountOpening.open(accountRequest().withUserId(entity.getId()).open()));
    }

    @Test
    void cannot_open_account_with_deposit_less_than_100_dolor() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> accountOpening.open(accountRequest().withBalance(80).open()));

    }

    @Test
    void cannot_open_account_without_any_user() {

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> accountOpening.open(accountRequest().withNoUser().open()));
    }

    @Test
    void cannot_open_account_with_duplicate_account_number() {
        final UserEntity entity = getRegisteredUser();
        accountOpening.open(accountRequest().withUserId(entity.getId()).open());

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> accountOpening.open(accountRequest().open()));
    }

    private UserEntity getRegisteredUser() {
        final User user = UserFake.user().build();
        final UserEntity entity = user.toEntity();
        userRepository.enroll(entity);
        return entity;
    }
}
