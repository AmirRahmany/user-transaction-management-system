package com.dev.user_transaction_management_system.infrastructure.web.controller;

import com.dev.user_transaction_management_system.infrastructure.web.response.HttpResponse;
import com.dev.user_transaction_management_system.use_case.activate_bank_account.ActivateBankAccount;
import com.dev.user_transaction_management_system.use_case.activate_bank_account.BankAccountActivationRequest;
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
    private final ActivateBankAccount activateBankAccount;

    public ActivationBankAccountController(ActivateBankAccount activateBankAccount) {
        this.activateBankAccount = activateBankAccount;
    }

    @PostMapping("/activate")
    public ResponseEntity<HttpResponse> activate(@RequestBody BankAccountActivationRequest request) {
        try {
            activateBankAccount.activate(request);
            return ResponseEntity.ok().body(HttpResponse.builder()
                    .timestamps(LocalDateTime.now().toString())
                    .status(HttpStatus.OK)
                    .statusCode(HttpStatus.OK.value())
                    .message("bank account activated").build());
        } catch (IllegalArgumentException e){
           return ResponseEntity.badRequest().body(HttpResponse.builder()
                    .timestamps(LocalDateTime.now().toString())
                    .status(HttpStatus.BAD_REQUEST)
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .message(e.getMessage()).build());
        } catch (Exception e) {
            log.error("ActivationBankAccountController: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
}
