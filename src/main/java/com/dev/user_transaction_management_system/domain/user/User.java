package com.dev.user_transaction_management_system.domain.user;

import com.dev.user_transaction_management_system.infrastructure.persistence.model.UserEntity;
import com.dev.user_transaction_management_system.infrastructure.util.Precondition;

import java.time.LocalDateTime;
import java.util.Objects;

public class User {

    private final UserId userId;
    private final FullName fullName;
    private final PhoneNumber phoneNumber;
    private final Credential credential;
    private final LocalDateTime createdAt;
    private UserStatus status;

    private User(UserId userId,
                 FullName fullName,
                 PhoneNumber phoneNumber,
                 Credential credential,
                 LocalDateTime createdAt,
                 UserStatus status) {

        Precondition.require(userId != null);
        Precondition.require(fullName != null);
        Precondition.require(phoneNumber != null);
        Precondition.require(createdAt != null);
        Precondition.require(status != null);

        this.userId = userId;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.credential = credential;
        this.createdAt = createdAt;
        this.status = status;

    }

    public static User of(
            UserId userId,
            FullName fullName,
            PhoneNumber phoneNumber,
            Credential credential,
            UserStatus status) {

        return new User(userId, fullName, phoneNumber, credential, LocalDateTime.now(), status);
    }

    public static User of(UserId userId,FullName fullName, PhoneNumber phoneNumber, Credential credential) {
        return new User(userId, fullName, phoneNumber, credential, LocalDateTime.now(), UserStatus.DISABLE);
    }


    public String email() {
        return credential.email();
    }

    public String firstName() {
        return fullName.firstName();
    }

    public String lastName() {
        return fullName.lastName();
    }

    public String phoneNumber() {
        return phoneNumber.value();
    }

    public String password() {
        return credential.password();
    }

    public LocalDateTime createdAt() {
        return createdAt;
    }


    public void enabled() {
        this.status = UserStatus.ENABLE;
    }

    public void disable() {
        this.status = UserStatus.DISABLE;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return status == user.status && Objects.equals(fullName, user.fullName) &&
                Objects.equals(phoneNumber, user.phoneNumber) &&
                Objects.equals(credential, user.credential) &&
                Objects.equals(createdAt, user.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fullName, phoneNumber, credential, createdAt, status);
    }

    public UserEntity toEntity() {
        return new UserEntity(fullName.firstName(),
                fullName.lastName(),
                phoneNumber.value(),
                email(),
                password(),
                createdAt, status);
    }

    public UserStatus status() {
        return status;
    }

    public boolean isEnabled() {
        return status.equals(UserStatus.ENABLE);
    }

    public boolean isDisable() {
        return !isEnabled();
    }

    public String fullName() {
        return fullName.toString();
    }
}
