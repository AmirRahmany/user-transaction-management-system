package com.dev.user_transaction_management_system.use_case.withdraw_money;

import com.dev.user_transaction_management_system.domain.Clock;
import com.dev.user_transaction_management_system.domain.Date;
import com.dev.user_transaction_management_system.domain.bank_account.BankAccount;
import com.dev.user_transaction_management_system.domain.bank_account.AccountNumber;
import com.dev.user_transaction_management_system.domain.bank_account.BankAccountRepository;
import com.dev.user_transaction_management_system.domain.exceptions.CouldNotFindBankAccount;
import com.dev.user_transaction_management_system.domain.transaction.*;
import com.dev.user_transaction_management_system.infrastructure.persistence.model.BankAccountEntity;
import com.dev.user_transaction_management_system.infrastructure.util.mapper.BankAccountMapper;
import com.dev.user_transaction_management_system.use_case.deposit_money.TransactionReceipt;
import io.jsonwebtoken.lang.Assert;
import jakarta.transaction.Transactional;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

@Service
public class WithdrawMoney {

    private final TransactionRepository transactionRepository;
    private final BankAccountRepository accountRepository;
    private final BankAccountMapper bankAccountMapper;
    private final ApplicationEventPublisher eventPublisher;
    private final Clock clock;

    public WithdrawMoney(@NonNull TransactionRepository transactionRepository,
                         @NonNull BankAccountRepository bankAccountRepository,
                         @NonNull ApplicationEventPublisher eventPublisher,
                         @NonNull BankAccountMapper bankAccountMapper,
                         @NonNull Clock clock) {

        this.transactionRepository = transactionRepository;
        this.accountRepository = bankAccountRepository;
        this.eventPublisher = eventPublisher;
        this.bankAccountMapper = bankAccountMapper;
        this.clock = clock;
    }

    @Transactional
    public TransactionReceipt withdraw(WithdrawalRequest request) {
        Assert.notNull(request, "withdraw request cannot be null");

        AccountNumber fromAccountNumber = AccountNumber.of(request.fromAccountNumber());
        final BankAccount account = finAccountBy(request.fromAccountNumber());
        final var referenceNumber = transactionRepository.generateReferenceNumber();
        final Date currentTime = Date.fromCurrentTime(clock.currentTime());

        final Amount amount = Amount.of(request.funds());
        account.decreaseBalance(amount);
        final var transaction = initiateTransaction(request, fromAccountNumber, amount, referenceNumber, currentTime);

        accountRepository.save(account.toEntity());
        transactionRepository.save(transaction.toEntity());
        account.releaseEvents().forEach(eventPublisher::publishEvent);
        return TransactionReceipt.makeOf(amount.asDouble(), referenceNumber.toString(), fromAccountNumber.asString(), currentTime.asString());
    }

    private Transaction initiateTransaction(WithdrawalRequest request,
                                            AccountNumber fromAccountNumber,
                                            Amount amount,
                                            ReferenceNumber referenceNumber,
                                            Date createdAt) {
        return Transaction.of(
                TransactionId.autoGenerateByDb(),
                fromAccountNumber,
                TransactionDetail.of(amount, TransactionType.WITHDRAWAL, request.description()),
                referenceNumber,
                createdAt);
    }

    private BankAccount finAccountBy(String reqAccountNumber) {
        final AccountNumber accountNumber = AccountNumber.of(reqAccountNumber);

        final BankAccountEntity bankAccountEntity = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> CouldNotFindBankAccount.withAccountNumber(accountNumber.toString()));

        return bankAccountMapper.toDomain(bankAccountEntity);
    }
}
