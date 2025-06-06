package com.dev.user_transaction_management_system.domain.transaction;

import com.dev.user_transaction_management_system.domain.Date;
import com.dev.user_transaction_management_system.domain.bank_account.AccountNumber;
import com.dev.user_transaction_management_system.infrastructure.persistence.model.TransactionEntity;
import org.springframework.util.Assert;

import java.time.LocalDateTime;

public class Transaction {

    private final TransactionId transactionId;
    private TransactionDetail transactionDetail;
    private final AccountNumber accountNumber;
    private final Date createdAt;
    private final ReferenceNumber referenceNumber;

    public Transaction(
            TransactionId transactionId,
            AccountNumber fromAccountNumber,
            TransactionDetail transactionDetail,
            Date createdAt,
            ReferenceNumber referenceNumber) {

        Assert.notNull(transactionId, "transaction id cannot be null");
        Assert.notNull(transactionDetail, "transaction detail cannot be null");
        Assert.notNull(referenceNumber, "reference number cannot be null");
        Assert.notNull(createdAt, "created at cannot be null");

        this.accountNumber = fromAccountNumber;
        this.createdAt = createdAt;
        this.referenceNumber = referenceNumber;
        this.transactionDetail = transactionDetail;
        this.transactionId = transactionId;
    }

    public static Transaction of(
            TransactionId transactionId,
            AccountNumber accountNumber,
            TransactionDetail transactionDetail,
            ReferenceNumber referenceNumber,
            Date createdAt) {

        return new Transaction(transactionId,
                accountNumber,
                transactionDetail,
                createdAt,
                referenceNumber);
    }


    public TransactionEntity toEntity() {
        final TransactionEntity entity = new TransactionEntity();
        entity.setAmount(transactionDetail.amount().asDouble());
        entity.setDescription(transactionDetail.description());
        entity.setTransactionType(transactionDetail.transactionType());
        entity.setAccountNumber(accountNumber.toString());
        entity.setReferenceNumber(referenceNumber.toString());
        entity.setCreatedAt(createdAt.asLocalDateTime());
        return entity;
    }
}
