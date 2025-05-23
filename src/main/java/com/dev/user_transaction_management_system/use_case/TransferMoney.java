package com.dev.user_transaction_management_system.use_case;

import com.dev.user_transaction_management_system.domain.bank_account.BankAccount;
import com.dev.user_transaction_management_system.domain.bank_account.AccountNumber;
import com.dev.user_transaction_management_system.domain.exceptions.CouldNotFindBankAccount;
import com.dev.user_transaction_management_system.domain.transaction.*;
import com.dev.user_transaction_management_system.domain.bank_account.BankAccountRepository;
import com.dev.user_transaction_management_system.infrastructure.persistence.model.BankAccountEntity;
import com.dev.user_transaction_management_system.infrastructure.util.mapper.BankAccountMapper;
import com.dev.user_transaction_management_system.use_case.dto.TransferMoneyRequest;
import com.dev.user_transaction_management_system.use_case.dto.TransferReceipt;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class TransferMoney {

    private final TransactionRepository transactionRepository;
    private final BankAccountRepository accountRepository;
    private final BankAccountMapper bankAccountMapper;

    public TransferMoney(TransactionRepository transactionRepository,
                         BankAccountRepository accountRepository) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
        this.bankAccountMapper = new BankAccountMapper();
    }

    @Transactional
    public TransferReceipt transfer(TransferMoneyRequest request) {
        final AccountNumber fromAccountNumber = AccountNumber.of(request.fromAccountNumber());
        final AccountNumber toAccountNumber = AccountNumber.of(request.toAccountNumber());
        fromAccountNumber.ensureDistinctAccounts(toAccountNumber);

        final BankAccount from = finAccountBy(fromAccountNumber);
        final BankAccount to = finAccountBy(toAccountNumber);
        String referenceNumber = transactionRepository.generateReferenceNumber();

        final Amount amount = Amount.of(request.amount());
        from.decreaseBalance(amount);
        to.increaseAmount(amount);

        final LocalDateTime createdAt = LocalDateTime.now();
        final Transaction transaction = initiateTransaction(request, referenceNumber,createdAt);

        accountRepository.save(from.toEntity());
        accountRepository.save(to.toEntity());
        transactionRepository.save(transaction.toEntity());

        return TransferReceipt.makeOf(amount.asDouble(), fromAccountNumber.toString(), toAccountNumber.toString(),
                referenceNumber, createdAt);
    }


    private BankAccount finAccountBy(AccountNumber fromAccountNumber) {
        final BankAccountEntity fromEntity = accountRepository.findByAccountNumber(fromAccountNumber)
                .orElseThrow(() -> CouldNotFindBankAccount.withAccountNumber(fromAccountNumber.toString()));
        return bankAccountMapper.toDomain(fromEntity);
    }

    private Transaction initiateTransaction(TransferMoneyRequest transferMoneyRequest, String referenceNumber, LocalDateTime createdAt) {
        final Amount amount = Amount.of(transferMoneyRequest.amount());
        final String description = transferMoneyRequest.description();
        final TransactionDetail transactionDetail = TransactionDetail.of(amount, TransactionType.DEPOSIT, description);

        return Transaction.of(
                TransactionId.autoGenerateByDb(),
                AccountNumber.of(transferMoneyRequest.fromAccountNumber()),
                transactionDetail,
                ReferenceNumber.fromString(referenceNumber),
                createdAt);
    }
}
