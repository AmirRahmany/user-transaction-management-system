package com.dev.user_transaction_management_system.domain.user;

import org.springframework.util.Assert;

public class PhoneNumber {
    public static final String PHONE_NUMBER_PATTERN = "^(0|98|\\+98)9[*\\d]{9}$";
    private final String value;

    private PhoneNumber(String phoneNumber) {
        Assert.hasText(phoneNumber,"phone number cannot be null or empty");
        ensurePhoneNumberIsValid(phoneNumber);
        this.value = phoneNumber;
    }

    private void ensurePhoneNumberIsValid(String phoneNumber) {
        if (!phoneNumber.matches(PHONE_NUMBER_PATTERN)){
         throw new IllegalArgumentException("phoneNumber is not valid.");
        }
    }

    public static PhoneNumber of(String phoneNumber) {
        return new PhoneNumber(phoneNumber);
    }

    public String asString() {
        return value;
    }
}
