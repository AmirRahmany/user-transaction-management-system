package com.dev.user_transaction_management_system.infrastructure.util.mapper;

import com.dev.user_transaction_management_system.domain.Date;
import com.dev.user_transaction_management_system.domain.user.*;
import com.dev.user_transaction_management_system.infrastructure.persistence.model.UserEntity;
import jakarta.persistence.Column;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UserMapper {

    public User toDomain(UserEntity entity) {
        final UserId userId = UserId.fromUUID(UUID.fromString(entity.getId()));
        final FullName fullName = FullName.of(entity.getFirstName(), entity.getLastName());
        final PhoneNumber phoneNumber = PhoneNumber.of(entity.getPhoneNumber());
        final Email email = Email.of(entity.getEmail());
        final Password password = Password.fromHashedPassword(entity.getPassword());
        final Credential credential = Credential.of(email, password);

        return User.of(userId,
                fullName,
                phoneNumber,
                credential,
                entity.getUserStatus(),
                Date.fromLocalDateTime(entity.getCreatedAt()));
    }
}