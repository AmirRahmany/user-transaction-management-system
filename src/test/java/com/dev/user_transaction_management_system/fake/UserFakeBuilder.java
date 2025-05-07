package com.dev.user_transaction_management_system.fake;

import com.dev.user_transaction_management_system.domain.user.*;
import com.dev.user_transaction_management_system.use_case.dto.UserRegistrationRequest;

public class UserFakeBuilder {

    public static final String BLANK = " ";
    private String firstName;
    private String lastName;
    private String email;
    private String plainPassword;
    private String phoneNumber;


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


    public UserFakeBuilder withNullPhoneNumber() {
        this.phoneNumber = null;
        return this;
    }

    public UserFakeBuilder withBlankPhoneNumber() {
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


    public User withNullPassword() {
        return aUser().withPassword(null).build();
    }

    public User withEmptyPassword() {
        return aUser().withPassword(BLANK).build();
    }

    public User build() {
        final FullName fullName = FullName.of(firstName, lastName);
        final Email mail = Email.of(this.email);
        final PhoneNumber phone = PhoneNumber.of(phoneNumber);
        final Password password = Password.of(plainPassword);
        final Credential credential = Credential.of(mail,password);

        return User.of(fullName, phone,credential);
    }

    public UserRegistrationRequest buildDTO() {
        return new UserRegistrationRequest(firstName, lastName,email,plainPassword,phoneNumber);
    }


}
