package com.dev.user_transaction_management_system.use_case;

import com.dev.user_transaction_management_system.domain.account.Account;
import com.dev.user_transaction_management_system.domain.account.AccountNumber;
import com.dev.user_transaction_management_system.domain.exceptions.CouldNotFindAccount;
import com.dev.user_transaction_management_system.domain.transaction.Amount;
import com.dev.user_transaction_management_system.domain.transaction.Transaction;
import com.dev.user_transaction_management_system.domain.transaction.TransactionType;
import com.dev.user_transaction_management_system.domain.exceptions.CouldNotProcessTransaction;
import com.dev.user_transaction_management_system.domain.account.AccountRepository;
import com.dev.user_transaction_management_system.domain.transaction.TransactionRepository;
import com.dev.user_transaction_management_system.infrastructure.persistence.model.AccountEntity;
import com.dev.user_transaction_management_system.infrastructure.util.AccountMapper;
import com.dev.user_transaction_management_system.use_case.dto.DepositRequest;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class DepositTransaction {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;

    public DepositTransaction(TransactionRepository transactionRepository,
                              AccountRepository accountRepository) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
        this.accountMapper = new AccountMapper();
    }

    @Transactional
    public Transaction deposit(DepositRequest depositRequest) {
        final AccountNumber fromAccountNumber = AccountNumber.of(depositRequest.fromAccountNumber());
        final AccountNumber toAccountNumber = AccountNumber.of(depositRequest.toAccountNumber());

        ensureSourceAndTargetAccountsAreDifferent(fromAccountNumber, toAccountNumber);


        final AccountEntity fromEntity = accountRepository.findByAccountNumber(fromAccountNumber)
                .orElseThrow(() -> CouldNotFindAccount.withAccountNumber(fromAccountNumber.toString()));

        final AccountEntity toEntity = accountRepository.findByAccountNumber(toAccountNumber)
                .orElseThrow(() -> CouldNotFindAccount.withAccountNumber(toAccountNumber.toString()));

        final Account from = accountMapper.toDomain(fromEntity);
        final Account to = accountMapper.toDomain(toEntity);
        final Amount amount = Amount.of(depositRequest.amount());

        from.ensureSufficientBalanceFor(amount);

        from.decreaseBalance(amount);
        to.increaseAmount(amount);
        final Transaction transaction = initiateTransaction(depositRequest);


        accountRepository.save(from.toEntity());
        accountRepository.save(to.toEntity());
        transactionRepository.save(transaction.toEntity());

        // return dto for user
        return transaction;
    }

    private static void ensureSourceAndTargetAccountsAreDifferent(AccountNumber fromAccountNumber,
                                                                  AccountNumber toAccountNumber) {
        if (fromAccountNumber.isSameAs(toAccountNumber)) {
            throw CouldNotProcessTransaction.becauseSourceAndTargetAccountsAreTheSame();
        }
    }

    private static Transaction initiateTransaction(DepositRequest depositRequest) {
        String referenceNumber = UUID.randomUUID().toString();
        return Transaction.of(
                0,
                AccountNumber.of(depositRequest.fromAccountNumber()),
                AccountNumber.of(depositRequest.toAccountNumber()),
                Amount.of(depositRequest.amount()),
                TransactionType.DEPOSIT,
                depositRequest.description(),
                referenceNumber,
                depositRequest.createdAt());
    }
}
