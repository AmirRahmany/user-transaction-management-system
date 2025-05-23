package com.dev.user_transaction_management_system.infrastructure.util.mapper;

import com.dev.user_transaction_management_system.domain.Calendar;
import com.dev.user_transaction_management_system.domain.Date;
import com.dev.user_transaction_management_system.domain.bank_account.BankAccount;
import com.dev.user_transaction_management_system.domain.bank_account.AccountId;
import com.dev.user_transaction_management_system.domain.bank_account.AccountNumber;
import com.dev.user_transaction_management_system.domain.transaction.Amount;
import com.dev.user_transaction_management_system.infrastructure.persistence.model.BankAccountEntity;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class BankAccountMapper {

    public BankAccount toDomain(BankAccountEntity account) {
        final UserMapper userMapper = new UserMapper();
        return BankAccount.open(
                AccountId.fromUUID(UUID.fromString(account.getAccountId())),
                AccountNumber.of(account.getAccountNumber()),
                userMapper.toDomain(account.getUser()),
                Amount.of(account.getBalance()),
                account.getStatus(),
                Date.fromLocalDateTime(account.getCreatedAt())
        );
    }
}
