package com.dev.user_transaction_management_system.use_case;

import com.dev.user_transaction_management_system.domain.account.BankAccount;
import com.dev.user_transaction_management_system.domain.account.AccountNumber;
import com.dev.user_transaction_management_system.domain.account.BankAccountRepository;
import com.dev.user_transaction_management_system.domain.exceptions.CouldNotFindAccount;
import com.dev.user_transaction_management_system.domain.transaction.*;
import com.dev.user_transaction_management_system.infrastructure.persistence.model.AccountEntity;
import com.dev.user_transaction_management_system.infrastructure.util.AccountMapper;
import com.dev.user_transaction_management_system.use_case.dto.WithdrawalRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class WithdrawingMoney {

    private final TransactionRepository transactionRepository;
    private final BankAccountRepository accountRepository;
    private final AccountMapper accountMapper;

    public WithdrawingMoney(TransactionRepository transactionRepository,
                            BankAccountRepository bankAccountRepository) {

        this.transactionRepository = transactionRepository;
        this.accountRepository = bankAccountRepository;
        this.accountMapper = new AccountMapper();
    }

    public ReferenceNumber withdraw(WithdrawalRequest withdrawalRequest) {
        final BankAccount account = finAccountBy(withdrawalRequest.accountNumber());

        final Amount amount = Amount.of(withdrawalRequest.funds());
        account.ensureSufficientBalanceFor(amount);

        account.decreaseBalance(amount);
        String referenceNumber = UUID.randomUUID().toString();
        final Transaction transaction = initTransaction(withdrawalRequest, account, amount, referenceNumber);

        accountRepository.save(account.toEntity());
        transactionRepository.save(transaction.toEntity());
        return ReferenceNumber.fromString(referenceNumber);
    }

    private BankAccount finAccountBy(String reqAccountNumber) {
        final AccountNumber accountNumber = AccountNumber.of(reqAccountNumber);

        final AccountEntity accountEntity = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> CouldNotFindAccount.withAccountNumber(accountNumber.toString()));

        return accountMapper.toDomain(accountEntity);
    }

    private static Transaction
    initTransaction(WithdrawalRequest withdrawalRequest, BankAccount bankAccount, Amount amount, String referenceNumber) {

        return Withdrawal.of(
                TransactionId.autoGenerateByDb(),
                bankAccount.accountNumber(),
                TransactionDetail.of(amount, TransactionType.WITHDRAWAL, withdrawalRequest.description()),
                ReferenceNumber.fromString(referenceNumber),
                LocalDateTime.now());
    }
}
