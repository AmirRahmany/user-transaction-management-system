package com.dev.user_transaction_management_system.domain.user;

public class Password {
    private final static String PATTERN =
            "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+=\\-`~\\[\\]{}|;':\",./<>?]).{8,}$";
    private final String password;


    private Password(String password) {
        if (password == null || password.isBlank() || isValid(password))
            throw new IllegalArgumentException();

        this.password = password;
    }


    public static Password of(String password){
        return new Password(password);
    }

    public boolean isValid(String password) {
        return !password.matches(PATTERN);
    }

    public String toString() {
        return password;
    }
}
