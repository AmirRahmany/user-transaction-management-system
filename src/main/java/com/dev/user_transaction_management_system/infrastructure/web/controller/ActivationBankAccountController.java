package com.dev.user_transaction_management_system.infrastructure.web.controller;

import com.dev.user_transaction_management_system.domain.bank_account.AccountNumber;
import com.dev.user_transaction_management_system.use_case.ActivatingBankAccount;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/account")
public class ActivationBankAccountController {
    private final ActivatingBankAccount activatingBankAccount;

    public ActivationBankAccountController(ActivatingBankAccount activatingBankAccount) {
        this.activatingBankAccount = activatingBankAccount;
    }

    @PostMapping("/activate")
    public ResponseEntity<?> activate(@RequestBody AccountNumber accountNumber){
        try {
            activatingBankAccount.activate(accountNumber.toString());
            return ResponseEntity.ok().build();
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
