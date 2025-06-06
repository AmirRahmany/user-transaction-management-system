package com.dev.user_transaction_management_system.use_case.activate_bank_account;

import com.dev.user_transaction_management_system.domain.bank_account.AccountNumber;
import com.dev.user_transaction_management_system.domain.bank_account.BankAccount;
import com.dev.user_transaction_management_system.domain.bank_account.BankAccountRepository;
import com.dev.user_transaction_management_system.domain.exceptions.CouldNotFindBankAccount;
import com.dev.user_transaction_management_system.infrastructure.persistence.model.BankAccountEntity;
import com.dev.user_transaction_management_system.infrastructure.util.mapper.BankAccountMapper;
import io.jsonwebtoken.lang.Assert;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
public class ActivateBankAccount {

    private final BankAccountRepository repository;

    private final ApplicationEventPublisher eventPublisher;
    private final BankAccountMapper bankAccountMapper;

    @Autowired
    public ActivateBankAccount(BankAccountRepository repository,
                               ApplicationEventPublisher eventPublisher) {
        this.repository = repository;
        this.eventPublisher = eventPublisher;
        this.bankAccountMapper = new BankAccountMapper();
    }

    @Transactional
    public void activate(BankAccountActivationRequest request) {
        Assert.notNull(request,"bank account request cannot be null.");

        final AccountNumber bankAccountNumber = AccountNumber.of(request.accountNumber());
        final BankAccountEntity bankAccountEntity = findBankAccountBy(bankAccountNumber);
        final BankAccount bankAccount = bankAccountMapper.toDomain(bankAccountEntity);

        bankAccount.enable();

        repository.save(bankAccount.toEntity());
        bankAccount.releaseEvents().forEach(eventPublisher::publishEvent);
    }

    private BankAccountEntity findBankAccountBy(AccountNumber bankAccountNumber) {
        return repository.findByAccountNumber(bankAccountNumber)
                .orElseThrow(() -> CouldNotFindBankAccount.withAccountNumber(bankAccountNumber.toString()));
    }
}
