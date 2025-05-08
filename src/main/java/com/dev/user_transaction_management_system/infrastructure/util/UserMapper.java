package com.dev.user_transaction_management_system.infrastructure.util;

import com.dev.user_transaction_management_system.domain.user.*;
import com.dev.user_transaction_management_system.use_case.dto.UserRegistrationRequest;
import com.dev.user_transaction_management_system.infrastructure.persistence.model.UserEntity;

public class UserMapper {

    public UserEntity toEntity(User user, String hashedPassword) {
        return new UserEntity(
                user.firstName(),
                user.lastName(),
                user.phoneNumber(),
                user.email(),
                hashedPassword,
                user.createdAt(),
                user.status());
    }

    public User toDomain(UserRegistrationRequest userRegistrationRequest) {
        final FullName fullName = FullName.of(userRegistrationRequest.firstName(), userRegistrationRequest.lastName());
        final PhoneNumber phoneNumber = PhoneNumber.of(userRegistrationRequest.phoneNumber());
        final Credential credential = Credential.of(Email.of(userRegistrationRequest.email()),
                Password.of(userRegistrationRequest.password()));

        return User.of(UserId.autoGenerateByDb(), fullName, phoneNumber, credential);
    }

    public User toDomain(UserEntity userEntity) {
        final UserId userId = UserId.fromInt(userEntity.getId());
        final FullName fullName = FullName.of(userEntity.getFirstName(), userEntity.getLastName());
        final PhoneNumber phoneNumber = PhoneNumber.of(userEntity.getPhoneNumber());
        final Email email = Email.of(userEntity.getEmail());
        final Password password = Password.of(userEntity.getPassword());
        final Credential credential = Credential.of(email, password);

        return User.of(userId, fullName, phoneNumber, credential, userEntity.getUserStatus());
    }
}
