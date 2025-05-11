package com.dev.user_transaction_management_system.use_case;

import com.dev.user_transaction_management_system.domain.exceptions.CouldNotFoundUser;
import com.dev.user_transaction_management_system.domain.user.UserRepository;
import com.dev.user_transaction_management_system.domain.user.UserStatus;
import com.dev.user_transaction_management_system.fake.AccountNumberGeneratorStubs;
import com.dev.user_transaction_management_system.fake.BankAccountRepositoryFake;
import com.dev.user_transaction_management_system.fake.UserRepositoryFake;
import com.dev.user_transaction_management_system.helper.UserAccountTestHelper;
import com.dev.user_transaction_management_system.infrastructure.persistence.model.UserEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static com.dev.user_transaction_management_system.fake.UserFakeBuilder.aUser;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ActivatingUserAccountTests {

    private final ActivatingUserAccount activatingUserAccount;

    private final UserAccountTestHelper helper;

    public ActivatingUserAccountTests() {
        UserRepository userRepository = new UserRepositoryFake();
        activatingUserAccount = new ActivatingUserAccount(userRepository);
        helper = new UserAccountTestHelper(userRepository);
    }

    @Test
    void enabling_user_account_successfully() {
        final UserEntity entity = helper.havingRegistered(aUser());

        assertThatNoException().isThrownBy(() -> activatingUserAccount.activate(entity.getId()));
    }

    @Test
    void cant_enabling_an_unknown_user_account() {
        assertThatExceptionOfType(CouldNotFoundUser.class).isThrownBy(() -> {
            final int unknownUser = 74;
            activatingUserAccount.activate(unknownUser);
        });
    }

    @Test
    void should_not_persist_when_user_account_is_already_enabled() {
        final UserEntity entity = helper.havingRegistered(aUser());
        entity.setUserStatus(UserStatus.ENABLE);
        final UserRepository repository = mock(UserRepository.class);
        ActivatingUserAccount userAccount = new ActivatingUserAccount(repository);

        final Integer userId = entity.getId();
        when(repository.findById(userId)).thenReturn(Optional.of(entity));

        assertThatNoException().isThrownBy(() -> userAccount.activate(userId));
        verify(repository, times(0)).save(any(UserEntity.class));
    }
}
