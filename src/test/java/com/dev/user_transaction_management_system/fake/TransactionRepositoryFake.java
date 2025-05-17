package com.dev.user_transaction_management_system.fake;

import com.dev.user_transaction_management_system.domain.bank_account.AccountNumber;
import com.dev.user_transaction_management_system.domain.transaction.Transaction;
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
    public Optional<TransactionEntity> findById(Integer transactionId) {
        return transactions.values().stream().filter(transactionEntity -> transactionEntity.getTransactionId()
                .equals(transactionId)).findFirst();
    }

    @Override
    public String generateReferenceNumber() {
        return "03004565879851";
    }

    @Override
    public List<TransactionEntity> findByAccountNumber(AccountNumber bankAccountNumber) {
        List<TransactionEntity> allTransactions = new LinkedList<>();
        transactions.values().stream()
                .filter(transaction -> bankAccountNumber.isSameAs(AccountNumber.of(transaction.getFromAccountNumber())))
                .forEach(transactionEntity -> allTransactions.add(transactionEntity));

        transactions.values().stream().filter(transaction -> {
                    if (transaction.getToAccountNumber() != null)
                        return bankAccountNumber.isSameAs(AccountNumber.of(transaction.getToAccountNumber()));
                    return false;
                })
                .forEach(transactionEntity -> allTransactions.add(transactionEntity));
        return allTransactions;
    }

}
