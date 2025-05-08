package com.dev.user_transaction_management_system.use_case;

import com.dev.user_transaction_management_system.domain.account.Account;
import com.dev.user_transaction_management_system.domain.account.AccountNumber;
import com.dev.user_transaction_management_system.domain.exceptions.CouldNotFindAccount;
import com.dev.user_transaction_management_system.domain.transaction.*;
import com.dev.user_transaction_management_system.domain.exceptions.CouldNotProcessTransaction;
import com.dev.user_transaction_management_system.domain.account.AccountRepository;
import com.dev.user_transaction_management_system.infrastructure.persistence.model.AccountEntity;
import com.dev.user_transaction_management_system.infrastructure.util.AccountMapper;
import com.dev.user_transaction_management_system.use_case.dto.DepositRequest;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class DepositingMoney {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private WithdrawingMoney withdrawingMoney;
    private final AccountMapper accountMapper;

    public DepositingMoney(TransactionRepository transactionRepository,
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

        final Account from = finAccountBy(fromAccountNumber);
        final Account to = finAccountBy(toAccountNumber);
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

    private Account finAccountBy(AccountNumber fromAccountNumber) {
        final AccountEntity fromEntity = accountRepository.findByAccountNumber(fromAccountNumber)
                .orElseThrow(() -> CouldNotFindAccount.withAccountNumber(fromAccountNumber.toString()));
        return accountMapper.toDomain(fromEntity);
    }

    private void
    ensureSourceAndTargetAccountsAreDifferent(AccountNumber fromAccountNumber, AccountNumber toAccountNumber) {
        if (fromAccountNumber.isSameAs(toAccountNumber)) {
            throw CouldNotProcessTransaction.becauseSourceAndTargetAccountsAreTheSame();
        }
    }

    private Transaction initiateTransaction(DepositRequest depositRequest) {
        String referenceNumber = UUID.randomUUID().toString();
        return Deposit.of(
                0,
                TransactionDetail.of(Amount.of(depositRequest.amount()),TransactionType.DEPOSIT, depositRequest.description()),
                AccountNumber.of(depositRequest.fromAccountNumber()),
                AccountNumber.of(depositRequest.toAccountNumber()),
                referenceNumber,
                depositRequest.createdAt());
    }
}
