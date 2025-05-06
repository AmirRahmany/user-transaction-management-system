package com.dev.user_transaction_management_system.fake;

import com.dev.user_transaction_management_system.model.UserEntity;
import com.dev.user_transaction_management_system.repository.UserRepository;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class UserRepositoryFake implements UserRepository {

    private Integer userId = 20;
    private final List<UserEntity> recordUsers = new LinkedList<>();

    @Override
    public void enroll(UserEntity user) {
        user.setId(userId);
        recordUsers.add(user);
    }

    @Override
    public Optional<UserEntity> findByEmail(String email) {
        return recordUsers.stream().filter(user -> user.getEmail().equals(email)).findFirst();
    }

    @Override
    public boolean isUserAlreadyExists(String email) {
        return findByEmail(email).isPresent();
    }

    @Override
    public Optional<UserEntity> findById(Integer userId) {
        return recordUsers.stream().filter(user->user.getId().equals(userId)).findFirst();
    }
}
