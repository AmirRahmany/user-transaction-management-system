package com.dev.user_transaction_management_system.infrastructure.util;

import com.dev.user_transaction_management_system.domain.account.Account;
import com.dev.user_transaction_management_system.domain.account.AccountNumber;
import com.dev.user_transaction_management_system.domain.transaction.Amount;
import com.dev.user_transaction_management_system.infrastructure.persistence.model.AccountEntity;

public class AccountMapper {

    public Account toDomain(AccountEntity account) {
        return Account.open(
                account.getAccountId(),
                AccountNumber.of(account.getAccountNumber()),
                account.getUserId(),
                Amount.of(account.getBalance()),
                account.getCreatedAt()
        );
    }
}
