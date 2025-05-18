package com.dev.user_transaction_management_system.fake;

import org.springframework.security.crypto.password.PasswordEncoder;

public class PasswordEncoderStub implements PasswordEncoder {
    @Override
    public String encode(CharSequence rawPassword) {
        return "hashedPassword";
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return false;
    }
}
