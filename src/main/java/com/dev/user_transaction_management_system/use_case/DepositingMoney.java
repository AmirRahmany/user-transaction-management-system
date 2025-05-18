package com.dev.user_transaction_management_system.use_case;

import com.dev.user_transaction_management_system.domain.bank_account.AccountNumber;
import com.dev.user_transaction_management_system.domain.bank_account.BankAccount;
import com.dev.user_transaction_management_system.domain.bank_account.BankAccountRepository;
import com.dev.user_transaction_management_system.domain.exceptions.CouldNotFindBankAccount;
import com.dev.user_transaction_management_system.domain.transaction.*;
import com.dev.user_transaction_management_system.infrastructure.persistence.model.BankAccountEntity;
import com.dev.user_transaction_management_system.infrastructure.util.BankAccountMapper;
import com.dev.user_transaction_management_system.use_case.dto.TransactionReceipt;
import com.dev.user_transaction_management_system.use_case.dto.DepositRequest;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class DepositingMoney {

    private final TransactionRepository transactionRepository;
    private final BankAccountRepository accountRepository;
    private final BankAccountMapper bankAccountMapper;

    public DepositingMoney(TransactionRepository transactionRepository,
                           BankAccountRepository accountRepository) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
        this.bankAccountMapper = new BankAccountMapper();
    }

    @Transactional
    public TransactionReceipt deposit(DepositRequest depositRequest) {
        final AccountNumber fromAccountNumber = AccountNumber.of(depositRequest.accountNumber());

        String referenceNumber = transactionRepository.generateReferenceNumber();

        final LocalDateTime createdAt = LocalDateTime.now();
        final BankAccount bankAccount = finAccountBy(fromAccountNumber);
        final Amount amount = Amount.of(depositRequest.amount());


        bankAccount.increaseAmount(amount);

        final Transaction transaction = initiateTransaction(depositRequest, referenceNumber,createdAt);

        accountRepository.save(bankAccount.toEntity());
        transactionRepository.save(transaction.toEntity());

        return TransactionReceipt.makeOf(amount.asDouble(),
                referenceNumber,
                fromAccountNumber.toString(),
                createdAt);
    }


    private BankAccount finAccountBy(AccountNumber fromAccountNumber) {
        final BankAccountEntity fromEntity = accountRepository.findByAccountNumber(fromAccountNumber)
                .orElseThrow(() -> CouldNotFindBankAccount.withAccountNumber(fromAccountNumber.toString()));
        return bankAccountMapper.toDomain(fromEntity);
    }

    private Transaction initiateTransaction(DepositRequest request, String referenceNumber, LocalDateTime createdAt) {
        final Amount amount = Amount.of(request.amount());
        final String description = request.description();
        final TransactionDetail transactionDetail = TransactionDetail.of(amount, TransactionType.DEPOSIT, description);

        return Transaction.of(
                TransactionId.autoGenerateByDb(),
                AccountNumber.of(request.accountNumber()),
                transactionDetail,
                ReferenceNumber.fromString(referenceNumber),
                createdAt);
    }
}
