package com.dev.user_transaction_management_system.infrastructure.util;

import com.dev.user_transaction_management_system.domain.account.BankAccount;
import com.dev.user_transaction_management_system.domain.account.AccountId;
import com.dev.user_transaction_management_system.domain.account.AccountNumber;
import com.dev.user_transaction_management_system.domain.transaction.Amount;
import com.dev.user_transaction_management_system.domain.user.UserId;
import com.dev.user_transaction_management_system.infrastructure.persistence.model.AccountEntity;

public class AccountMapper {

    public BankAccount toDomain(AccountEntity account) {
        return BankAccount.open(
                AccountId.fromInt(account.getAccountId()),
                AccountNumber.of(account.getAccountNumber()),
                UserId.fromInt(account.getUserId()),
                Amount.of(account.getBalance()),
                account.getCreatedAt()
        );
    }
}
