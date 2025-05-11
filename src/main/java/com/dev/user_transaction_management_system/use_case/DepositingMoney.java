package com.dev.user_transaction_management_system.use_case;

import com.dev.user_transaction_management_system.domain.account.BankAccount;
import com.dev.user_transaction_management_system.domain.account.AccountNumber;
import com.dev.user_transaction_management_system.domain.exceptions.CouldNotFindAccount;
import com.dev.user_transaction_management_system.domain.transaction.*;
import com.dev.user_transaction_management_system.domain.account.BankAccountRepository;
import com.dev.user_transaction_management_system.infrastructure.persistence.model.AccountEntity;
import com.dev.user_transaction_management_system.infrastructure.util.AccountMapper;
import com.dev.user_transaction_management_system.use_case.dto.DepositReceipt;
import com.dev.user_transaction_management_system.use_case.dto.DepositRequest;
import com.dev.user_transaction_management_system.use_case.dto.WithdrawalRequest;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class DepositingMoney {

    private final TransactionRepository transactionRepository;
    private final BankAccountRepository accountRepository;
    private final WithdrawingMoney withdrawingMoney;
    private final AccountMapper accountMapper;

    public DepositingMoney(TransactionRepository transactionRepository,
                           BankAccountRepository accountRepository, WithdrawingMoney withdrawingMoney) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
        this.withdrawingMoney = withdrawingMoney;
        this.accountMapper = new AccountMapper();
    }

    @Transactional
    public DepositReceipt deposit(DepositRequest depositRequest) {
        final AccountNumber fromAccountNumber = AccountNumber.of(depositRequest.fromAccountNumber());
        final AccountNumber toAccountNumber = AccountNumber.of(depositRequest.toAccountNumber());
        fromAccountNumber.ensureDistinctAccounts(toAccountNumber);

        final WithdrawalRequest withdrawalRequest = makeWithdrawalRequestOf(depositRequest);
        ReferenceNumber referenceNumber = withdrawingMoney.withdraw(withdrawalRequest);
        final BankAccount to = finAccountBy(toAccountNumber);
        to.increaseAmount(Amount.of(depositRequest.amount()));
        final Transaction transaction = initiateTransaction(depositRequest, referenceNumber);


        accountRepository.save(to.toEntity());
        transactionRepository.save(transaction.toEntity());

        return DepositReceipt.makeOf(referenceNumber.toString(),
                fromAccountNumber.toString(),
                toAccountNumber.toString(),
                depositRequest.createdAt());
    }

    private static WithdrawalRequest makeWithdrawalRequestOf(DepositRequest depositRequest) {
        return new WithdrawalRequest(
                depositRequest.amount(),
                depositRequest.fromAccountNumber(),
                depositRequest.description());
    }

    private BankAccount finAccountBy(AccountNumber fromAccountNumber) {
        final AccountEntity fromEntity = accountRepository.findByAccountNumber(fromAccountNumber)
                .orElseThrow(() -> CouldNotFindAccount.withAccountNumber(fromAccountNumber.toString()));
        return accountMapper.toDomain(fromEntity);
    }

    private Transaction initiateTransaction(DepositRequest depositRequest, ReferenceNumber referenceNumber) {
        final Amount amount = Amount.of(depositRequest.amount());
        final String description = depositRequest.description();
        final TransactionDetail transactionDetail = TransactionDetail.of(amount, TransactionType.DEPOSIT, description);

        return Deposit.of(
                TransactionId.autoGenerateByDb(),
                transactionDetail,
                AccountNumber.of(depositRequest.fromAccountNumber()),
                AccountNumber.of(depositRequest.toAccountNumber()),
                referenceNumber,
                depositRequest.createdAt());
    }
}
