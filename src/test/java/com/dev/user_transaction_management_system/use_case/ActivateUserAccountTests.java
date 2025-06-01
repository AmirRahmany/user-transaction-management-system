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
import com.dev.user_transaction_management_system.use_case.activate_user_account.ActivateUserAccount;
import com.dev.user_transaction_management_system.use_case.activate_user_account.UserActivationRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.dev.user_transaction_management_system.test_builder.FakeAccount.User;
import static com.dev.user_transaction_management_system.test_builder.UserTestBuilder.aUser;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.postgresql.shaded.com.ongres.scram.common.message.ServerFinalMessage.Error.UNKNOWN_USER;

@ExtendWith(MockitoExtension.class)
class ActivateUserAccountTests {

    private final ActivateUserAccount activateUserAccount;

    private final UserAccountRegistrationTestHelper helper;
    private final FakeEventPublisher publisher;


    public ActivateUserAccountTests() {
        publisher = new FakeEventPublisher();
        UserRepository userRepository = new UserRepositoryFake();
        activateUserAccount = new ActivateUserAccount(userRepository, publisher,new UserMapper());
        helper = new UserAccountRegistrationTestHelper(userRepository, new PasswordEncoderStub());
    }

    @Test
    void enable_user_account_successfully() {
        final UserEntity entity = helper.havingRegistered(aUser());
        final UserActivationRequest activationRequest = createActivationRequestFor(entity.getUsername());

        assertThatNoException().isThrownBy(() -> activateUserAccount.activate(activationRequest));
    }

    @Test
    void cant_enable_an_unknown_user_account() {
        final UserActivationRequest activationRequest = createActivationRequestFor(User.UNKNOWN);

        assertThatExceptionOfType(CouldNotFoundUser.class)
                .isThrownBy(() -> activateUserAccount.activate(activationRequest));
    }

    @Test
    void does_not_reactivate_already_enabled_user_account() {
        final UserEntity entity = helper.havingRegistered(aUser());
        entity.setUserStatus(UserStatus.ENABLE);
        final UserActivationRequest activationRequest = createActivationRequestFor(entity.getUsername());

        final UserRepository repository = mock(UserRepository.class);
        ActivateUserAccount userAccount = new ActivateUserAccount(repository, publisher,new UserMapper());

        when(repository.findByEmail(any())).thenReturn(Optional.of(entity));

        assertThatExceptionOfType(CouldNotActivateUserAccount.class)
                .isThrownBy(()-> userAccount.activate(activationRequest));
        verify(repository, times(0)).save(entity);
    }

    private static UserActivationRequest createActivationRequestFor(String username) {
        return new UserActivationRequest(username);
    }
}
