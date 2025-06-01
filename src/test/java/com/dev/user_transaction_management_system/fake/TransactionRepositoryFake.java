package com.dev.user_transaction_management_system.fake;

import com.dev.user_transaction_management_system.domain.bank_account.AccountNumber;
import com.dev.user_transaction_management_system.domain.transaction.ReferenceNumber;
import com.dev.user_transaction_management_system.domain.transaction.TransactionRepository;
import com.dev.user_transaction_management_system.infrastructure.persistence.model.TransactionEntity;

import java.util.*;

public class TransactionRepositoryFake implements TransactionRepository {

    private final Map<Integer, TransactionEntity> transactions = new LinkedHashMap<>();

    @Override
    public void save(TransactionEntity transactionEntity) {
        if (transactionEntity.getTransactionId() == null) {
            transactionEntity.setTransactionId(transactions.size() + 1);
        }
        transactions.put(transactionEntity.getTransactionId(), transactionEntity);
    }

    @Override
    public ReferenceNumber generateReferenceNumber() {
        return ReferenceNumber.fromString("03004565879851");
    }

    @Override
    public List<TransactionEntity> findByAccountNumber(AccountNumber bankAccountNumber) {
        List<TransactionEntity> allTransactions = new LinkedList<>();
        transactions.values().stream()
                .filter(transaction -> bankAccountNumber.isSameAs(AccountNumber.of(transaction.getAccountNumber())))
                .forEach(transactionEntity -> allTransactions.add(transactionEntity));

        transactions.values().stream().filter(transaction -> {
                    if (transaction.getAccountNumber() != null)
                        return bankAccountNumber.isSameAs(AccountNumber.of(transaction.getAccountNumber()));
                    return false;
                })
                .forEach(transactionEntity -> allTransactions.add(transactionEntity));
        return allTransactions;
    }

}
