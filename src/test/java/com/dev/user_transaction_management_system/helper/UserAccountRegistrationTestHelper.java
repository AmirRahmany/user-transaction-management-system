package com.dev.user_transaction_management_system.helper;

import com.dev.user_transaction_management_system.domain.exceptions.CouldNotFoundUser;
import com.dev.user_transaction_management_system.domain.user.UserRepository;
import com.dev.user_transaction_management_system.domain.user.UserStatus;
import com.dev.user_transaction_management_system.fake.UserFakeBuilder;
import com.dev.user_transaction_management_system.infrastructure.persistence.model.UserEntity;
import com.dev.user_transaction_management_system.use_case.RegisteringUserAccount;
import com.dev.user_transaction_management_system.use_case.dto.UserRegistrationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import static com.dev.user_transaction_management_system.fake.UserFakeBuilder.aUser;

@Component
public class UserAccountRegistrationTestHelper {

    private final UserRepository userRepository;

    private final RegisteringUserAccount registeringUserAccount;


    @Autowired
    public UserAccountRegistrationTestHelper(UserRepository userRepository,PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.registeringUserAccount = new RegisteringUserAccount(userRepository, passwordEncoder);
    }


    public UserEntity havingRegistered(UserFakeBuilder userFakeBuilder) {
        final UserRegistrationRequest request = userFakeBuilder.buildDTO();
        this.registeringUserAccount.register(request);
        return userRepository.findByEmail(request.email())
                .orElseThrow(()->CouldNotFoundUser.withEmail(request.email()));
    }

    public UserEntity havingEnabledUser() {
        final UserEntity entity = havingRegistered(aUser());
        entity.setUserStatus(UserStatus.ENABLE);
        userRepository.save(entity);
        return entity;
    }

}
