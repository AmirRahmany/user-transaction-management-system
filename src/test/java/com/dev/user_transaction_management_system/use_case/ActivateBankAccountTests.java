package com.dev.user_transaction_management_system.use_case;

import com.dev.user_transaction_management_system.domain.bank_account.AccountNumber;
import com.dev.user_transaction_management_system.domain.bank_account.BankAccountRepository;
import com.dev.user_transaction_management_system.domain.exceptions.CouldNotFindBankAccount;
import com.dev.user_transaction_management_system.fake.BankAccountRepositoryFake;
import com.dev.user_transaction_management_system.fake.FakeEventPublisher;
import com.dev.user_transaction_management_system.helper.BankAccountTestHelper;
import com.dev.user_transaction_management_system.infrastructure.persistence.model.BankAccountEntity;
import com.dev.user_transaction_management_system.test_builder.FakeAccount;
import com.dev.user_transaction_management_system.test_builder.FakeAccount.Bank;
import com.dev.user_transaction_management_system.use_case.activate_bank_account.ActivateBankAccount;
import com.dev.user_transaction_management_system.use_case.activate_bank_account.BankAccountActivationRequest;
import com.dev.user_transaction_management_system.use_case.activate_user_account.UserActivationRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.dev.user_transaction_management_system.test_builder.BankAccountTestBuilder.anAccount;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ActivateBankAccountTests {

    private final BankAccountTestHelper bankAccountHelper;
    private final ActivateBankAccount activateBankAccount;
    private final FakeEventPublisher eventPublisher;

    public ActivateBankAccountTests() {
        BankAccountRepository accountRepository = new BankAccountRepositoryFake();
        this.bankAccountHelper = new BankAccountTestHelper(accountRepository);
        eventPublisher = new FakeEventPublisher();
        activateBankAccount = new ActivateBankAccount(accountRepository, eventPublisher);
    }

    @Test
    void activate_user_bank_account_successfully() {
        var bankAccount = bankAccountHelper.havingOpened(anAccount());
        final var activationRequest = createActivationRequestFor(bankAccount.accountNumber().toString());

        assertThatNoException().isThrownBy(() -> activateBankAccount.activate(activationRequest));
    }

    @Test
    void cant_activate_un_existed_user_bank_account() {
        final BankAccountActivationRequest activationRequest = createActivationRequestFor(Bank.UNKNOWN);
        assertThatExceptionOfType(CouldNotFindBankAccount.class)
                .isThrownBy(() -> activateBankAccount.activate(activationRequest));
    }

    @Test
    void does_not_reactivate_already_enabled_bank_account() {
        var bankAccount = bankAccountHelper.havingEnabledAccount();
        final BankAccountRepository repository = mock(BankAccountRepository.class);
        final ActivateBankAccount accountActivation = new ActivateBankAccount(repository, eventPublisher);
        final AccountNumber accountNumber = bankAccount.accountNumber();
        final BankAccountActivationRequest activationRequest = createActivationRequestFor(accountNumber.toString());

        final BankAccountEntity entity = bankAccount.toEntity();
        when(repository.findByAccountNumber(accountNumber)).thenReturn(Optional.of(entity));

        assertThatNoException().isThrownBy(() -> accountActivation.activate(activationRequest));
        verify(repository, times(0)).save(entity);
    }

    private static BankAccountActivationRequest createActivationRequestFor(String accountNumber) {
        return new BankAccountActivationRequest(accountNumber);
    }
}
