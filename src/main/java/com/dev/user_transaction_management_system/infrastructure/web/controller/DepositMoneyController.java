package com.dev.user_transaction_management_system.infrastructure.web.controller;

import com.dev.user_transaction_management_system.infrastructure.web.response.HttpResponse;
import com.dev.user_transaction_management_system.use_case.DepositingMoney;
import com.dev.user_transaction_management_system.use_case.dto.TransactionReceipt;
import com.dev.user_transaction_management_system.use_case.dto.DepositRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/transaction")
@Slf4j
public class DepositMoneyController {

    private final DepositingMoney depositingMoney;

    public DepositMoneyController(DepositingMoney depositingMoney) {
        this.depositingMoney = depositingMoney;
    }

    @PostMapping("/deposit")
    public ResponseEntity<HttpResponse> deposit(@RequestBody DepositRequest request) {
        try {
            final TransactionReceipt transactionReceipt = depositingMoney.deposit(request);
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
