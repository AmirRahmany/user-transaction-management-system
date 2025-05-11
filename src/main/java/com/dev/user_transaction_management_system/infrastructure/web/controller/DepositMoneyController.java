package com.dev.user_transaction_management_system.infrastructure.web.controller;

import com.dev.user_transaction_management_system.domain.transaction.Transaction;
import com.dev.user_transaction_management_system.use_case.DepositingMoney;
import com.dev.user_transaction_management_system.use_case.WithdrawingMoney;
import com.dev.user_transaction_management_system.use_case.dto.DepositReceipt;
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
    public ResponseEntity<?> deposit(@RequestBody DepositRequest depositRequest) {
        try {
            final DepositReceipt receipt = depositingMoney.deposit(depositRequest);
            return ResponseEntity.ok(receipt);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
