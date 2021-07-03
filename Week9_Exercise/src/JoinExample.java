public class JoinExample {
    public static void main(String[] args) {
        TimerBomb bomb = new TimerBomb();
        bomb.start();

        try {
            bomb.join();
        } catch (InterruptedException e) {

        }

        System.out.println("BOOOOOOOOOM!");
    }

}

class TimerBomb extends Thread {
    private String[] count = {"five", "four", "three", "two", "one"};

    @Override
    public void run() {
        for (String c : count) {
            System.out.println(c);
        }
    }
}
