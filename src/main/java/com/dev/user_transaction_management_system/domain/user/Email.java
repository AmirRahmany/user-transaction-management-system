package com.dev.user_transaction_management_system.domain.user;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode
@ToString
public class Email {
    private static final String PATTERN = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
    private final String value;

    private Email(String email) {
        if (isNull(email) || email.isBlank() || isValid(email)) throw new IllegalArgumentException();

        this.value = email;
    }

    private static boolean isNull(String field) {
        return field == null;
    }

    public static Email of(String email){
        return new Email(email);
    }

    private static boolean isValid(String email) {
        return !email.matches(PATTERN);
    }

    public String value(){
        return value;
    }
}
