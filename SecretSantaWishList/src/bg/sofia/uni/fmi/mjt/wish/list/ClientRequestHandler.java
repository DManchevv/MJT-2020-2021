package bg.sofia.uni.fmi.mjt.wish.list;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class ClientRequestHandler implements Runnable {

    private final Socket socket;
    private final Map<String, List<String>> wishContainer;
    private final List<WishListUser> accounts;
    private final AtomicBoolean loggedIn;
    private final Commands commandMenu;
    private String currentUser;

    public ClientRequestHandler(Socket socket, Map<String, List<String>> wishContainer, List<WishListUser> accounts) {

        this.socket = socket;
        this.wishContainer = wishContainer;
        this.accounts = accounts;
        this.loggedIn = new AtomicBoolean(false);
        this.commandMenu = new Commands();

    }

    @Override
    public void run() {
        try (PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            String inputLine;
            while (!Thread.currentThread().isInterrupted()) {

                if (in.ready()) {
                    inputLine = in.readLine();

                    String[] lineWords = inputLine.split("\\s+");

                    if ("post-wish".equals(lineWords[0])) {

                        out.println(commandMenu.postWishCommand(accounts, lineWords, loggedIn, out, wishContainer));

                    } else if ("get-wish".equals(lineWords[0])) {

                        out.println(commandMenu.getWishCommand(loggedIn, out, wishContainer, currentUser));

                    } else if ("disconnect".equals(lineWords[0])) {

                        out.println("[ Disconnected from server ]");
                        break;

                    } else if ("register".equals(lineWords[0])) {

                        String result = commandMenu.registerCommand(accounts, lineWords, loggedIn, out);

                        if (result.contains("successfully")) {
                            currentUser = lineWords[1];
                        }

                        out.println(result);

                    } else if ("login".equals(lineWords[0])) {

                        String result = commandMenu.loginCommand(accounts, lineWords, loggedIn, out);

                        if (result.contains("successfully")) {
                            currentUser = lineWords[1];
                        }

                        out.println(result);

                    } else if ("logout".equals(lineWords[0])) {

                        out.println(commandMenu.logoutCommand(loggedIn, out));

                    } else {

                        out.println("[ Unknown command ]");

                    }

                }

            }

            loggedIn.set(false);
            out.println("[ Disconnected from server ]");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }
}