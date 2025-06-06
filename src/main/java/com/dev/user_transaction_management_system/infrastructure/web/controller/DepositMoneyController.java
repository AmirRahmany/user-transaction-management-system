package com.dev.user_transaction_management_system.infrastructure.web.controller;

import com.dev.user_transaction_management_system.infrastructure.web.response.HttpResponse;
import com.dev.user_transaction_management_system.use_case.deposit_money.DepositMoney;
import com.dev.user_transaction_management_system.use_case.deposit_money.TransactionReceipt;
import com.dev.user_transaction_management_system.use_case.deposit_money.DepositRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/transaction")
@Slf4j
public class DepositMoneyController {

    private final DepositMoney depositMoney;

    public DepositMoneyController(DepositMoney depositMoney) {
        this.depositMoney = depositMoney;
    }

    @PostMapping("/deposit")
    public ResponseEntity<HttpResponse> deposit(@RequestBody DepositRequest request) {
        try {
            final TransactionReceipt transactionReceipt = depositMoney.deposit(request);
            final HttpResponse response = HttpResponse.builder().timestamps(LocalDateTime.now().toString()).
                    data( transactionReceipt)
                    .status(HttpStatus.OK)
                    .message("Deposit successful")
                    .statusCode(HttpStatus.OK.value())
                    .build();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Deposit transaction failed{}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
}
