package com.dev.user_transaction_management_system.domain.user;

import java.util.Objects;

public final class FullName {
    private final String firstName;
    private final String lastName;

    public FullName(String firstName, String lastName) {
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

    public String firstName() {
        return firstName;
    }

    public String lastName() {
        return lastName;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (FullName) obj;
        return Objects.equals(this.firstName, that.firstName) &&
                Objects.equals(this.lastName, that.lastName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName);
    }

    @Override
    public String toString() {
        return "FullName[" +
                "firstName=" + firstName + ", " +
                "lastName=" + lastName + ']';
    }

}
