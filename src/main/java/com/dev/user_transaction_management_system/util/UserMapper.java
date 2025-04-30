package com.dev.user_transaction_management_system.util;

import com.dev.user_transaction_management_system.domain.user.*;
import com.dev.user_transaction_management_system.dto.UserInformation;
import com.dev.user_transaction_management_system.model.UserEntity;

import static java.time.LocalDateTime.now;

public class UserMapper {

    public UserEntity toEntity(User user, String hashedPassword){
        return new UserEntity(
                user.firstName(),
                user.lastName(),
                user.phoneNumber(),
                user.email(),
                hashedPassword,
                user.createdAt(),
                user.isActive());
    }

    public User toDomain(UserEntity userEntity){
        final FullName fullName = FullName.of(userEntity.getFirstName(), userEntity.getLastName());
        final PhoneNumber phoneNumber = PhoneNumber.of(userEntity.getPhoneNumber());
        final Credential credential = Credential.of(Email.of(userEntity.getEmail()), Password.of(userEntity.getPassword()));
        return User.of(fullName,phoneNumber,credential);
    }

    public User toDomain(UserInformation userInformation){
        final FullName fullName = FullName.of(userInformation.firstName(), userInformation.lastName());
        final PhoneNumber phoneNumber = PhoneNumber.of(userInformation.phoneNumber());
        final Credential credential = Credential.of(Email.of(userInformation.email()),
                Password.of(userInformation.password()));

        return User.of(fullName,phoneNumber,credential);
    }
}
