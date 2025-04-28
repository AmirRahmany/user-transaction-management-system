package com.dev.user_transaction_management_system.domain.user;

public class PhoneNumber {
    private final String value;

    private PhoneNumber(String phoneNumber) {
        if (isNull(phoneNumber) || phoneNumber.isBlank()) throw new IllegalArgumentException();
        this.value = phoneNumber;
    }

    private static boolean isNull(String field) {
        return field == null;
    }

    public static PhoneNumber of(String phoneNumber) {
        return new PhoneNumber(phoneNumber);
    }

    public String value() {
        return value;
    }
}
