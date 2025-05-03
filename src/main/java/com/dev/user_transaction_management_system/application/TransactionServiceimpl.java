package com.dev.user_transaction_management_system.application;

import com.dev.user_transaction_management_system.domain.transaction.Account;
import com.dev.user_transaction_management_system.domain.transaction.Transaction;
import com.dev.user_transaction_management_system.exceptions.InsufficientAccountBalance;

public class TransactionServiceimpl implements TransactionService {

    @Override
    public void deposit(Transaction transaction, Account to) {
        if (transaction.isBalanceInsufficient()){
            throw new InsufficientAccountBalance();
        }
    }
}
