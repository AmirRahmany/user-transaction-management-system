package com.dev.user_transaction_management_system.use_case;

import com.dev.user_transaction_management_system.domain.exceptions.CouldNotFoundUser;
import com.dev.user_transaction_management_system.domain.user.UserRepository;
import com.dev.user_transaction_management_system.domain.user.UserStatus;
import com.dev.user_transaction_management_system.fake.UserFakeBuilder;
import com.dev.user_transaction_management_system.fake.UserRepositoryFake;
import com.dev.user_transaction_management_system.infrastructure.persistence.model.UserEntity;
import com.dev.user_transaction_management_system.infrastructure.util.UserMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.dev.user_transaction_management_system.fake.UserFakeBuilder.aUser;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ActivatingUserAccountTests {

    private UserRepository userRepository;

    private ActivatingUserAccount activatingUserAccount;


    public ActivatingUserAccountTests() {
        this.userRepository = new UserRepositoryFake();
        this.activatingUserAccount = new ActivatingUserAccount(userRepository);
    }

    @Test
    void enabling_user_account_successfully() {
        final UserEntity entity = havingRegistered(aUser());

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
    void should_not_call_save_if_user_is_already_enabled() {
        final UserEntity entity = havingRegistered(aUser());
        entity.setUserStatus(UserStatus.ENABLE);

        final UserRepository repository = mock(UserRepository.class);

        when(repository.findById(entity.getId())).thenReturn(Optional.of(entity));

        ActivatingUserAccount userAccount = new ActivatingUserAccount(repository);

        assertThatNoException().isThrownBy(() -> userAccount.activate(entity.getId()));
        verify(repository, times(0)).save(any(UserEntity.class));
    }

    private UserEntity havingRegistered(UserFakeBuilder userFakeBuilder) {
        final UserMapper userMapper = new UserMapper();
        final UserEntity entity = userMapper.toEntity(userFakeBuilder.build());
        userRepository.save(entity);
        return entity;
    }
}
