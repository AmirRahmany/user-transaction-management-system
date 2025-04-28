package com.dev.user_transaction_management_system.application;

import com.dev.user_transaction_management_system.domain.user.User;
import com.dev.user_transaction_management_system.exceptions.CouldNotRegisterUserAlreadyExists;
import com.dev.user_transaction_management_system.util.UserMapper;
import com.dev.user_transaction_management_system.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;

@Service
public class UserRegistration implements Registration {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserRegistration(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.userMapper = new UserMapper();
    }

    @Transactional
    public void register(User user) {
        validateUserAlreadyExists(user);

        userRepository.enroll(userMapper.toEntity(user,0));
    }

    private void validateUserAlreadyExists(User user) {
        if (userRepository.isUserAlreadyExists(user.email())) {
            throw new CouldNotRegisterUserAlreadyExists();
        }
    }
}
