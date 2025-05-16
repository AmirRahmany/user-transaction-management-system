package com.dev.user_transaction_management_system.use_case;

import com.dev.user_transaction_management_system.domain.bank_account.BankAccount;
import com.dev.user_transaction_management_system.domain.bank_account.AccountNumber;
import com.dev.user_transaction_management_system.domain.exceptions.CouldNotFindBankAccount;
import com.dev.user_transaction_management_system.domain.transaction.*;
import com.dev.user_transaction_management_system.domain.bank_account.BankAccountRepository;
import com.dev.user_transaction_management_system.infrastructure.persistence.model.BankAccountEntity;
import com.dev.user_transaction_management_system.infrastructure.util.BankAccountMapper;
import com.dev.user_transaction_management_system.use_case.dto.DepositReceipt;
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
    public TransferReceipt transfer(TransferMoneyRequest transferMoneyRequest) {
        final AccountNumber fromAccountNumber = AccountNumber.of(transferMoneyRequest.fromAccountNumber());
        final AccountNumber toAccountNumber = AccountNumber.of(transferMoneyRequest.toAccountNumber());
        fromAccountNumber.ensureDistinctAccounts(toAccountNumber);

        String referenceNumber = transactionRepository.generateReferenceNumber();

        final LocalDateTime createdAt = LocalDateTime.now();
        final BankAccount from = finAccountBy(fromAccountNumber);
        final BankAccount to = finAccountBy(toAccountNumber);
        final Amount amount = Amount.of(transferMoneyRequest.amount());


        from.decreaseBalance(amount);
        to.increaseAmount(amount);

        final Transaction transaction = initiateTransaction(transferMoneyRequest, referenceNumber,createdAt);

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
                AccountNumber.of(transferMoneyRequest.toAccountNumber()),
                transactionDetail,
                ReferenceNumber.fromString(referenceNumber),
                createdAt);
    }
}
