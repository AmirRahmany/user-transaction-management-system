package com.dev.user_transaction_management_system.helper;

import com.dev.user_transaction_management_system.domain.user.User;
import com.dev.user_transaction_management_system.domain.user.UserRepository;
import com.dev.user_transaction_management_system.domain.user.UserStatus;
import com.dev.user_transaction_management_system.infrastructure.persistence.model.UserEntity;
import com.dev.user_transaction_management_system.test_builder.UserTestBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import static com.dev.user_transaction_management_system.test_builder.UserTestBuilder.aUser;

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



    public UserEntity havingRegistered(UserTestBuilder userTestBuilder) {
        final User user = userTestBuilder.build();
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
