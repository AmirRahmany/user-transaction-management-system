package com.dev.user_transaction_management_system.helper;

import com.dev.user_transaction_management_system.domain.account.BankAccountRepository;
import com.dev.user_transaction_management_system.domain.account.BankAccount;
import com.dev.user_transaction_management_system.fake.AccountFakeBuilder;
import org.springframework.beans.factory.annotation.Autowired;

public class BankAccountTestHelper {
    
    @Autowired
    protected BankAccountRepository accountRepository;

    protected BankAccount havingOpened(AccountFakeBuilder accountFakeBuilder) {
        final BankAccount to = accountFakeBuilder.open();
        accountRepository.save(to.toEntity());
        return to;
    }
}
