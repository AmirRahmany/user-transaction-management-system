package com.dev.user_transaction_management_system.domain.user;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode
@ToString
public final class FullName {
    private final String firstName;
    private final String lastName;

    private FullName(String firstName, String lastName) {
        if (isValidName(firstName, lastName)) throw new IllegalArgumentException();

        this.firstName = firstName;
        this.lastName = lastName;
    }

    private static boolean isValidName(String firstName, String lastName) {
        return isNull(firstName) || isNull(lastName) || firstName.isBlank() || lastName.isBlank();
    }

    private static boolean isNull(String field) {
        return field == null;
    }

    public static FullName of(String firstName, String lastName) {
        return new FullName(firstName,lastName);
    }

    public String firstName() {
        return firstName;
    }

    public String lastName() {
        return lastName;
    }
}
