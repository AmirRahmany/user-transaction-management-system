package com.dev.user_transaction_management_system.infrastructure.web.controller;

import com.dev.user_transaction_management_system.use_case.OpeningAccount;
import com.dev.user_transaction_management_system.use_case.dto.AccountRequest;
import com.dev.user_transaction_management_system.use_case.dto.AccountResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AccountController {

    private final OpeningAccount openingAccount;

    @Autowired
    public AccountController(OpeningAccount openingAccount) {
        this.openingAccount = openingAccount;
    }

    @PostMapping("/account")
    public ResponseEntity<?> open(@RequestBody AccountRequest accountRequest) {
        try {
            final AccountResponse accountResponse = openingAccount.open(accountRequest);
            return ResponseEntity.ok(accountResponse);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

}
