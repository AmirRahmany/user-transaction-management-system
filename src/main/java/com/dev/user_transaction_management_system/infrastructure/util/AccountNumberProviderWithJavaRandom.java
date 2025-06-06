package com.dev.user_transaction_management_system.infrastructure.util;

import com.dev.user_transaction_management_system.domain.bank_account.AccountNumber;
import com.dev.user_transaction_management_system.domain.bank_account.AccountNumberProvider;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class AccountNumberProviderWithJavaRandom implements AccountNumberProvider {
    private static final  int ACCOUNT_NUMBER_LENGTH = 13;
    private static final  String PREFIX = "0300";
    private final Random random;

    public AccountNumberProviderWithJavaRandom() {
        this.random = new Random();
    }

    public AccountNumber generateAccountNumber() {
        StringBuilder tmpAccountNumber = new StringBuilder();
        for (int i = 0; i < accountNumberLength(); i++) {
            tmpAccountNumber.append(random.nextInt(0, 10));
        }
        final String accountNumber = tmpAccountNumber.toString();


        return AccountNumber.of(PREFIX + accountNumber);
    }

    private int accountNumberLength() {
        return ACCOUNT_NUMBER_LENGTH - PREFIX.length();
    }

}
