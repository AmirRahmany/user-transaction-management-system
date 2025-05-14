package com.dev.user_transaction_management_system.domain.user;

import org.springframework.util.Assert;

public class PhoneNumber {
    private final String value;

    private PhoneNumber(String phoneNumber) {
        Assert.hasText(phoneNumber,"phone number cannot be null or empty");

        this.value = phoneNumber;
    }

    public static PhoneNumber of(String phoneNumber) {
        return new PhoneNumber(phoneNumber);
    }

    public String asString() {
        return value;
    }
}
