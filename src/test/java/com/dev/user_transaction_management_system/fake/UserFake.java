package com.dev.user_transaction_management_system.fake;

import com.dev.user_transaction_management_system.domain.user.*;

import java.time.LocalDateTime;

public class UserFake {

    public static final String BLANK = " ";
    private String firstName;
    private String lastName;
    private String email;
    private String plainPassword;
    private String phoneNumber;
    private LocalDateTime createdAt;
    private boolean isActive;


    private UserFake(String firstName,
                     String lastName,
                     String email,
                     String password,
                     String phoneNumber,
                     LocalDateTime createdAt,
                     boolean isActive) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.plainPassword = password;
        this.phoneNumber = phoneNumber;
        this.createdAt = createdAt;
        this.isActive = isActive;
    }

    private UserFake() {
    }

    public static UserFake user() {
        return new UserFake().getUser();
    }

    public UserFake getUser() {
        return new UserFake("amir", "rahmani", "amir@gmail.com", "abcD1234#",
                "09907994339", LocalDateTime.now(), false);
    }

    public UserFake withFirstName(String firstName) {
        this.firstName = firstName;

        return this;
    }

    public UserFake lastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public UserFake withEmail(String email) {
        this.email = email;

        return this;
    }

    public UserFake password(String password) {
        this.plainPassword = password;

        return this;
    }

    public UserFake phoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;

        return this;
    }

    public UserFake createAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;

        return this;
    }

    public UserFake active() {
        this.isActive = true;
        return this;
    }

    public UserFake deActive() {
        this.isActive = false;
        return this;
    }

    public UserFake withNullPhoneNumber() {
        this.phoneNumber = null;
        return this;
    }

    public UserFake withBlankPhoneNumber() {
        this.phoneNumber = BLANK;
        return this;
    }

    public User withNullEmail() {
        return getUser().withEmail(null).build();
    }

    public User withBlankEmail() {
        return getUser().withEmail(BLANK).build();
    }

    public User withInvalidEmail() {
        return getUser().withEmail("jjsgjiojtjijtij.com").build();
    }

    public User withNullName() {
        return getUser().withFirstName(null).build();
    }

    public User withNullPassword() {
        return user().password(null).build();
    }

    public User withEmptyPassword() {
        return user().password(BLANK).build();
    }

    public User build() {
        final FullName fullName = new FullName(firstName, lastName);
        final Email mail = Email.of(this.email);
        final PhoneNumber phone = PhoneNumber.of(phoneNumber);
        final Password password = Password.of(plainPassword);
        final Credential credential = Credential.of(mail,password);

        return User.of(fullName, phone,credential, createdAt);
    }
}
