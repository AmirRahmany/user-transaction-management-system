package com.dev.user_transaction_management_system.fake;

import com.dev.user_transaction_management_system.domain.bank_account.AccountNumber;
import com.dev.user_transaction_management_system.domain.bank_account.AccountNumberProvider;

public class AccountNumberProviderStub implements AccountNumberProvider {

    @Override
    public AccountNumber generateAccountNumber() {
        return AccountNumber.of("0300546214558");
    }
}
