package com.dev.user_transaction_management_system.unit;

import com.dev.user_transaction_management_system.domain.account.AccountNumber;
import com.dev.user_transaction_management_system.domain.account.IAccountNumberGenerator;

public class AccountNumberGeneratorStubs implements IAccountNumberGenerator {

    @Override
    public AccountNumber generate() {
        return AccountNumber.of("0300546214558");
    }
}
