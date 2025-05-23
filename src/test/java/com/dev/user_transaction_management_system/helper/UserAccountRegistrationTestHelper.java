package com.dev.user_transaction_management_system.helper;

import com.dev.user_transaction_management_system.domain.exceptions.CouldNotFoundUser;
import com.dev.user_transaction_management_system.domain.user.User;
import com.dev.user_transaction_management_system.domain.user.UserRepository;
import com.dev.user_transaction_management_system.domain.user.UserStatus;
import com.dev.user_transaction_management_system.fake.UserFakeBuilder;
import com.dev.user_transaction_management_system.infrastructure.persistence.model.UserEntity;
import com.dev.user_transaction_management_system.use_case.RegisteringUserAccount;
import com.dev.user_transaction_management_system.use_case.dto.UserRegistrationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import static com.dev.user_transaction_management_system.fake.UserFakeBuilder.aUser;

@Component
@Transactional
public class UserAccountRegistrationTestHelper {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;


    @Autowired
    public UserAccountRegistrationTestHelper(UserRepository userRepository,
                                             PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
       this.passwordEncoder = passwordEncoder;
    }



    public UserEntity havingRegistered(UserFakeBuilder userFakeBuilder) {
        final User user = userFakeBuilder.build();
        final UserEntity entity = user.toEntity();
        entity.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(entity);
        return entity;
    }


    public UserEntity havingEnabledUser() {
        final UserEntity entity = havingRegistered(aUser());
        entity.setUserStatus(UserStatus.ENABLE);
        userRepository.save(entity);
        return entity;
    }

}
