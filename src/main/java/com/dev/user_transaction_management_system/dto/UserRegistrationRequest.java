package com.dev.user_transaction_management_system.dto;

public record UserRegistrationRequest(
        String firstName,
        String lastName,
        String email,
        String password,
        String phoneNumber){
}
