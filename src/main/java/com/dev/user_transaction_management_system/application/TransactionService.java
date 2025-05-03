package com.dev.user_transaction_management_system.application;

import com.dev.user_transaction_management_system.domain.transaction.Account;
import com.dev.user_transaction_management_system.domain.transaction.Transaction;

public interface TransactionService {
    void deposit(Transaction transaction,Account to);
}
