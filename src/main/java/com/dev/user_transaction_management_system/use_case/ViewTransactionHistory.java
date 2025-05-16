package com.dev.user_transaction_management_system.use_case;

import com.dev.user_transaction_management_system.domain.bank_account.AccountNumber;
import com.dev.user_transaction_management_system.domain.transaction.TransactionRepository;
import com.dev.user_transaction_management_system.infrastructure.persistence.model.TransactionEntity;
import com.dev.user_transaction_management_system.use_case.dto.TransactionHistory;
import com.dev.user_transaction_management_system.use_case.dto.TransactionHistoryRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ViewTransactionHistory {

    private final TransactionRepository transactionRepository;

    public ViewTransactionHistory(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public List<TransactionHistory> viewHistoryByAccountNumber(TransactionHistoryRequest request) {
        List<TransactionHistory> histories = new ArrayList<>();
        final AccountNumber accountNumber = AccountNumber.of(request.accountNumber());
        final List<TransactionEntity> result = transactionRepository.findByAccountNumber(accountNumber);

        result.forEach(transaction -> histories.add(new TransactionHistory(
                transaction.getTransactionType().name(),
               transaction.getCreatedAt(),transaction.getAmount(),
                transaction.getReferenceNumber())
        ));

        return histories;
    }


}
