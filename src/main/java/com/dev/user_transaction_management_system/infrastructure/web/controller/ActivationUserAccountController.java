package com.dev.user_transaction_management_system.infrastructure.web.controller;


import com.dev.user_transaction_management_system.use_case.ActivatingUserAccount;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class ActivationUserAccountController {

    private final ActivatingUserAccount activatingUserAccount;

    public ActivationUserAccountController(ActivatingUserAccount activatingUserAccount) {
        this.activatingUserAccount = activatingUserAccount;
    }

    @PostMapping("/activation")
    public ResponseEntity<?> activate(@RequestBody int userId){
        try {
            activatingUserAccount.activate(userId);
            return ResponseEntity.ok().build();
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
