package com.dev.user_transaction_management_system.controller;

import com.dev.user_transaction_management_system.application.UserRegistration;
import com.dev.user_transaction_management_system.dto.UserInformation;
import com.dev.user_transaction_management_system.util.UserMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class UserController {

    private final UserRegistration userRegistration;
    private final UserMapper userMapper;

    public UserController(UserRegistration userRegistration) {
        this.userRegistration = userRegistration;
        this.userMapper = new UserMapper();
    }

    @PostMapping("/register")
    public ResponseEntity<UserInformation> register(@RequestBody UserInformation userInformation) {
        try {
            userRegistration.register(userMapper.toDomain(userInformation));
            return ResponseEntity.ok(userInformation);
        } catch (RuntimeException exception) {
            return new ResponseEntity<>(HttpStatusCode.valueOf(HttpStatus.BAD_REQUEST.value()));
        }
    }
}
