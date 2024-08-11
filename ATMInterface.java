import java.util.Scanner;

class BankAccount
{
    private double balance;

    public BankAccount(double initialBalance)
    {
        this.balance = initialBalance;
    }

    public double getBalance()
    {
        return balance;
    }

    public void deposit(double amount)
    {
        if (amount > 0)
        {
            balance += amount;
        }
    }

    public boolean withdraw(double amount)
    {
        if (amount > 0 && amount <= balance)
        {
            balance -= amount;
            return true;
        }
        return false;
    }
}

class ATM
{
    private BankAccount account;

    public ATM(BankAccount account)
    {
        this.account = account;
    }

    public void displayMenu()
    {
        System.out.println("ATM Menu:");
        System.out.println("1: Withdraw:");
        System.out.println("2: Deposit:");
        System.out.println("3: Check Balance:");
        System.out.println("4: Exit:");
    }

    public void run()
    {
        Scanner scanner = new Scanner(System.in);
        while (true)
        {
            displayMenu();
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            switch (choice)
            {
                case 1:
                    System.out.print("Enter the amount to withdraw: ");
                    double withdrawAmount = scanner.nextDouble();
                    if (account.withdraw(withdrawAmount))
                    {
                        System.out.println("Withdraw Successful.");
                    }
                    else
                    {
                        System.out.println("Insufficient Balance.");
                    }
                    break;

                case 2:
                    System.out.print("Enter the amount to deposit: ");
                    double depositAmount = scanner.nextDouble();
                    account.deposit(depositAmount);
                    System.out.println("Deposit Successful.");
                    break;

                case 3:
                    System.out.println("Your account balance is: " + account.getBalance());
                    break;

                case 4:
                    System.out.println("Thank you for using the ATM.");
                    return;

                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
        }
    }
}

public class ATMInterface
{
    public static void main(String[] args)
    {
        BankAccount userAccount = new BankAccount(1000.0);
        ATM atm = new ATM(userAccount);
        atm.run();
    }
}
