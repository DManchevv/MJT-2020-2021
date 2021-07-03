public class DaemonThreads {
    public static void main(String[] args) {
        BackgroundTask backgroundTask = new BackgroundTask();
        backgroundTask.start();

        System.out.println("Main thread terminates");
    }

}

class BackgroundTask extends Thread {
    public BackgroundTask() {
        setDaemon(true);
    }

    @Override
    public void run() {
        for (int i = 0; i < 1000; i++) {
            System.out.println(i);
        }
        System.out.printf("%s thread terminates", (isDaemon() ? "Daemon" : "Non-daemon"));
    }
}
