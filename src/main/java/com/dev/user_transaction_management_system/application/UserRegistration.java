package com.dev.user_transaction_management_system.application;

import com.dev.user_transaction_management_system.domain.user.User;
import com.dev.user_transaction_management_system.exceptions.CouldNotRegisterUserAlreadyExists;
import com.dev.user_transaction_management_system.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserRegistration implements Registration {

    private final UserRepository userRepository;

    public UserRegistration(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void register(User user) {
        validateUserAlreadyExists(user);

        userRepository.enroll(user);
    }

    private void validateUserAlreadyExists(User user) {
        if (userRepository.findByEmail(user.email()).isPresent()) {
            throw new CouldNotRegisterUserAlreadyExists();
        }
    }
}
