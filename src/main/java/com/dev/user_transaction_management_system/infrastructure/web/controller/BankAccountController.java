package com.dev.user_transaction_management_system.infrastructure.web.controller;

import com.dev.user_transaction_management_system.use_case.OpeningBankAccount;
import com.dev.user_transaction_management_system.use_case.dto.AccountRequest;
import com.dev.user_transaction_management_system.use_case.dto.OpeningAccountResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class BankAccountController {

    private final OpeningBankAccount openingBankAccount;

    @Autowired
    public BankAccountController(OpeningBankAccount openingBankAccount) {
        this.openingBankAccount = openingBankAccount;
    }

    @PostMapping("/account")
    public ResponseEntity<?> open(@RequestBody AccountRequest accountRequest) {
        try {
            final OpeningAccountResponse openingAccountResponse = openingBankAccount.open(accountRequest);
            return ResponseEntity.ok(openingAccountResponse);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
