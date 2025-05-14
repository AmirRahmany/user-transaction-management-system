package com.dev.user_transaction_management_system.domain.user;

import com.dev.user_transaction_management_system.infrastructure.persistence.model.UserEntity;

import java.util.Optional;

public interface UserRepository{

    void save(UserEntity user);

    Optional<UserEntity> findByEmail(String email);

    boolean isUserAlreadyExists(String email);

    Optional<UserEntity> findById(UserId userId);
}
