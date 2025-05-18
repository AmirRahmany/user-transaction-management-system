package com.dev.user_transaction_management_system.infrastructure.persistence.repository;

import com.dev.user_transaction_management_system.domain.user.UserId;
import com.dev.user_transaction_management_system.domain.user.UserRepository;
import com.dev.user_transaction_management_system.infrastructure.persistence.model.UserEntity;
import io.jsonwebtoken.lang.Assert;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class UserRepositoryWithJpa implements UserRepository {

    private final EntityManager entityManager;
    private final UUIDIdentifierGenerator identifierGenerator;

    public UserRepositoryWithJpa(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.identifierGenerator = new UUIDIdentifierGenerator();
    }

    @Override
    public void save(UserEntity user) {
       entityManager.merge(user);
    }

    @Override
    public Optional<UserEntity> findById(UserId userId) {
        Assert.notNull(userId);

        final UserEntity userEntity = entityManager.find(UserEntity.class, userId.asString());
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

    @Override
    public UUID nextIdentify() {
        return identifierGenerator.generate();
    }
}
