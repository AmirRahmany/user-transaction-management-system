package com.dev.user_transaction_management_system.infrastructure.web.controller;


import com.dev.user_transaction_management_system.infrastructure.web.response.HttpResponse;
import com.dev.user_transaction_management_system.use_case.view_transaction_history.ViewTransactionHistory;
import com.dev.user_transaction_management_system.use_case.view_transaction_history.TransactionHistory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/transaction")
@Slf4j
public class TransactionHistoryController {

    private final ViewTransactionHistory transactionHistory;

    public TransactionHistoryController(ViewTransactionHistory transactionHistory) {
        this.transactionHistory = transactionHistory;
    }

    @GetMapping("/history/{accountNumber}")
    public ResponseEntity<HttpResponse> viewHistory(@PathVariable String accountNumber){
        try{
            final List<TransactionHistory> histories = transactionHistory.getHistoryByAccountNumber(accountNumber);

            final HttpResponse response = HttpResponse.builder().timestamps(LocalDateTime.now().toString()).
                    data(histories)
                    .status(HttpStatus.OK)
                    .statusCode(HttpStatus.OK.value())
                    .build();
            return ResponseEntity.ok(response);
        }catch (Exception e){
            log.error("transaction failed{}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
}
