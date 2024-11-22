package frank.springsecurity64;

public interface BankAccountService {

    @PostReadBankAccount
    BankAccount findById(long id);

    @PostReadBankAccount
    BankAccount getById(long id);
}
