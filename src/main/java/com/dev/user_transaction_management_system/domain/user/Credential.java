package com.dev.user_transaction_management_system.domain.user;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.util.Assert;

@EqualsAndHashCode
@ToString

public class Credential {
    private final Password password;
    private final Email email;

    private Credential(Email email, Password password) {
        Assert.notNull(email,"email cannot be null");
        Assert.notNull(password,"password cannot be null");

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
