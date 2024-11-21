package frank.springsecurity64;

public class BankAccountServiceImpl {

    public BankAccount findById(Long id) {
        return new BankAccount(id, "Frank", "4990028101", 10000);
    }

    public BankAccount getById(Long id) {
        return findById(id);
    }
}
