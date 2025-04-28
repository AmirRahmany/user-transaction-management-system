package com.dev.user_transaction_management_system.repository;

import com.dev.user_transaction_management_system.entity.UserEntity;

import java.util.Optional;

public interface UserRepository{

    void enroll(UserEntity user);

    Optional<UserEntity> findByEmail(String email);

}
