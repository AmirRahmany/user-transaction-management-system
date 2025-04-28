package com.dev.user_transaction_management_system.domain.user;

import java.util.Objects;

public class Email {
    public static final String PATTERN = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
    private final String email;

    private Email(String email) {
        if (isNull(email) || email.isBlank() || isValid(email)) throw new IllegalArgumentException();

        this.email = email;
    }

    public static Email of(String email){
        return new Email(email);
    }

    public String value(){
        return email;
    }

    private static boolean isValid(String email) {
        return !email.matches(PATTERN);
    }

    private static boolean isNull(String field) {
        return field == null;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Email email1 = (Email) o;
        return Objects.equals(email, email1.email);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(email);
    }


    @Override
    public String toString() {
        return "Email{" +
                "email='" + email + '\'' +
                '}';
    }
}
