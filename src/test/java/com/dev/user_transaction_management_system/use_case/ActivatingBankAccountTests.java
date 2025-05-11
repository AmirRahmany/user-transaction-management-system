package com.dev.user_transaction_management_system.use_case;

import com.dev.user_transaction_management_system.domain.account.AccountNumber;
import com.dev.user_transaction_management_system.domain.account.BankAccount;
import com.dev.user_transaction_management_system.domain.account.BankAccountRepository;
import com.dev.user_transaction_management_system.domain.exceptions.CouldNotFindBankAccount;
import com.dev.user_transaction_management_system.fake.BankAccountRepositoryFake;
import com.dev.user_transaction_management_system.helper.BankAccountTestHelper;
import com.dev.user_transaction_management_system.infrastructure.persistence.model.BankAccountEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.dev.user_transaction_management_system.fake.AccountFakeBuilder.anAccount;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ActivatingBankAccountTests extends BankAccountTestHelper {

    private final ActivatingBankAccount activatingBankAccount;

    public ActivatingBankAccountTests() {
        super.accountRepository = new BankAccountRepositoryFake();
        activatingBankAccount = new ActivatingBankAccount(accountRepository);
    }

    @Test
    void activate_user_bank_account_successfully() {
        final BankAccount bankAccount = havingOpened(anAccount());

        assertThatNoException().isThrownBy(() -> activatingBankAccount.activate(bankAccount.accountNumber().toString()));
    }

    @Test
    void cant_un_existed_user_bank_account() {
        assertThatExceptionOfType(CouldNotFindBankAccount.class)
                .isThrownBy(() -> activatingBankAccount.activate("0300457896321"));
    }

    @Test
    void should_not_persist_when_account_is_already_enabled() {
        final BankAccount bankAccount = havingEnabledAccount();
        final BankAccountRepository repository = mock(BankAccountRepository.class);
        final ActivatingBankAccount accountActivation = new ActivatingBankAccount(repository);
        final AccountNumber accountNumber = bankAccount.accountNumber();

        final BankAccountEntity entity = bankAccount.toEntity();
        when(repository.findByAccountNumber(accountNumber)).thenReturn(Optional.of(entity));

        assertThatNoException().isThrownBy(() -> accountActivation.activate(accountNumber.toString()));
        verify(repository, times(0)).save(entity);
    }
}
