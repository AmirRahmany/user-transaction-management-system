package com.dev.user_transaction_management_system.fake;

import com.dev.user_transaction_management_system.domain.user.*;
import com.dev.user_transaction_management_system.dto.UserRegistrationRequest;

public class UserFake {

    public static final String BLANK = " ";
    private String firstName;
    private String lastName;
    private String email;
    private String plainPassword;
    private String phoneNumber;


    private UserFake(String firstName,
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

    private UserFake() {
    }

    public static UserFake user() {
        return new UserFake().getUser();
    }

    public UserFake getUser() {
        return new UserFake("amir",
                "rahmani",
                "amir@gmail.com",
                "abcD1234#",
                "09907994339");
    }

    public UserFake withFirstName(String firstName) {
        this.firstName = firstName;

        return this;
    }

    public UserFake withLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public UserFake withEmail(String email) {
        this.email = email;

        return this;
    }

    public UserFake withPassword(String password) {
        this.plainPassword = password;

        return this;
    }

    public UserFake withPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;

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


    public User withNullPassword() {
        return user().withPassword(null).build();
    }

    public User withEmptyPassword() {
        return user().withPassword(BLANK).build();
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
