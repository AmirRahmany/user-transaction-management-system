package com.dev.user_transaction_management_system.repository;

import com.dev.user_transaction_management_system.model.UserEntity;

import java.util.Optional;

public interface UserRepository{

    void enroll(UserEntity user);

    Optional<UserEntity> findByEmail(String email);

    boolean isUserAlreadyExists(String email);

    Optional<UserEntity> findById(Integer userId);
}
