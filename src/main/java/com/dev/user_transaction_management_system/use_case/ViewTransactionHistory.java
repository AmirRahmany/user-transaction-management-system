package com.dev.user_transaction_management_system.use_case;

import com.dev.user_transaction_management_system.domain.bank_account.AccountNumber;
import com.dev.user_transaction_management_system.domain.transaction.TransactionRepository;
import com.dev.user_transaction_management_system.infrastructure.persistence.model.TransactionEntity;
import com.dev.user_transaction_management_system.use_case.dto.TransactionHistory;
import io.jsonwebtoken.lang.Assert;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ViewTransactionHistory {

    private final TransactionRepository transactionRepository;

    public ViewTransactionHistory(@NonNull TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public List<TransactionHistory> getHistoryByAccountNumber(String accountNumberRequest) {
        Assert.hasText(accountNumberRequest, "account number cannot be null or empty");

        final List<TransactionEntity> result =
                transactionRepository.findByAccountNumber(AccountNumber.of(accountNumberRequest));

        List<TransactionHistory> histories = new ArrayList<>();
        result.forEach(transaction -> histories.add(new TransactionHistory(
                transaction.getTransactionType().name(),
                transaction.getCreatedAt(),
                transaction.getAmount(),
                transaction.getReferenceNumber())
        ));

        return histories;
    }
}
