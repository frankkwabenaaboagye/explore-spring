package frank.springsecurity64;

public class BankAccount {

    public BankAccount(long id, String owner, String accountNumber, double balance) {
        this.id = id;
        this.owner = owner;
        this.accountNumber = accountNumber;
        this.balance = balance;
    }

    final long id;

    public long getId() {
        return id;
    }

    public String getOwner() {
        return owner;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public double getBalance() {
        return balance;
    }

    final String owner;
    final String accountNumber;
    final double balance;






}
