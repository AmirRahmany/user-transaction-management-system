package com.dev.user_transaction_management_system.fake;

import com.dev.user_transaction_management_system.domain.user.UserId;
import com.dev.user_transaction_management_system.infrastructure.persistence.model.UserEntity;
import com.dev.user_transaction_management_system.domain.user.UserRepository;

import java.util.*;

public class UserRepositoryFake implements UserRepository {

    private final Map<String, UserEntity> recordUsers = new LinkedHashMap<>();


    @Override
    public void save(UserEntity user) {
        recordUsers.put(user.getId(), user);
    }

    @Override
    public Optional<UserEntity> findByEmail(String email) {
        return recordUsers.values().stream().filter(user -> user.getEmail().equals(email)).findFirst();
    }

    @Override
    public boolean isUserAlreadyExists(String email) {
        return findByEmail(email).isPresent();
    }

    @Override
    public Optional<UserEntity> findById(UserId userId) {
        return recordUsers.values().stream().filter(user -> user.getId().equals(userId.asString())).findFirst();
    }

    @Override
    public UUID nextIdentify() {
        return UUID.randomUUID();
    }
}
