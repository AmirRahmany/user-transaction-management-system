package com.dev.user_transaction_management_system.infrastructure.web.controller;

import com.dev.user_transaction_management_system.domain.transaction.Transaction;
import com.dev.user_transaction_management_system.use_case.WithdrawingMoney;
import com.dev.user_transaction_management_system.use_case.dto.WithdrawalRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/transaction")
public class WithdrawalMoneyController {

    private final WithdrawingMoney withdrawingMoney;

    public WithdrawalMoneyController(WithdrawingMoney withdrawingMoney) {
        this.withdrawingMoney = withdrawingMoney;
    }

    @PostMapping("/withdraw")
    public ResponseEntity<?> withdraw(@RequestBody WithdrawalRequest withdrawalRequest) {
        try {
            final Transaction receipt = withdrawingMoney.withdraw(withdrawalRequest);

            return ResponseEntity.ok(withdrawalRequest);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
