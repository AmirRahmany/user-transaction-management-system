package com.dev.user_transaction_management_system.fake;

import com.dev.user_transaction_management_system.domain.user.*;
import com.dev.user_transaction_management_system.use_case.dto.UserRegistrationRequest;

public class UserFakeBuilder {

    public static final String BLANK = " ";

    private Integer userId = 0;
    private String firstName;
    private String lastName;
    private String email;
    private String plainPassword;
    private String phoneNumber;
    private UserStatus userStatus = UserStatus.DISABLE;


    private UserFakeBuilder(String firstName,
                            String lastName,
                            String email,
                            String password,
                            String phoneNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.plainPassword = password;
        this.phoneNumber = phoneNumber;
    }

    private UserFakeBuilder() {
    }

    public static UserFakeBuilder aUser() {
        return new UserFakeBuilder().getUser();
    }

    private UserFakeBuilder getUser() {
        return new UserFakeBuilder("amir",
                "rahmani",
                "amir@gmail.com",
                "abcD1234#",
                "09907994339");
    }

    public UserFakeBuilder withUSerId(Integer userId) {
        this.userId = userId;
        return this;
    }

    public UserFakeBuilder withFirstName(String firstName) {
        this.firstName = firstName;

        return this;
    }

    public UserFakeBuilder withLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public UserFakeBuilder withEmail(String email) {
        this.email = email;

        return this;
    }

    public UserFakeBuilder withPassword(String password) {
        this.plainPassword = password;

        return this;
    }

    public UserFakeBuilder withPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;

        return this;
    }


    public UserRegistrationRequest withNullPhoneNumber() {
        return getUser().withPhoneNumber(null).buildDTO();
    }

    public UserRegistrationRequest withBlankPhoneNumber() {
        return getUser().withPhoneNumber(BLANK).buildDTO();
    }

    public UserRegistrationRequest withNullEmail() {
        return getUser().withEmail(null).buildDTO();
    }

    public UserRegistrationRequest withBlankEmail() {
        return getUser().withEmail(BLANK).buildDTO();
    }

    public UserRegistrationRequest withInvalidEmail() {
        return getUser().withEmail("jjsgjiojtjijtij.com").buildDTO();
    }


    public UserRegistrationRequest withNullPassword() {
        return aUser().withPassword(null).buildDTO();
    }

    public UserRegistrationRequest withEmptyPassword() {
        return aUser().withPassword(BLANK).buildDTO();
    }

    public UserFakeBuilder withDisabledStatus() {
        this.userStatus = UserStatus.DISABLE;
        return this;
    }

    public UserFakeBuilder withEnabledStatus() {
        this.userStatus = UserStatus.ENABLE;
        return this;
    }

    public User build() {
        final UserId id = UserId.fromInt(userId);
        final FullName fullName = FullName.of(firstName, lastName);
        final Email mail = Email.of(this.email);
        final PhoneNumber phone = PhoneNumber.of(phoneNumber);
        final Password password = Password.of(plainPassword);
        final Credential credential = Credential.of(mail, password);

        return User.of(id, fullName, phone, credential, userStatus);
    }

    public UserRegistrationRequest buildDTO() {
        return new UserRegistrationRequest(firstName, lastName, email, plainPassword, phoneNumber);
    }

}
