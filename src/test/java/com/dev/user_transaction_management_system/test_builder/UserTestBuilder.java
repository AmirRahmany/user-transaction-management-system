package com.dev.user_transaction_management_system.test_builder;

import com.dev.user_transaction_management_system.domain.Date;
import com.dev.user_transaction_management_system.domain.user.*;
import com.dev.user_transaction_management_system.fake.FakeClock;
import com.dev.user_transaction_management_system.use_case.register_user_account.UserRegistrationRequest;

public class UserTestBuilder {

    public static final String BLANK = " ";

    private String userId = "8c5148ea-857b-4996-a09c-5a5131a33564";
    private String firstName = "sara";
    private String lastName = "bahrami";
    private String email = "jacid20853@besibali.com";
    private String plainPassword = "@abcD1234#";
    private String phoneNumber =  "09101456585";
    private UserStatus userStatus = UserStatus.DISABLE;
    private final Date createdAt = Date.fromCurrentTime((new FakeClock()).currentTime());


    private UserTestBuilder() {
    }

    public static UserTestBuilder aUser() {
        return new UserTestBuilder().getUser();
    }

    private UserTestBuilder getUser() {
        return this;
    }

    public UserTestBuilder withFirstName(String firstName) {
        this.firstName = firstName;

        return this;
    }

    public UserTestBuilder withLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public UserTestBuilder withEmail(String email) {
        this.email = email;

        return this;
    }

    public UserTestBuilder withPassword(String password) {
        this.plainPassword = password;

        return this;
    }

    public UserTestBuilder withPhoneNumber(String phoneNumber) {
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

    public UserTestBuilder withDisabledStatus() {
        this.userStatus = UserStatus.DISABLE;
        return this;
    }

    public UserTestBuilder withEnabledStatus() {
        this.userStatus = UserStatus.ENABLE;
        return this;
    }

    public User build() {
        final UserId id = UserId.fromString(userId);
        final FullName fullName = FullName.of(firstName, lastName);
        final Email mail = Email.of(this.email);
        final PhoneNumber phone = PhoneNumber.of(phoneNumber);
        final Password password = Password.fromPlainPassword(plainPassword);
        final Credential credential = Credential.of(mail, password);

        return User.of(id, fullName, phone, credential, userStatus,createdAt);
    }

    public UserRegistrationRequest buildDTO() {
        return new UserRegistrationRequest(firstName, lastName, email, plainPassword, phoneNumber);
    }

}
