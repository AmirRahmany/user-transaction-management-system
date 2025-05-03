package com.dev.user_transaction_management_system.fake;

import com.dev.user_transaction_management_system.domain.transaction.*;

import static com.dev.user_transaction_management_system.fake.AccountFake.account;

public class TransactionFake {
    private  int transactionId;
    private  String userId = "user_987fbc97-4bed-5078-8f07-9141ba07c9f3";
    private  Account account = account().open();
    private TransactionStatus transactionStatus = TransactionStatus.PROCESSING;
    private  Amount amount = Amount.of(200);
    private  TransactionType transactionType;


    public static TransactionFake transaction(){
        return new TransactionFake();
    }

    public TransactionFake withTransactionId(int transactionId){
        this.transactionId = transactionId;
        return this;
    }

    public TransactionFake withUserId(String userId){
        this.userId = userId;
        return this;
    }

    public TransactionFake withAccount(Account account){
        this.account = account;
        return this;
    }

    public TransactionFake withStatus(TransactionStatus status){
        this.transactionStatus = status;
        return this;
    }

    public TransactionFake withAmount(double amount){
        this.amount = Amount.of(amount);
        return this;
    }

    public TransactionFake withTransactionType(TransactionType transactionType){
        this.transactionType = transactionType;
        return this;
    }

    public TransactionFake withInsufficientBalance() {
        this.account = AccountFake.account().withInsufficientBalance().open();
        return this;
    }

    public Transaction initiate(){
        return Transaction.of(userId,account,amount,transactionType);
    }

}
