package frank.springsecurity64;

import org.springframework.security.access.prepost.PreAuthorize;

public interface BankAccountService {

    @PostReadBankAccount
    BankAccount findById(long id);

    @PostReadBankAccount
    BankAccount getById(long id);

    @PreAuthorize("#bankAccountToSave?.owner == authentication?.name")
    default void saveBankAccount(BankAccount bankAccountToSave) {}

    @PreAuthorize("#bankAccountToUpdate?.owner == authentication?.name")
    default void updateBankAccount(BankAccount bankAccountToUpdate) {}
}
