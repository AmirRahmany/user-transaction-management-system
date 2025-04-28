package com.dev.user_transaction_management_system.repository;

import com.dev.user_transaction_management_system.domain.user.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserRepositoryImpl implements UserRepository {


    @Override
    public void enroll(User user) {

    }

    @Override
    public Optional<User> findByEmail(String email) {
        return Optional.empty();
    }
}
