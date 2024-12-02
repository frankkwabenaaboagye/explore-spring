package frank.springsecurity64;

import org.springframework.security.access.prepost.PreAuthorize;

public interface BankAccountService {

    @PostReadBankAccount
    BankAccount findById(long id);

    @PostReadBankAccount
    BankAccount getById(long id);

    @PreWriteBankAccount(theVariable = "#bankAccountToSave")
    default void saveBankAccount(BankAccount bankAccountToSave) {}

    @PreWriteBankAccount(theVariable = "#bankAccountToUpdate")
    default void updateBankAccount(BankAccount bankAccountToUpdate) {}
}
