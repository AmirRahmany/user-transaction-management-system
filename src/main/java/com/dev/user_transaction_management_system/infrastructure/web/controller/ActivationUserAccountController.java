package com.dev.user_transaction_management_system.infrastructure.web.controller;


import com.dev.user_transaction_management_system.infrastructure.web.response.HttpResponse;
import com.dev.user_transaction_management_system.use_case.ActivatingUserAccount;
import com.dev.user_transaction_management_system.use_case.dto.UserActivationRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class ActivationUserAccountController {

    private final ActivatingUserAccount activatingUserAccount;


    public ActivationUserAccountController(ActivatingUserAccount activatingUserAccount) {
        this.activatingUserAccount = activatingUserAccount;
    }

    @PostMapping("/activate")
    public ResponseEntity<HttpResponse> activate(@RequestBody UserActivationRequest request) {
        try {
            activatingUserAccount.activate(request.username());
            return ResponseEntity.ok().body(HttpResponse.builder()
                    .timestamps(LocalDateTime.now().toString())
                    .status(HttpStatus.OK)
                    .statusCode(HttpStatus.OK.value())
                    .message("user activated").build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
