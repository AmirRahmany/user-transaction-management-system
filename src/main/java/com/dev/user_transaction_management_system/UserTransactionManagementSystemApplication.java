package com.dev.user_transaction_management_system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class UserTransactionManagementSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserTransactionManagementSystemApplication.class, args);
    }



}
