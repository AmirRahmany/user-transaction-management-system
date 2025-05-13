package com.dev.user_transaction_management_system.domain.user;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.util.Assert;

@EqualsAndHashCode
@ToString
public final class FullName {
    private final String firstName;
    private final String lastName;

    private FullName(String firstName, String lastName) {
        Assert.hasText(firstName,"firstName cannot be null or empty");
        Assert.hasText(lastName,"lastName cannot be null or empty");

        this.firstName = firstName;
        this.lastName = lastName;
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
