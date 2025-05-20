package com.dev.user_transaction_management_system.use_case;

import com.dev.user_transaction_management_system.domain.bank_account.AccountNumber;
import com.dev.user_transaction_management_system.domain.bank_account.BankAccount;
import com.dev.user_transaction_management_system.domain.bank_account.BankAccountRepository;
import com.dev.user_transaction_management_system.domain.exceptions.CouldNotFindBankAccount;
import com.dev.user_transaction_management_system.domain.user.FullName;
import com.dev.user_transaction_management_system.domain.user.UserRepository;
import com.dev.user_transaction_management_system.infrastructure.persistence.model.BankAccountEntity;
import com.dev.user_transaction_management_system.infrastructure.util.BankAccountMapper;
import com.dev.user_transaction_management_system.use_case.event.BankAccountActivated;
import com.dev.user_transaction_management_system.use_case.event.UserAccountActivated;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
public class ActivatingBankAccount {

    private final BankAccountRepository repository;

    private final ApplicationEventPublisher eventPublisher;
    private final BankAccountMapper bankAccountMapper;

    @Autowired
    public ActivatingBankAccount(BankAccountRepository repository,
                                 ApplicationEventPublisher eventPublisher) {
        this.repository = repository;
        this.eventPublisher = eventPublisher;
        this.bankAccountMapper = new BankAccountMapper();
    }

    @Transactional
    public void activate(String accountNumber) {
        final AccountNumber bankAccountNumber = AccountNumber.of(accountNumber);
        final BankAccountEntity bankAccountEntity = findBankAccountBy(bankAccountNumber);
        final BankAccount bankAccount = bankAccountMapper.toDomain(bankAccountEntity);

        bankAccount.enable();

        repository.save(bankAccount.toEntity());
        bankAccount.recordEvents().forEach(eventPublisher::publishEvent);
    }

    private BankAccountEntity findBankAccountBy(AccountNumber bankAccountNumber) {
        return repository.findByAccountNumber(bankAccountNumber)
                .orElseThrow(() -> CouldNotFindBankAccount.withAccountNumber(bankAccountNumber.toString()));
    }
}
