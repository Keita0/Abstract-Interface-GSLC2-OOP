package AbstractAndInterface;

import java.util.*;

@SuppressWarnings("serial")
//exception handling
class InsufficientFundsException extends Exception {
    public InsufficientFundsException(String message) {
        super(message);
    }
}

// this interface defines the contract for bank account functionality
// don't contain any method implementation and only declare the method
// the class implementing the interface must provide implementation for every method define in the interface
interface BankAccount {
    void deposit(double amount);
    void withdraw(double amount) throws InsufficientFundsException;
    double getBalance();
}

// this abstract class will define the default implementation for deposit and balance retrieval
/*
   can have both abstract and non-abstract methods
   abstract -> declared without implementation and must be overridden by the subclass inheriting it
   non-abstract -> can be either inherited or overridden
*/
abstract class AbstractBankAccount implements BankAccount {
    protected double balance; // accessible in the same package
    
    @Override
    public void deposit(double amount) {
        balance += amount;
    }

    @Override
    public double getBalance() {
        return balance;
    }
    // concrete class can then provide their own implementation for withdraw {void withdraw(double amount) throws InsufficientFundsException;}
}

class SavingAccount extends AbstractBankAccount {
    private double interestRate;

    SavingAccount(double interestRate) {
        this.interestRate = interestRate;
    }

    public void addInterest() {
        balance += balance * interestRate;
        System.out.println("Total balance after interest : " + balance);
    }

    @Override
    public void withdraw(double amount) throws InsufficientFundsException { //different implementation for withdraw from the class checkingAccount
        if (balance < amount) {
            throw new InsufficientFundsException("Insufficient funds");
        }
        System.out.println(amount + " Withdrawn from a Saving Account");
        balance -= amount;
        System.out.println("Remaining Balance : " + balance);
    }
}

class CheckingAccount extends AbstractBankAccount {
    private double overdraftLimit;

    CheckingAccount(double overdraftLimit) {
        this.overdraftLimit = overdraftLimit;
    }

    @Override
    public void withdraw(double amount) throws InsufficientFundsException {
        if (balance + overdraftLimit < amount) {
            throw new InsufficientFundsException("Overdraft of " + overdraftLimit + " exceeded");
        }
        System.out.println(amount + " Withdrawn from a Checking Account");
        balance -= amount;
        System.out.println("Remaining Balance : " + balance);
    }
}


class Bank {
    private List<BankAccount> accounts;

    Bank() {
        accounts = new ArrayList<>();
    }

    public void addAccount(BankAccount account) {
        accounts.add(account);
    }

    public double getTotalBalance() {
        double total = 0;
        for (BankAccount account : accounts) {
            total += account.getBalance();
        }
        return total;
    }
}

public class BankingSim {
	public static void main(String[] args) {
		Bank bank = new Bank();
		
		SavingAccount savingAcc = new SavingAccount(0.07);
		CheckingAccount checkingAcc = new CheckingAccount(100);
		
		savingAcc.deposit(1000);
		checkingAcc.deposit(500);
		
		try {
            savingAcc.addInterest();
            savingAcc.withdraw(200);
            checkingAcc.withdraw(1500); // this should fail due to it surpassing the overdraft limit
        } catch (InsufficientFundsException e) {
            System.out.println(e.getMessage());
        }
		
		bank.addAccount(checkingAcc);
		bank.addAccount(savingAcc);
		
		System.out.println("Total balance: " + bank.getTotalBalance());
	}
}
