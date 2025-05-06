package com.dev.user_transaction_management_system.infrastructure.persistence.repository;

import com.dev.user_transaction_management_system.domain.user.UserRepository;
import com.dev.user_transaction_management_system.infrastructure.persistence.model.UserEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private final EntityManager entityManager;

    public UserRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void enroll(UserEntity user) {
        entityManager.merge(user);
    }

    @Override
    public Optional<UserEntity> findById(Integer userId) {
        final UserEntity userEntity = entityManager.find(UserEntity.class, userId);
        return Optional.ofNullable(userEntity);
    }

    @Override
    public Optional<UserEntity> findByEmail(String email) {
        final String sql = "FROM UserEntity where email=:email";

        try {
            final UserEntity userEntity = entityManager.createQuery(sql, UserEntity.class)
                    .setParameter("email", email)
                    .getSingleResult();
            return Optional.ofNullable(userEntity);

        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public boolean isUserAlreadyExists(String email) {
        return findByEmail(email).isPresent();
    }
}
