package com.dev.user_transaction_management_system.domain.user;

import com.dev.user_transaction_management_system.domain.Event;
import com.dev.user_transaction_management_system.domain.exceptions.CouldNotOpenAnAccount;
import com.dev.user_transaction_management_system.infrastructure.persistence.model.UserEntity;
import com.dev.user_transaction_management_system.use_case.event.UserAccountActivated;
import org.springframework.util.Assert;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class User {

    private final UserId userId;
    private final FullName fullName;
    private final PhoneNumber phoneNumber;
    private final Credential credential;
    private final LocalDateTime createdAt;
    private UserStatus status;
    private List<Event> events;

    private User(UserId userId,
                 FullName fullName,
                 PhoneNumber phoneNumber,
                 Credential credential,
                 LocalDateTime createdAt,
                 UserStatus status) {

        Assert.notNull(userId, "userId cannot be null");
        Assert.notNull(fullName,"fullName cannot be null");
        Assert.notNull(phoneNumber,"phoneNumber cannot be null");
        Assert.notNull(createdAt,"createdAt cannot be null");
        Assert.notNull(status,"user status cannot be null");

        this.userId = userId;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.credential = credential;
        this.createdAt = createdAt;
        this.status = status;
        this.events = new ArrayList<>();
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

    public void enabled() {
        this.status = UserStatus.ENABLE;
        this.events.add(new UserAccountActivated(fullName.asString(), credential.email(), phoneNumber.asString()));
    }

    public void disable() {
        this.status = UserStatus.DISABLE;
    }

    public boolean isEnabled() {
        return status.equals(UserStatus.ENABLE);
    }

    public boolean isDisable() {
        return !isEnabled();
    }

    public String fullName() {
        return fullName.asString();
    }

    public String userId() {
        return userId.asString();
    }

    public void ensureUserIsEnabled() {
        if (isDisable())
            throw CouldNotOpenAnAccount.withDisableUser();
    }

    public UserEntity toEntity() {
        final UserEntity entity = new UserEntity();
        entity.setId(userId.asString());
        entity.setFirstName(fullName.firstName());
        entity.setLastName(fullName.lastName());
        entity.setEmail(credential.email());
        entity.setPassword(credential.password());
        entity.setPhoneNumber(phoneNumber.asString());
        entity.setUserStatus(status);
        entity.setCreatedAt(createdAt);
        return entity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(userId, user.userId) && Objects.equals(fullName, user.fullName)
                && Objects.equals(phoneNumber, user.phoneNumber) && Objects.equals(credential, user.credential)
                && Objects.equals(createdAt, user.createdAt) && status == user.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, fullName, phoneNumber, credential, createdAt, status);
    }

    public List<Event> recordEvents() {
        return events;
    }

    public String getUserName() {
        return credential.email();
    }

    public String getPassword() {
        return credential.password();
    }
}
