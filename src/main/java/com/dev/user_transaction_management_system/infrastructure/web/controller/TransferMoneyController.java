package com.dev.user_transaction_management_system.infrastructure.web.controller;

import com.dev.user_transaction_management_system.use_case.transfer_money.TransferMoney;
import com.dev.user_transaction_management_system.use_case.transfer_money.TransferMoneyRequest;
import com.dev.user_transaction_management_system.use_case.transfer_money.TransferReceipt;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/transaction")
public class TransferMoneyController {

    // TODO this use case needs to refactor
    private final TransferMoney transferMoney;

    public TransferMoneyController(TransferMoney transferMoney) {
        this.transferMoney = transferMoney;
    }

    @PostMapping("/transfer")
    public ResponseEntity<?> transfer(@RequestBody TransferMoneyRequest request) {
        try {
            final TransferReceipt receipt = transferMoney.transfer(request);
            return ResponseEntity.ok(receipt);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
