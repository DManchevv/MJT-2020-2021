public class WaitNotifyExample {
    public static void main(String[] args) {
        CreditBankAccount account = new CreditBankAccount("Student");
    }
}

class CreditBankAccount extends BankAccount {
    public CreditBankAccount(String name) {
        super(name, 0);
    }

    @Override
    public synchronized void deposit(double amount) {
        super.deposit(amount);
        this.notifyAll();
    }

    public synchronized void withdrawCreditPayment(double monthFee) {
        while (balance < monthFee) {
            System.out.println("Not enough money, thread will wait");
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        withdraw(monthFee);
        System.out.println("Successfully withdraw money. Remaining balance " + balance);
    }
}

