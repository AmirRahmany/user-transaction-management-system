package com.dev.user_transaction_management_system.infrastructure.web.controller;

import com.dev.user_transaction_management_system.infrastructure.web.response.HttpResponse;
import com.dev.user_transaction_management_system.use_case.OpeningBankAccount;
import com.dev.user_transaction_management_system.use_case.dto.AccountRequest;
import com.dev.user_transaction_management_system.use_case.dto.OpeningAccountResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class BankAccountController {

    private static final Logger log = LogManager.getLogger(BankAccountController.class);
    private final OpeningBankAccount openingBankAccount;

    @Autowired
    public BankAccountController(OpeningBankAccount openingBankAccount) {
        this.openingBankAccount = openingBankAccount;
    }

    @PostMapping("/account")
    public ResponseEntity<HttpResponse> open(@RequestBody AccountRequest accountRequest) {
        try {
            final OpeningAccountResponse openingAccountResponse = openingBankAccount.open(accountRequest);
            final HttpResponse response = HttpResponse.builder().timestamps(LocalDateTime.now().toString()).
                    data(openingAccountResponse)
                    .message("account opened")
                    .status(HttpStatus.CREATED)
                    .statusCode(HttpStatus.CREATED.value())
                    .build();
            return ResponseEntity.created(new URI("")).body(response);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

}
