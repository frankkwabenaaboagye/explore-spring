package frank.springsecurity64;

public class BankAccountServiceImpl implements BankAccountService {

    @Override
    public BankAccount findById(long id) {
        BankAccount account = new BankAccount(id, "Frank", "4990028101", 10000);

        return account;
    }

    @Override
    public BankAccount getById(long id) {
        return findById(id);
    }
}
