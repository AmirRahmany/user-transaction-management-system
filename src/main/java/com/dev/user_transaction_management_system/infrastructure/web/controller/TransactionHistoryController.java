package com.dev.user_transaction_management_system.infrastructure.web.controller;


import com.dev.user_transaction_management_system.use_case.ViewTransactionHistory;
import com.dev.user_transaction_management_system.use_case.dto.TransactionHistory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transaction")
public class TransactionHistoryController {

    private final ViewTransactionHistory transactionHistory;

    public TransactionHistoryController(ViewTransactionHistory transactionHistory) {
        this.transactionHistory = transactionHistory;
    }

    @GetMapping("/history/{accountNumber}")
    public ResponseEntity<?> viewHistory(@PathVariable String accountNumber){
        try{
            final List<TransactionHistory> histories = transactionHistory.getHistoryByAccountNumber(accountNumber);
            return ResponseEntity.ok(histories);
        }catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }
}
