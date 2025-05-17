package com.dev.user_transaction_management_system.infrastructure.web.controller;

import com.dev.user_transaction_management_system.use_case.DepositingMoney;
import com.dev.user_transaction_management_system.use_case.dto.TransactionReceipt;
import com.dev.user_transaction_management_system.use_case.dto.DepositRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/transaction")
public class DepositMoneyController {

    private final DepositingMoney depositingMoney;

    public DepositMoneyController(DepositingMoney depositingMoney) {
        this.depositingMoney = depositingMoney;
    }

    @PostMapping("/deposit")
    public ResponseEntity<?> deposit(@RequestBody DepositRequest request) {
        try {
            final TransactionReceipt transactionReceipt = depositingMoney.deposit(request);
            return ResponseEntity.ok(transactionReceipt);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
