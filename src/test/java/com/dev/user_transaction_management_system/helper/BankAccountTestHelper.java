package com.dev.user_transaction_management_system.helper;

import com.dev.user_transaction_management_system.domain.bank_account.AccountStatus;
import com.dev.user_transaction_management_system.domain.bank_account.BankAccountRepository;
import com.dev.user_transaction_management_system.domain.bank_account.BankAccount;
import com.dev.user_transaction_management_system.fake.AccountFakeBuilder;
import com.dev.user_transaction_management_system.infrastructure.persistence.model.BankAccountEntity;
import com.dev.user_transaction_management_system.use_case.OpeningBankAccount;
import com.dev.user_transaction_management_system.use_case.dto.AccountRequest;
import com.dev.user_transaction_management_system.use_case.dto.OpeningAccountResponse;
import org.springframework.beans.factory.annotation.Autowired;

import static com.dev.user_transaction_management_system.fake.AccountFakeBuilder.anAccount;

public class BankAccountTestHelper {
    
    @Autowired
    protected BankAccountRepository accountRepository;

    protected BankAccount havingOpened(AccountFakeBuilder accountFakeBuilder) {
        final BankAccount account = accountFakeBuilder.open();

        accountRepository.save(account.toEntity());
        return account;
    }

    protected BankAccount havingEnabledAccount() {
        final BankAccount to = anAccount().open();
        final BankAccountEntity entity = to.toEntity();
        entity.setStatus(AccountStatus.ENABLE);
        accountRepository.save(entity);
        return to;
    }


}
