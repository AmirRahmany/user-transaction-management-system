package com.dev.user_transaction_management_system.util;

import com.dev.user_transaction_management_system.domain.transaction.AccountNumber;

import java.util.Random;

public class AccountNumberGenerator {
    private static final  int ACCOUNT_NUMBER_LENGTH = 13;
    private static final  String PREFIX = "0300";
    private Random random;

    public AccountNumberGenerator() {
        this.random = new Random();
    }

    public AccountNumber generate() {
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
