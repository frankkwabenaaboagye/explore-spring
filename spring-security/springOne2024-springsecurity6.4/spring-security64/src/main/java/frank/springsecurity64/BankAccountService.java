package frank.springsecurity64;

public interface BankAccountService {
    BankAccount findById(long id);

    BankAccount getById(long id);
}
