package com.dev.user_transaction_management_system.infrastructure.web.controller;

import com.dev.user_transaction_management_system.infrastructure.web.response.HttpResponse;
import com.dev.user_transaction_management_system.use_case.ActivatingBankAccount;
import com.dev.user_transaction_management_system.use_case.dto.BankAccountActivationRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/account")
@Slf4j
public class ActivationBankAccountController {
    private final ActivatingBankAccount activatingBankAccount;

    public ActivationBankAccountController(ActivatingBankAccount activatingBankAccount) {
        this.activatingBankAccount = activatingBankAccount;
    }

    @PostMapping("/activate")
    public ResponseEntity<HttpResponse> activate(@RequestBody BankAccountActivationRequest request) {
        try {
            activatingBankAccount.activate(request.accountNumber());
            return ResponseEntity.ok().body(HttpResponse.builder()
                    .timestamps(LocalDateTime.now().toString())
                    .status(HttpStatus.OK)
                    .statusCode(HttpStatus.OK.value())
                    .message("bank account activated").build());
        } catch (Exception e) {
            log.error("ActivationBankAccountController: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
}
