package com.dev.user_transaction_management_system.use_case;

import com.dev.user_transaction_management_system.domain.exceptions.CouldNotActivateUserAccount;
import com.dev.user_transaction_management_system.domain.exceptions.CouldNotFoundUser;
import com.dev.user_transaction_management_system.domain.user.UserRepository;
import com.dev.user_transaction_management_system.domain.user.UserStatus;
import com.dev.user_transaction_management_system.fake.FakeEventPublisher;
import com.dev.user_transaction_management_system.fake.PasswordEncoderStub;
import com.dev.user_transaction_management_system.fake.UserRepositoryFake;
import com.dev.user_transaction_management_system.helper.UserAccountRegistrationTestHelper;
import com.dev.user_transaction_management_system.infrastructure.persistence.model.UserEntity;
import com.dev.user_transaction_management_system.infrastructure.util.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.dev.user_transaction_management_system.test_builder.FakeUser.UNKNOWN_USER;
import static com.dev.user_transaction_management_system.test_builder.UserTestBuilder.aUser;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ActivatingUserAccountTests {

    private final ActivatingUserAccount activatingUserAccount;

    private final UserAccountRegistrationTestHelper helper;
    private final FakeEventPublisher publisher;


    public ActivatingUserAccountTests() {
        publisher = new FakeEventPublisher();
        UserRepository userRepository = new UserRepositoryFake();
        activatingUserAccount = new ActivatingUserAccount(userRepository, publisher,new UserMapper());
        helper = new UserAccountRegistrationTestHelper(userRepository, new PasswordEncoderStub());
    }

    @Test
    void enable_user_account_successfully() {
        final UserEntity entity = helper.havingRegistered(aUser());

        assertThatNoException().isThrownBy(() -> activatingUserAccount.activate(entity.getUsername()));
    }

    @Test
    void cant_enable_an_unknown_user_account() {
        assertThatExceptionOfType(CouldNotFoundUser.class).isThrownBy(() -> {
            activatingUserAccount.activate(UNKNOWN_USER);
        });
    }

    @Test
    void does_not_reactivate_already_enabled_user_account() {
        final UserEntity entity = helper.havingRegistered(aUser());
        entity.setUserStatus(UserStatus.ENABLE);

        final UserRepository repository = mock(UserRepository.class);
        ActivatingUserAccount userAccount = new ActivatingUserAccount(repository, publisher,new UserMapper());

        when(repository.findByEmail(any())).thenReturn(Optional.of(entity));

        assertThatExceptionOfType(CouldNotActivateUserAccount.class)
                .isThrownBy(()->userAccount.activate(entity.getUsername()));
        verify(repository, times(0)).save(entity);
    }
}
