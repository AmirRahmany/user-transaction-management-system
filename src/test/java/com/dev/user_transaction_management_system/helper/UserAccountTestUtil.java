package com.dev.user_transaction_management_system.helper;

import com.dev.user_transaction_management_system.domain.exceptions.CouldNotFindBankAccount;
import com.dev.user_transaction_management_system.domain.user.UserRepository;
import com.dev.user_transaction_management_system.fake.UserFakeBuilder;
import com.dev.user_transaction_management_system.infrastructure.persistence.model.UserEntity;
import com.dev.user_transaction_management_system.use_case.ActivatingUserAccount;
import com.dev.user_transaction_management_system.use_case.RegisteringUserAccount;
import com.dev.user_transaction_management_system.use_case.dto.UserRegistrationRequest;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class UserAccountTestUtil {

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    private RegisteringUserAccount registeringUserAccount;

    @Autowired
    private ActivatingUserAccount activatingUserAccount;

    protected UserEntity havingRegistered(UserFakeBuilder userFakeBuilder) {
        final UserRegistrationRequest user = userFakeBuilder.buildDTO();
        registeringUserAccount.register(user);
        return findUserByEmail(user.email());
    }

    private UserEntity findUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(CouldNotFindBankAccount::new);
    }

    protected void activateUserAccount(int userId) {
        activatingUserAccount.activate(userId);
    }

}
