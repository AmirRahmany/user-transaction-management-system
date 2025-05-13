package com.dev.user_transaction_management_system.fake;

import com.dev.user_transaction_management_system.domain.bank_account.AccountNumber;
import com.dev.user_transaction_management_system.domain.bank_account.IAccountNumberGenerator;

public class AccountNumberGeneratorStubs implements IAccountNumberGenerator {

    @Override
    public AccountNumber generate() {
        return AccountNumber.of("0300546214558");
    }
}
