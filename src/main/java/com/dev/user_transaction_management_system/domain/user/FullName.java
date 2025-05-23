package com.dev.user_transaction_management_system.domain.user;

public record FullName(String firstName, String lastName) {

    public static FullName of(String firstName, String lastName) {
        return new FullName(firstName, lastName);
    }

    public String asString() {
        return firstName + " " + lastName;
    }
}
