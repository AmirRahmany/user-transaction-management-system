package com.dev.user_transaction_management_system.infrastructure.persistence.repository;

import com.dev.user_transaction_management_system.domain.user.UserId;
import com.dev.user_transaction_management_system.domain.user.UserRepository;
import com.dev.user_transaction_management_system.infrastructure.persistence.model.UserEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@Slf4j
public class UserRepositoryWithJpa implements UserRepository {

    private final EntityManager entityManager;

    public UserRepositoryWithJpa(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void save(UserEntity user) {
        entityManager.merge(user);
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
            log.error(e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public boolean isUserAlreadyExists(String email) {
        return findByEmail(email).isPresent();
    }

    @Override
    public UserId nextIdentify() {
        return UserId.fromUUID(UUID.randomUUID());
    }
}
