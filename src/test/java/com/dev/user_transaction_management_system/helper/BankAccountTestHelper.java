package com.dev.user_transaction_management_system.helper;

import com.dev.user_transaction_management_system.domain.bank_account.AccountNumber;
import com.dev.user_transaction_management_system.domain.bank_account.AccountStatus;
import com.dev.user_transaction_management_system.domain.bank_account.BankAccountRepository;
import com.dev.user_transaction_management_system.domain.bank_account.BankAccount;
import com.dev.user_transaction_management_system.domain.exceptions.CouldNotFindBankAccount;
import com.dev.user_transaction_management_system.test_builder.BankAccountTestBuilder;
import com.dev.user_transaction_management_system.infrastructure.persistence.model.BankAccountEntity;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;

import static com.dev.user_transaction_management_system.test_builder.BankAccountTestBuilder.anAccount;


@Component
public class BankAccountTestHelper {

    private final BankAccountRepository accountRepository;

    public BankAccountTestHelper(BankAccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Transactional
    public BankAccount havingOpened(BankAccountTestBuilder bankAccountTestBuilder) {
        final BankAccount account = bankAccountTestBuilder.open();

        accountRepository.save(account.toEntity());
        return account;
    }

    @Transactional
    public BankAccount havingEnabledAccount() {
        final BankAccount to = anAccount().open();
        final BankAccountEntity entity = to.toEntity();
        entity.setStatus(AccountStatus.ENABLE);
        accountRepository.save(entity);
        return to;
    }


    public BankAccountEntity findByAccountNumber(AccountNumber accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber).orElseThrow(CouldNotFindBankAccount::new);
    }
}
