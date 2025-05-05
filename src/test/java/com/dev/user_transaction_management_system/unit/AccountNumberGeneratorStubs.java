package com.dev.user_transaction_management_system.unit;

import com.dev.user_transaction_management_system.domain.transaction.AccountNumber;
import com.dev.user_transaction_management_system.util.IAccountNumberGenerator;

public class AccountNumberGeneratorStubs implements IAccountNumberGenerator {

    @Override
    public AccountNumber generate() {
        return AccountNumber.of("0300546214558");
    }
}
