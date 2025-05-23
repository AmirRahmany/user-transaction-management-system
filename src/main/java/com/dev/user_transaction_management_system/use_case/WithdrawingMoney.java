package com.dev.user_transaction_management_system.use_case;

import com.dev.user_transaction_management_system.domain.bank_account.BankAccount;
import com.dev.user_transaction_management_system.domain.bank_account.AccountNumber;
import com.dev.user_transaction_management_system.domain.bank_account.BankAccountRepository;
import com.dev.user_transaction_management_system.domain.exceptions.CouldNotFindBankAccount;
import com.dev.user_transaction_management_system.domain.transaction.*;
import com.dev.user_transaction_management_system.infrastructure.persistence.model.BankAccountEntity;
import com.dev.user_transaction_management_system.infrastructure.util.mapper.BankAccountMapper;
import com.dev.user_transaction_management_system.use_case.dto.TransactionReceipt;
import com.dev.user_transaction_management_system.use_case.dto.WithdrawalRequest;
import io.jsonwebtoken.lang.Assert;
import jakarta.transaction.Transactional;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class WithdrawingMoney {

    private final TransactionRepository transactionRepository;
    private final BankAccountRepository accountRepository;
    private final BankAccountMapper bankAccountMapper;
    private final ApplicationEventPublisher eventPublisher;

    public WithdrawingMoney(@NonNull TransactionRepository transactionRepository,
                            @NonNull BankAccountRepository bankAccountRepository,
                            @NonNull ApplicationEventPublisher eventPublisher,
                            @NonNull BankAccountMapper bankAccountMapper) {

        this.transactionRepository = transactionRepository;
        this.accountRepository = bankAccountRepository;
        this.eventPublisher = eventPublisher;
        this.bankAccountMapper = bankAccountMapper;
    }

    @Transactional
    public TransactionReceipt withdraw(WithdrawalRequest request) {
        Assert.notNull(request,"withdraw request cannot be null");

        AccountNumber fromAccountNumber = AccountNumber.of(request.fromAccountNumber());
        final BankAccount account = finAccountBy(request.fromAccountNumber());
        String referenceNumber = transactionRepository.generateReferenceNumber();


        final Amount amount = Amount.of(request.funds());
        account.decreaseBalance(amount);

        final LocalDateTime createdAt = LocalDateTime.now();
        final var transaction = initiateTransaction(request, fromAccountNumber, amount, referenceNumber, createdAt);

        accountRepository.save(account.toEntity());
        transactionRepository.save(transaction.toEntity());
        account.releaseEvents().forEach(eventPublisher::publishEvent);
        return TransactionReceipt.makeOf(amount.asDouble(),referenceNumber,fromAccountNumber.asString(),createdAt);
    }

    private Transaction initiateTransaction(WithdrawalRequest request,
                                                   AccountNumber fromAccountNumber,
                                                   Amount amount, String referenceNumber,
                                                   LocalDateTime createdAt) {
        return Transaction.of(
                TransactionId.autoGenerateByDb(),
                fromAccountNumber,
                TransactionDetail.of(amount, TransactionType.WITHDRAWAL, request.description()),
                ReferenceNumber.fromString(referenceNumber),
                createdAt);
    }

    private BankAccount finAccountBy(String reqAccountNumber) {
        final AccountNumber accountNumber = AccountNumber.of(reqAccountNumber);

        final BankAccountEntity bankAccountEntity = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> CouldNotFindBankAccount.withAccountNumber(accountNumber.toString()));

        return bankAccountMapper.toDomain(bankAccountEntity);
    }
}
