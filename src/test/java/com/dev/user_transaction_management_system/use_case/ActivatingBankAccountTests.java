package com.dev.user_transaction_management_system.use_case;

import com.dev.user_transaction_management_system.domain.bank_account.AccountNumber;
import com.dev.user_transaction_management_system.domain.bank_account.BankAccountRepository;
import com.dev.user_transaction_management_system.domain.exceptions.CouldNotFindBankAccount;
import com.dev.user_transaction_management_system.fake.BankAccountRepositoryFake;
import com.dev.user_transaction_management_system.fake.FakeEventPublisher;
import com.dev.user_transaction_management_system.helper.BankAccountTestHelper;
import com.dev.user_transaction_management_system.infrastructure.persistence.model.BankAccountEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.dev.user_transaction_management_system.test_builder.BankAccountTestBuilder.anAccount;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ActivatingBankAccountTests {

    private final BankAccountTestHelper bankAccountHelper;
    private final ActivatingBankAccount activatingBankAccount;
    private final FakeEventPublisher eventPublisher;

    public ActivatingBankAccountTests() {
        BankAccountRepository accountRepository = new BankAccountRepositoryFake();
        this.bankAccountHelper = new BankAccountTestHelper(accountRepository);
        eventPublisher = new FakeEventPublisher();
        activatingBankAccount = new ActivatingBankAccount(accountRepository, eventPublisher);
    }

    @Test
    void activate_user_bank_account_successfully() {
        var bankAccount = bankAccountHelper.havingOpened(anAccount());

        assertThatNoException().isThrownBy(() -> activatingBankAccount.activate(bankAccount.accountNumber().toString()));
    }

    @Test
    void cant_activate_un_existed_user_bank_account() {
        assertThatExceptionOfType(CouldNotFindBankAccount.class)
                .isThrownBy(() -> activatingBankAccount.activate("0300457896321"));
    }

    @Test
    void does_not_reactivate_already_enabled_bank_account() {
        var bankAccount = bankAccountHelper.havingEnabledAccount();
        final BankAccountRepository repository = mock(BankAccountRepository.class);
        final ActivatingBankAccount accountActivation = new ActivatingBankAccount(repository,eventPublisher);
        final AccountNumber accountNumber = bankAccount.accountNumber();

        final BankAccountEntity entity = bankAccount.toEntity();
        when(repository.findByAccountNumber(accountNumber)).thenReturn(Optional.of(entity));

        assertThatNoException().isThrownBy(() -> accountActivation.activate(accountNumber.toString()));
        verify(repository, times(0)).save(entity);
    }
}
