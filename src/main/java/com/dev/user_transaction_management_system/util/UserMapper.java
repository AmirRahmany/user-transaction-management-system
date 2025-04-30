package com.dev.user_transaction_management_system.util;

import com.dev.user_transaction_management_system.domain.user.*;
import com.dev.user_transaction_management_system.dto.UserInformation;
import com.dev.user_transaction_management_system.model.UserEntity;

import java.time.LocalDateTime;

import static java.time.LocalDateTime.now;

public class UserMapper {

    public UserEntity toEntity(User user,int userId){
        return new UserEntity(
                userId,
                user.firstName(),
                user.lastName(),
                user.phoneNumber(),
                user.email(),
                user.password(),
                user.createdAt(),
                user.isActive());
    }

    public User toDomain(UserEntity userEntity){
        final FullName fullName = FullName.of(userEntity.firstName(), userEntity.lastName());
        final PhoneNumber phoneNumber = PhoneNumber.of(userEntity.phoneNumber());
        final Credential credential = Credential.of(Email.of(userEntity.email()), Password.of(userEntity.password()));
        return User.of(fullName,phoneNumber,credential);
    }

    public User toDomain(UserInformation userInformation){
        final FullName fullName = FullName.of(userInformation.firstName(), userInformation.lastName());
        final PhoneNumber phoneNumber = PhoneNumber.of(userInformation.phoneNumber());
        final Credential credential = Credential.of(Email.of(userInformation.email()), Password.of(userInformation.password()));
        return User.of(fullName,phoneNumber,credential);
    }
}
