public class RaceConditionExample {
    public static void main(String[] args) throws InterruptedException {
        BankAccount acc = new BankAccount("Stoyo");

        Depositor[] depositors = new Depositor[5];
        for (int i = 0; i < depositors.length; i++) {
            depositors[i] = new Depositor(acc, 500);
            depositors[i].start();
        }

        for (int i = 0; i < depositors.length; i++) {
            depositors[i].join();
        }
        System.out.println(acc);
    }
}
