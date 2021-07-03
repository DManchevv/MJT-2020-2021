package bg.sofia.uni.fmi.mjt.wish.list;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class WishListClient {

    private static final int SERVER_PORT = 4444;
    private static final String SERVER_HOST = "localhost";
    private static final String DISCONNECTED = "[ Disconnected from server ]";

    public static void main(String[] args) {

        try (Socket socket = new Socket(SERVER_HOST, SERVER_PORT);
             PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             BufferedReader in = new BufferedReader(new InputStreamReader(System.in))) {

            System.out.println("Connected to the server.");
            System.out.println("Enter message: ");

            while (true) {

                if (in.ready()) {
                    String message = in.readLine();
                    writer.println(message);
                }

                if (reader.ready()) {
                    String reply = reader.readLine();
                    System.out.println(reply);

                    if (reply.equals(DISCONNECTED)) {
                        break;
                    }

                    if (reply.equals("")) {
                        System.out.println("Enter message: ");
                    }

                }

            }

        } catch (IOException e) {
            System.out.println("There is a problem with the network communication");
            e.printStackTrace();
        }
    }
}
