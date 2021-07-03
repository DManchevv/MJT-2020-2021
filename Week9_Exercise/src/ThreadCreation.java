public class ThreadCreation {
    public static void main(String[] args) throws InterruptedException {
        CustomThread customThread = new CustomThread("Custom#1");
        customThread.start();

        CustomThread customThread2 = new CustomThread("Custom#2");
        customThread2.start();

        Thread customThread3 = new Thread(new CustomRunnable());
        customThread3.start();

        Thread customThread4 = new Thread() {
            @Override
            public void run() {
                System.out.println("Hello world, from an anonymous class!");
            }
        };
        customThread4.start();

        Thread customThread5 = new Thread(() -> System.out.println("Hello world, from a lambda!"));
        customThread5.start();

        for (int i = 0; i < 10; i++) {
            Thread.sleep(1);
            System.out.println("Hello world from main thread");
        }
    }
}

class CustomThread extends Thread {
    private String name;
    public CustomThread(String name) {
        this.name = name;
    }

    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Hello world from custom " + name);
        }
    }
}

class CustomRunnable implements Runnable {
    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Hello world from runnable ");
        }
    }
}