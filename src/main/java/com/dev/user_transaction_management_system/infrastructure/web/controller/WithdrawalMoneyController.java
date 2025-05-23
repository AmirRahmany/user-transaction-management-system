package com.dev.user_transaction_management_system.infrastructure.web.controller;

import com.dev.user_transaction_management_system.infrastructure.web.response.HttpResponse;
import com.dev.user_transaction_management_system.use_case.WithdrawingMoney;
import com.dev.user_transaction_management_system.use_case.dto.TransactionReceipt;
import com.dev.user_transaction_management_system.use_case.dto.WithdrawalRequest;
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
public class WithdrawalMoneyController {

    private final WithdrawingMoney withdrawingMoney;

    public WithdrawalMoneyController(WithdrawingMoney withdrawingMoney) {
        this.withdrawingMoney = withdrawingMoney;
    }

    @PostMapping("/withdraw")
    public ResponseEntity<HttpResponse> withdraw(@RequestBody WithdrawalRequest withdrawalRequest) {
        try {
            final TransactionReceipt receipt = withdrawingMoney.withdraw(withdrawalRequest);
            final HttpResponse response = HttpResponse.builder().timestamps(LocalDateTime.now().toString()).
                    data(receipt)
                    .status(HttpStatus.OK)
                    .statusCode(HttpStatus.OK.value())
                    .message("Withdrawal successful")
                    .build();
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            log.error("Withdrawal failed{}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
}
