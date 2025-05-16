package com.dev.user_transaction_management_system.infrastructure.web.controller;

import com.dev.user_transaction_management_system.use_case.DepositingMoney;
import com.dev.user_transaction_management_system.use_case.TransferMoney;
import com.dev.user_transaction_management_system.use_case.dto.DepositReceipt;
import com.dev.user_transaction_management_system.use_case.dto.TransferMoneyRequest;
import com.dev.user_transaction_management_system.use_case.dto.TransferReceipt;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/transaction")
public class TransferMoneyController {

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
