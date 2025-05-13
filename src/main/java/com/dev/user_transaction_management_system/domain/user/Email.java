package com.dev.user_transaction_management_system.domain.user;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.util.Assert;

@EqualsAndHashCode
@ToString
public class Email {
    private static final String PATTERN = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
    private final String value;

    private Email(String email) {
        Assert.hasText(email,"email cannot be null or empty");

        if (isValid(email)) throw new IllegalArgumentException();

        this.value = email;
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
