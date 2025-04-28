package com.dev.user_transaction_management_system.domain.user;

import java.time.LocalDateTime;
import java.util.Objects;

public class User {

    private final FullName fullName;
    private final PhoneNumber phoneNumber;
    private final Credential credential;
    private final LocalDateTime createdAt;
    private final boolean isActive;

    private User(FullName fullName,
                 PhoneNumber phoneNumber,
                 Credential credential,
                 LocalDateTime createdAt,
                 boolean isActive) {

        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.credential = credential;
        this.createdAt = createdAt;
        this.isActive = isActive;

    }

    public static User of(FullName fullName,
                          PhoneNumber phoneNumber,
                          Credential credential,
                          LocalDateTime createdAt) {

        return new User(fullName, phoneNumber, credential, createdAt, false);
    }


    public String email() {
        return credential.email();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return isActive == user.isActive && Objects.equals(fullName, user.fullName) &&
                Objects.equals(phoneNumber, user.phoneNumber) && Objects.equals(credential, user.credential) &&
                Objects.equals(createdAt, user.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fullName, phoneNumber, credential, createdAt, isActive);
    }

    public String firstName() {
        return fullName.firstName();
    }

    public String lastName() {
        return fullName.lastName();
    }

    public String phoneNumber(){
        return phoneNumber.value();
    }

    public String password() {
        return credential.password();
    }

    public LocalDateTime createdAt() {
        return createdAt;
    }

    public boolean isActive() {
        return isActive;
    }
}
