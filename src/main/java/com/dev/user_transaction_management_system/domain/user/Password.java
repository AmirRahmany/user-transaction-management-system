package com.dev.user_transaction_management_system.domain.user;

import lombok.EqualsAndHashCode;
import org.springframework.util.Assert;

import java.util.Objects;

@EqualsAndHashCode
public class Password {
    private static final String PASSWORD_VALIDATION_MESSAGE="your password not valid";
    private static final String PATTERN =
            "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+=\\-`~\\[\\]{}|;':\",./<>?]).{8,}$";
    private final String plainPassword;


    private Password(String password) {
        Assert.notNull(password, "password cannot be null");
        Assert.hasText(password, "password cannot be empty");

        this.plainPassword = password;
    }


    public static Password fromHashedPassword(String hashedPassword) {
        return new Password(hashedPassword);
    }

    public static Password fromPlainPassword(String password) {
        final var createdPassword = new Password(password);
        validatePassword(createdPassword.toString());
        return createdPassword;
    }

    private static void validatePassword(String password) {
        if (!isValid(password))
            throw new IllegalArgumentException("password not valid");
    }

    private static boolean isValid(String password) {
        return password.matches(PATTERN);
    }

    public String toString() {
        return plainPassword;
    }

}
