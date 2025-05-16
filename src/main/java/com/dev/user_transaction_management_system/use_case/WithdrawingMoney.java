package com.dev.user_transaction_management_system.use_case;

import com.dev.user_transaction_management_system.domain.bank_account.BankAccount;
import com.dev.user_transaction_management_system.domain.bank_account.AccountNumber;
import com.dev.user_transaction_management_system.domain.bank_account.BankAccountRepository;
import com.dev.user_transaction_management_system.domain.exceptions.CouldNotFindBankAccount;
import com.dev.user_transaction_management_system.domain.transaction.*;
import com.dev.user_transaction_management_system.infrastructure.persistence.model.BankAccountEntity;
import com.dev.user_transaction_management_system.infrastructure.util.BankAccountMapper;
import com.dev.user_transaction_management_system.use_case.dto.WithdrawalRequest;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class WithdrawingMoney {

    private final TransactionRepository transactionRepository;
    private final BankAccountRepository accountRepository;
    private final BankAccountMapper bankAccountMapper;

    public WithdrawingMoney(TransactionRepository transactionRepository,
                            BankAccountRepository bankAccountRepository) {

        this.transactionRepository = transactionRepository;
        this.accountRepository = bankAccountRepository;
        this.bankAccountMapper = new BankAccountMapper();
    }

    @Transactional
    public ReferenceNumber withdraw(WithdrawalRequest request) {
        AccountNumber fromAccountNumber = AccountNumber.of(request.fromAccountNumber());
        final BankAccount account = finAccountBy(request.fromAccountNumber());
        String referenceNumber = transactionRepository.generateReferenceNumber();


        final Amount amount = Amount.of(request.funds());
        account.decreaseBalance(amount);

        final Transaction transaction = Transaction.of(
                TransactionId.autoGenerateByDb(),
                fromAccountNumber,
                fromAccountNumber, //TODO refactor
                TransactionDetail.of(amount, TransactionType.WITHDRAWAL, request.description()),
                ReferenceNumber.fromString(referenceNumber),
                LocalDateTime.now());

        accountRepository.save(account.toEntity());
        transactionRepository.save(transaction.toEntity());
        return ReferenceNumber.fromString(referenceNumber);
    }

    private BankAccount finAccountBy(String reqAccountNumber) {
        final AccountNumber accountNumber = AccountNumber.of(reqAccountNumber);

        final BankAccountEntity bankAccountEntity = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> CouldNotFindBankAccount.withAccountNumber(accountNumber.toString()));

        return bankAccountMapper.toDomain(bankAccountEntity);
    }
}
