package com.dev.user_transaction_management_system.repository;

import com.dev.user_transaction_management_system.domain.user.User;

import java.util.Optional;

public interface UserRepository {

    void enroll(User user);

    Optional<User> findByEmail(String email);

}
