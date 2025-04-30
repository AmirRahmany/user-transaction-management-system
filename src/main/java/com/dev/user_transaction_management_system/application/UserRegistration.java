package com.dev.user_transaction_management_system.application;

import com.dev.user_transaction_management_system.domain.user.User;
import com.dev.user_transaction_management_system.exceptions.CouldNotRegisterUserAlreadyExists;
import com.dev.user_transaction_management_system.util.UserMapper;
import com.dev.user_transaction_management_system.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserRegistration implements Registration {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserRegistration(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = new UserMapper();
    }

    @Transactional
    public void register(User user) {
        validateUserAlreadyExists(user);

        final String hashedPassword = passwordEncoder.encode(user.password());
        userRepository.enroll(userMapper.toEntity(user, hashedPassword));
    }

    private void validateUserAlreadyExists(User user) {
        if (userRepository.isUserAlreadyExists(user.email())) {
            throw new CouldNotRegisterUserAlreadyExists();
        }
    }
}
