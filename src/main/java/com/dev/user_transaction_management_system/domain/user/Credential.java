package com.dev.user_transaction_management_system.domain.user;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode
@ToString

public class Credential {
    private final Password password;
    private final Email email;

    private Credential(Email email, Password password) {
        if (email == null || password == null) {
            throw new IllegalArgumentException("email or password must not be null");
        }
        this.password = password;
        this.email = email;
    }

    public static Credential of(Email email, Password password) {
        return new Credential(email, password);
    }

    public String password() {
        return password.toString();
    }

    public String email() {
        return email.value();
    }
}
