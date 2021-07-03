package bg.sofia.uni.fmi.mjt.wish.list;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class TodoistServer {

    private static int SERVER_PORT = 4444;
    private static final int MAX_EXECUTOR_THREADS = 10;
    private Map<String, Collaboration> collaborations;
    private boolean exit;
    private final ExecutorService executor = Executors.newFixedThreadPool(MAX_EXECUTOR_THREADS);

    public TodoistServer(int port) {

        SERVER_PORT = port;
        this.collaborations = new ConcurrentHashMap<>();
        this.exit = false;

    }

    public void start() {

        try (ServerSocket serverSocket = new ServerSocket(SERVER_PORT);) {

            System.out.println("Server started and listening for connect requests");

            Socket clientSocket;

            while (!exit) {

                try {

                    serverSocket.setSoTimeout(2000);
                    clientSocket = serverSocket.accept();
                    clientSocket.setSoTimeout(2000);
                    System.out.println("Accepted connection request from client" + clientSocket.getInetAddress());
                    ClientRequestHandler clientHandler =
                            new ClientRequestHandler(clientSocket, collaborations);

                    executor.execute(clientHandler);

                } catch (InterruptedIOException ignored) {
                    // We are using the catch block only to catch the exception thrown by setSoTimeout and
                    // our program can continue its work properly.
                }

            }

        } catch (IOException e) {
            System.out.println("There is a problem with the server socket");
            e.printStackTrace();
        }

    }

    public void stop() {
        executor.shutdown();

        try {

            if (!executor.awaitTermination(800, TimeUnit.MILLISECONDS)) {
                executor.shutdownNow();
            }

        } catch (InterruptedException e) {
            executor.shutdownNow();
        }

        exit = true;
    }

    public static void main(String[] args) {

        TodoistServer server = new TodoistServer(4444);
        CustomThread thread = new CustomThread(server);
        thread.start();

        try {
            Thread.sleep(60000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        server.stop();
        thread.interrupt();
    }

}

