package com.dev.user_transaction_management_system.use_case.register_user_account;

public record UserRegistrationRequest(
        String firstName,
        String lastName,
        String email,
        String password,
        String phoneNumber){
}
