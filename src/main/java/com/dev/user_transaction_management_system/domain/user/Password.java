package com.dev.user_transaction_management_system.domain.user;

import lombok.EqualsAndHashCode;
import org.springframework.util.Assert;

@EqualsAndHashCode
public class Password {
    private static final String PATTERN =
            "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+=\\-`~\\[\\]{}|;':\",./<>?]).{8,}$";
    private final String plainPassword;


    private Password(String password) {
        Assert.hasText(password,"password cannot be null or empty");

        if (isValid(password))
            throw new IllegalArgumentException();

        this.plainPassword = password;
    }


    public static Password of(String password) {
        return new Password(password);
    }

    public boolean isValid(String password) {
        return !password.matches(PATTERN);
    }

    public String toString() {
        return plainPassword;
    }
}
