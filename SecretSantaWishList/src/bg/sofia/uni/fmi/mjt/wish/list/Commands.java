package bg.sofia.uni.fmi.mjt.wish.list;

import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

public class Commands {

    public String registerCommand(List<WishListUser> accounts, String[] lineWords,
                                  AtomicBoolean loggedIn, PrintWriter out) {

        if (accounts == null || lineWords == null || loggedIn == null || out == null) {
            throw new IllegalArgumentException("Null argument in registerCommand method.");
        }

        if (loggedIn.get()) {
            return "[ You cannot use the register command when you are already logged in ]" + System.lineSeparator();
        }

        UniqueVerifier verifier = new UniqueVerifier();

        if (verifier.isUserAlreadyRegistered(accounts, lineWords[1])) {
            return "[ Username " + lineWords[1] + " is already taken, select another one ]" + System.lineSeparator();
        }

        if (lineWords[1]
                .replaceAll("[a-zA-Z0-9._-]", "")
                .equals("")) {

            accounts.add(new WishListUser(lineWords[1], lineWords[2]));
            loggedIn.set(true);
            return "[ Username " + lineWords[1] + " successfully registered ]" + System.lineSeparator();
        }

        return "[ Username " + lineWords[1] + " is invalid, select a valid one ]" + System.lineSeparator();
    }

    public String loginCommand(List<WishListUser> accounts, String[] lineWords,
                               AtomicBoolean loggedIn, PrintWriter out) {

        if (accounts == null || lineWords == null || loggedIn == null || out == null) {
            throw new IllegalArgumentException("Null argument in loginCommand method.");
        }

        if (loggedIn.get()) {
            return "[ You cannot use the login command when you are already logged in ]" + System.lineSeparator();
        }

        if (accounts.contains(new WishListUser(lineWords[1], lineWords[2]))) {
            loggedIn.set(true);
            return "[ User " + lineWords[1] + " successfully logged in ]" + System.lineSeparator();
        }

        return "[ Invalid username/password combination ]" + System.lineSeparator();
    }

    public String logoutCommand(AtomicBoolean loggedIn, PrintWriter out) {

        if (loggedIn == null || out == null) {
            throw new IllegalArgumentException("Null argument in logoutCommand method.");
        }

        if (!loggedIn.get()) {
            return "[ You are not logged in ]" + System.lineSeparator();
        }

        loggedIn.set(false);
        return "[ Successfully logged out ]" + System.lineSeparator();
    }

    public String postWishCommand(List<WishListUser> accounts, String[] lineWords,
                                  AtomicBoolean loggedIn, PrintWriter out, Map<String, List<String>> wishContainer) {

        if (accounts == null || lineWords == null || loggedIn == null || out == null || wishContainer == null) {
            throw new IllegalArgumentException("Null argument in postWishCommand method.");
        }

        if (!loggedIn.get()) {
            return "[ You are not logged in ]" + System.lineSeparator();
        }

        UniqueVerifier verifier = new UniqueVerifier();

        if (!verifier.isUserAlreadyRegistered(accounts, lineWords[1])) {
            return "[ Student with username " + lineWords[1] + " is not registered ]" + System.lineSeparator();
        }

        String student = lineWords[1];
        String gift = lineWords[2];

        if (wishContainer.containsKey(student) && wishContainer.get(student).contains(gift)) {
            return "[ The same gift for student " + student + " was already submitted ]" + System.lineSeparator();
        }

        if (wishContainer.get(student) == null) {
            List<String> giftList = new LinkedList<>();
            giftList.add(gift);
            wishContainer.put(student, giftList);

        } else {

            List<String> giftList = new LinkedList<>(wishContainer.get(student));
            giftList.add(gift);
            wishContainer.put(student, giftList);
        }

        return "[ Gift " + gift + " for student " + student + " submitted successfully ]" + System.lineSeparator();
    }

    public String getWishCommand(AtomicBoolean loggedIn, PrintWriter out,
                                 Map<String, List<String>> wishContainer, String currentUser) {

        if (loggedIn == null || out == null || wishContainer == null || currentUser == null) {
            throw new IllegalArgumentException("Null argument in getWishCommand method.");
        }

        if (!loggedIn.get()) {
            return "[ You are not logged in ]" + System.lineSeparator();
        }

        if (wishContainer.isEmpty()) {
            return "[ There are no students present in the wish list ]" + System.lineSeparator();
        }

        int size = wishContainer.size();
        int item = new Random().nextInt(size);
        int i = 0;

        if (wishContainer.containsKey(currentUser) && item == size - 1) {
            item--;
        }

        for (Map.Entry<String, List<String>> pair : wishContainer.entrySet()) {

            if (i == item && !pair.getKey().equals(currentUser)) {
                wishContainer.remove(pair.getKey());
                return "[ " + pair.getKey() + ": " + pair.getValue() + " ]" + System.lineSeparator();
            } else {
                item++;
            }

            i++;
        }

        return  "[ There are no students present in the wish list ]" + System.lineSeparator();
    }

}
