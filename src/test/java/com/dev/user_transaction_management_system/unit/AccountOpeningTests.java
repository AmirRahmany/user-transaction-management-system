package com.dev.user_transaction_management_system.unit;

import com.dev.user_transaction_management_system.application.AccountOpening;
import com.dev.user_transaction_management_system.domain.transaction.AccountNumber;
import com.dev.user_transaction_management_system.domain.transaction.Amount;
import com.dev.user_transaction_management_system.model.AccountEntity;
import com.dev.user_transaction_management_system.repository.AccountRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static com.dev.user_transaction_management_system.fake.AccountRequestFakeBuilder.accountRequest;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNoException;

@ExtendWith(MockitoExtension.class)
class AccountOpeningTests {

    private final AccountOpening accountOpening;

    public AccountOpeningTests() {
        accountOpening = new AccountOpening(new AccountRepositoryFake(), new AccountNumberGeneratorStubs());
    }

    @Test
    void open_an_account_successfully() {

        assertThatNoException().isThrownBy(() -> accountOpening.open(accountRequest().open()));
    }

    @Test
    void cannot_open_account_with_deposit_less_than_100_dolor() {

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> accountOpening.open(accountRequest().withBalance(80).open()));

    }

    @Test
    void cannot_open_account_without_any_user() {

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> accountOpening.open(accountRequest().withNoUser().open()));
    }

    @Test
    void cannot_open_account_with_duplicate_account_number() {
        accountOpening.open(accountRequest().open());

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> accountOpening.open(accountRequest().open()));
    }



    private static class AccountRepositoryFake implements AccountRepository {

        private final List<AccountEntity> records = new LinkedList<>();

        @Override
        public void save(AccountEntity accountEntity) {
            if (accountEntity == null)
                throw new IllegalArgumentException("account must not be empty");
            records.add(accountEntity);
        }

        @Override
        public Optional<AccountEntity> findById(Integer accountId) {
            return records.stream()
                    .filter(account -> account.hasSameAccountId(accountId)).findFirst();
        }

        @Override
        public void increaseBalance(Integer accountId, Amount amount) {

        }

        @Override
        public void decreaseBalance(Integer accountId, Amount amount) {

        }

        @Override
        public boolean accountNumberExists(AccountNumber accountNumber) {
            return records.stream().anyMatch(accountEntity -> accountEntity.getAccountNumber().equals(accountNumber.toString()));
        }
    }
}
