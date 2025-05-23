package com.dev.user_transaction_management_system.domain.user;

import com.dev.user_transaction_management_system.domain.Date;
import com.dev.user_transaction_management_system.domain.event.NotifiableEvent;
import com.dev.user_transaction_management_system.domain.exceptions.CouldNotOpenAnAccount;
import com.dev.user_transaction_management_system.domain.exceptions.CouldNotActivateUserAccount;
import com.dev.user_transaction_management_system.infrastructure.persistence.model.UserEntity;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class User {

    private final UserId userId;
    private final FullName fullName;
    private final PhoneNumber phoneNumber;
    private final Credential credential;
    private final Date createdAt;
    private UserStatus status;
    private final List<NotifiableEvent> events;

    private User(UserId userId,
                 FullName fullName,
                 PhoneNumber phoneNumber,
                 Credential credential,
                 Date createdAt,
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

    public static User of(UserId userId,
                          FullName fullName,
                          PhoneNumber phoneNumber,
                          Credential credential,
                          UserStatus status,
                          Date date) {
        return new User(userId, fullName, phoneNumber, credential, date, status);
    }

    public void enabled() {
        this.ensureUserIsDisabled();

        this.status = UserStatus.ENABLE;
        this.events.add(new UserAccountWasActivated(fullName.asString(), credential.email().asString(), phoneNumber.asString()));
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

    public void ensureUserIsEnabled() {
        if (isDisable())
            throw CouldNotOpenAnAccount.withDisableUser();
    }

    private void ensureUserIsDisabled() {
        if (isEnabled())
            throw CouldNotActivateUserAccount.becauseUserAccountIsAlreadyActivated();
    }

    public String fullName() {
        return fullName.asString();
    }

    public List<NotifiableEvent> releaseEvents() {
        return events;
    }

    public Email email() {
        return credential.email();
    }

    public String getPassword() {
        return credential.password();
    }

    public String phoneNumber(){
        return phoneNumber.asString();
    }

    public UserEntity toEntity() {
        final UserEntity entity = new UserEntity();
        entity.setId(userId.asString());
        entity.setFirstName(fullName.firstName());
        entity.setLastName(fullName.lastName());
        entity.setEmail(credential.email().asString());
        entity.setPassword(credential.password());
        entity.setPhoneNumber(phoneNumber.asString());
        entity.setUserStatus(status);
        entity.setCreatedAt(createdAt.asLocalDateTime());
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
}
