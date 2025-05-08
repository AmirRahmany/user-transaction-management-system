package com.dev.user_transaction_management_system.use_case;

import com.dev.user_transaction_management_system.domain.account.Account;
import com.dev.user_transaction_management_system.domain.account.AccountNumber;
import com.dev.user_transaction_management_system.domain.exceptions.CouldNotFindAccount;
import com.dev.user_transaction_management_system.domain.transaction.*;
import com.dev.user_transaction_management_system.domain.account.AccountRepository;
import com.dev.user_transaction_management_system.infrastructure.persistence.model.AccountEntity;
import com.dev.user_transaction_management_system.infrastructure.util.AccountMapper;
import com.dev.user_transaction_management_system.use_case.dto.DepositRequest;
import com.dev.user_transaction_management_system.use_case.dto.WithdrawalRequest;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class DepositingMoney {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final WithdrawingMoney withdrawingMoney;
    private final AccountMapper accountMapper;

    public DepositingMoney(TransactionRepository transactionRepository,
                           AccountRepository accountRepository, WithdrawingMoney withdrawingMoney) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
        this.withdrawingMoney = withdrawingMoney;
        this.accountMapper = new AccountMapper();
    }

    @Transactional
    public Transaction deposit(DepositRequest depositRequest) {
        final AccountNumber fromAccountNumber = AccountNumber.of(depositRequest.fromAccountNumber());
        final AccountNumber toAccountNumber = AccountNumber.of(depositRequest.toAccountNumber());

        fromAccountNumber.ensureDistinctAccounts(toAccountNumber);

        final WithdrawalRequest withdrawalRequest = new WithdrawalRequest(
                depositRequest.amount(),
                depositRequest.fromAccountNumber(),
                depositRequest.description());

       withdrawingMoney.withdraw(withdrawalRequest);

        final Account to = finAccountBy(toAccountNumber);
        to.increaseAmount(Amount.of(depositRequest.amount()));
        final Transaction transaction = initiateTransaction(depositRequest);


        accountRepository.save(to.toEntity());
        transactionRepository.save(transaction.toEntity());

        // return dto for user
        return transaction;
    }

    private Account finAccountBy(AccountNumber fromAccountNumber) {
        final AccountEntity fromEntity = accountRepository.findByAccountNumber(fromAccountNumber)
                .orElseThrow(() -> CouldNotFindAccount.withAccountNumber(fromAccountNumber.toString()));
        return accountMapper.toDomain(fromEntity);
    }

    private Transaction initiateTransaction(DepositRequest depositRequest) {
        String referenceNumber = UUID.randomUUID().toString();
        final Amount amount = Amount.of(depositRequest.amount());
        final String description = depositRequest.description();
        final TransactionDetail transactionDetail = TransactionDetail.of(amount, TransactionType.DEPOSIT, description);

        return Deposit.of(
                TransactionId.autoGenerateByDb(),
                transactionDetail,
                AccountNumber.of(depositRequest.fromAccountNumber()),
                AccountNumber.of(depositRequest.toAccountNumber()),
                ReferenceNumber.fromString(referenceNumber),
                depositRequest.createdAt());
    }
}
