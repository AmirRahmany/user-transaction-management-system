package com.dev.user_transaction_management_system.domain.user;

import org.springframework.util.Assert;

public record FullName(String firstName, String lastName) {

    public static FullName of(String firstName, String lastName) {
        Assert.hasText(firstName,"firstname cannot be null or empty");
        Assert.hasText(lastName,"firstname cannot be null or empty");

        return new FullName(firstName, lastName);
    }

    public String asString() {
        return firstName + " " + lastName;
    }
}
