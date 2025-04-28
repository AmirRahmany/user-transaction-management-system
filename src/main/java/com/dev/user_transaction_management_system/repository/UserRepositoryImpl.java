package com.dev.user_transaction_management_system.repository;

import com.dev.user_transaction_management_system.entity.UserEntity;
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
        entityManager.persist(user);
    }

    @Override
    public Optional<UserEntity> findByEmail(String email) {
        final String sql = "FROM UserEntity where email=:email";

        try{
            final UserEntity userEntity = entityManager.createQuery(sql, UserEntity.class)
                    .setParameter("email", email)
                    .getSingleResult();

            return Optional.ofNullable(userEntity);

        } catch (NoResultException e) {
            return Optional.empty();
        }
    }
}
