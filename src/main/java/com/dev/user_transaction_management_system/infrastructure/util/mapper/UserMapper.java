package com.dev.user_transaction_management_system.infrastructure.util.mapper;

import com.dev.user_transaction_management_system.domain.user.*;
import com.dev.user_transaction_management_system.infrastructure.persistence.model.UserEntity;
import jakarta.persistence.Column;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UserMapper {

    public User toDomain(UserEntity userEntity) {
        final UserId userId = UserId.fromUUID(UUID.fromString(userEntity.getId()));
        final FullName fullName = FullName.of(userEntity.getFirstName(), userEntity.getLastName());
        final PhoneNumber phoneNumber = PhoneNumber.of(userEntity.getPhoneNumber());
        final Email email = Email.of(userEntity.getEmail());
        final Password password = Password.fromHashedPassword(userEntity.getPassword());
        final Credential credential = Credential.of(email, password);

        return User.of(userId, fullName, phoneNumber, credential, userEntity.getUserStatus());
    }
}
