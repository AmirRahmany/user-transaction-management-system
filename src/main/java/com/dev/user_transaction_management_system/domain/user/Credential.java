package com.dev.user_transaction_management_system.domain.user;

import java.util.Objects;

public class Credential {
    private Password password;
    private Email email;

    private Credential(Email email, Password password) {
        this.password = password;
        this.email = email;
    }

    public static Credential of(Email email,Password password){
        return new Credential(email,password);
    }

    public String password() {
        return password.toString();
    }

    public String email() {
        return email.value();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Credential that = (Credential) o;
        return Objects.equals(password, that.password) && Objects.equals(email, that.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(password, email);
    }
}
