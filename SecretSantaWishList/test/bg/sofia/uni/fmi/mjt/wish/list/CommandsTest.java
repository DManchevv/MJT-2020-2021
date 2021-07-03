package bg.sofia.uni.fmi.mjt.wish.list;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class CommandsTest {

    private static Commands command;
    private static PrintWriter out;

    @Before
    public void setUp() {
        command = new Commands();
        out = new PrintWriter(System.out);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRegisterCommandWithAccountsNullArgument() {
        command.registerCommand(null, new String[]{}, new AtomicBoolean(false), out);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRegisterCommandWithLineWordsNullArgument() {
        List<WishListUser> accounts = new CopyOnWriteArrayList<>();

        command.registerCommand(accounts, null, new AtomicBoolean(false), out);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRegisterCommandWithLoggedInNullArgument() {
        List<WishListUser> accounts = new CopyOnWriteArrayList<>();

        command.registerCommand(accounts, new String[]{}, null, out);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRegisterCommandWithPrintWriterNullArgument() {
        List<WishListUser> accounts = new CopyOnWriteArrayList<>();

        command.registerCommand(accounts, new String[]{}, new AtomicBoolean(false), null);
    }

    @Test
    public void testRegisterCommandWithAlreadyTakenUsername() {
        List<WishListUser> accounts = new CopyOnWriteArrayList<>();
        String assertMessage = "Appropriate message for already taken username is returned.";
        String expected = "[ Username Kaloyan is already taken, select another one ]" + System.lineSeparator();

        accounts.add(new WishListUser("Kaloyan", "12345"));

        assertEquals(assertMessage, expected, command.registerCommand(accounts,
                new String[]{"register", "Kaloyan", "qwerty"}, new AtomicBoolean(false), out));
    }

    @Test
    public void testRegisterCommandWhileLoggedIn() {
        List<WishListUser> accounts = new CopyOnWriteArrayList<>();
        String assertMessage = "Appropriate message for user already logged in is returned.";
        String expected = "[ You cannot use the register command when you are already logged in ]"
                + System.lineSeparator();

        assertEquals(assertMessage, expected, command.registerCommand(accounts, new String[]{"register", ""},
                new AtomicBoolean(true), out));
    }

    @Test
    public void testRegisterCommandWithUsernameContainingIllegalSymbols() {
        List<WishListUser> accounts = new CopyOnWriteArrayList<>();
        String invalidUsername = "!@Pe$0";
        String assertMessage = "Appropriate message for illegal symbols in username is returned.";
        String expected = "[ Username " + invalidUsername + " is invalid, select a valid one ]"
                + System.lineSeparator();

        assertEquals(assertMessage, expected, command.registerCommand(accounts,
                new String[]{"register", invalidUsername, "1234"}, new AtomicBoolean(false), out));
    }

    @Test
    public void testRegisterCommandWithUsernameContainingSpecialAllowedSymbols() {
        List<WishListUser> accounts = new CopyOnWriteArrayList<>();
        String validUsername = "Blizzard_Entertainment-1991-Inc.";
        String assertMessage = "Successfully registered the user and returned appropriate message.";
        String expected = "[ Username " + validUsername + " successfully registered ]" + System.lineSeparator();

        assertEquals(assertMessage, expected, command.registerCommand(accounts,
                new String[]{"register", validUsername, "irvineUSA91"}, new AtomicBoolean(false), out));
    }

    @Test
    public void testRegisterCommandWithNormalUsername() {
        List<WishListUser> accounts = new CopyOnWriteArrayList<>();
        String validUsername = "Mitko31";
        String assertMessage = "Successfully registered the user and appropriate message returned.";
        String expected = "[ Username " + validUsername + " successfully registered ]" + System.lineSeparator();

        assertEquals(assertMessage, expected, command.registerCommand(accounts,
                new String[]{"register", validUsername, "31111"}, new AtomicBoolean(false), out));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLoginCommandWithAccountsNullArgument() {
        command.loginCommand(null, new String[]{}, new AtomicBoolean(false), out);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLoginCommandWithLineWordsNullArgument() {
        List<WishListUser> accounts = new CopyOnWriteArrayList<>();

        command.loginCommand(accounts, null, new AtomicBoolean(false), out);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLoginCommandWithLoggedInNullArgument() {
        List<WishListUser> accounts = new CopyOnWriteArrayList<>();

        command.loginCommand(accounts, new String[]{}, null, out);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLoginCommandWithPrintWriterNullArgument() {
        List<WishListUser> accounts = new CopyOnWriteArrayList<>();

        command.loginCommand(accounts, new String[]{}, new AtomicBoolean(false), null);
    }

    @Test
    public void testLoginCommandWhileLoggedIn() {
        List<WishListUser> accounts = new CopyOnWriteArrayList<>();
        String assertMessage = "Appropriate message for user already logged in is returned.";
        String expected = "[ You cannot use the login command when you are already logged in ]"
                + System.lineSeparator();

        assertEquals(assertMessage, expected, command.loginCommand(accounts, new String[]{"login", ""},
                new AtomicBoolean(true), out));
    }

    @Test
    public void testLoginCommandWithNonexistentUser() {
        List<WishListUser> accounts = new CopyOnWriteArrayList<>();
        String assertMessage = "Appropriate message for non-existent user is returned.";
        String expected = "[ Invalid username/password combination ]" + System.lineSeparator();

        assertEquals(assertMessage, expected, command.loginCommand(accounts,
                new String[]{"login", "Mohamed", "kekw123"}, new AtomicBoolean(false), out));
    }

    @Test
    public void testLoginCommandWithInvalidPassword() {
        List<WishListUser> accounts = new CopyOnWriteArrayList<>();
        String assertMessage = "Appropriate message for invalid password is returned.";
        String expected = "[ Invalid username/password combination ]" + System.lineSeparator();

        accounts.add(new WishListUser("Stefan", "stef123"));

        assertEquals(assertMessage, expected, command.loginCommand(accounts,
                new String[]{"login", "Stefan", "lordship"}, new AtomicBoolean(false), out));
    }

    @Test
    public void testLoginCommandWithValidCredentials() {
        List<WishListUser> accounts = new CopyOnWriteArrayList<>();
        String assertMessage = "Successfully logged in the user and returned appropriate message.";
        String expected = "[ User Zdravko successfully logged in ]" + System.lineSeparator();

        accounts.add(new WishListUser("Zdravko", "Abcd1234"));

        assertEquals(assertMessage, expected, command.loginCommand(accounts,
                new String[]{"login", "Zdravko", "Abcd1234"}, new AtomicBoolean(false), out));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLogoutCommandWithLoggedInNullArgument() {
        command.logoutCommand(null, out);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLogoutCommandWithPrintWriterNullArgument() {
        command.logoutCommand(new AtomicBoolean(false), null);
    }

    @Test
    public void testLogoutCommandWhileNotLoggedIn() {
        String assertMessage = "Appropriate message for user not being logged in returned.";
        String expected = "[ You are not logged in ]" + System.lineSeparator();

        assertEquals(assertMessage, expected, command.logoutCommand(new AtomicBoolean(false), out));
    }

    @Test
    public void testLogoutCommandWhileLoggedIn() {
        String assertMessage = "Logout successfully and return appropriate message.";
        String expected = "[ Successfully logged out ]" + System.lineSeparator();

        assertEquals(assertMessage, expected, command.logoutCommand(new AtomicBoolean(true), out));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPostWishCommandWithAccountsNullArgument() {
        Map<String, List<String>> wishContainer = new ConcurrentHashMap<>();

        command.postWishCommand(null, new String[]{}, new AtomicBoolean(false), out, wishContainer);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPostWishCommandWithLineWordsNullArgument() {
        List<WishListUser> accounts = new CopyOnWriteArrayList<>();
        Map<String, List<String>> wishContainer = new ConcurrentHashMap<>();

        command.postWishCommand(accounts, null, new AtomicBoolean(false), out, wishContainer);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPostWishCommandWithLoggedInNullArgument() {
        List<WishListUser> accounts = new CopyOnWriteArrayList<>();
        Map<String, List<String>> wishContainer = new ConcurrentHashMap<>();

        command.postWishCommand(accounts, new String[]{}, null, out, wishContainer);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPostWishCommandWithPrintWriterNullArgument() {
        List<WishListUser> accounts = new CopyOnWriteArrayList<>();
        Map<String, List<String>> wishContainer = new ConcurrentHashMap<>();

        command.postWishCommand(accounts, new String[]{}, new AtomicBoolean(false), null, wishContainer);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPostWishCommandWithWishContainerNullArgument() {
        List<WishListUser> accounts = new CopyOnWriteArrayList<>();

        command.postWishCommand(accounts, new String[]{}, new AtomicBoolean(false), out, null);
    }

    @Test
    public void testPostWishCommandWhileNotLoggedIn() {
        List<WishListUser> accounts = new CopyOnWriteArrayList<>();
        Map<String, List<String>> wishContainer = new ConcurrentHashMap<>();
        String assertMessage = "Appropriate message for user not being logged in returned.";
        String expected = "[ You are not logged in ]" + System.lineSeparator();

        assertEquals(assertMessage, expected, command.postWishCommand(accounts, new String[]{"post-wish", "a", "b"},
                new AtomicBoolean(false), out, wishContainer));
    }

    @Test
    public void testPostWishCommandWithNotRegisteredReceiver() {
        List<WishListUser> accounts = new CopyOnWriteArrayList<>();
        Map<String, List<String>> wishContainer = new ConcurrentHashMap<>();
        String assertMessage = "Appropriate message for student not registered returned.";
        String expected = "[ Student with username Zdravko is not registered ]" + System.lineSeparator();

        assertEquals(assertMessage, expected, command.postWishCommand(accounts,
                new String[]{"post-wish", "Zdravko", "parcal"}, new AtomicBoolean(true), out, wishContainer));
    }

    @Test
    public void testPostWishCommandWithGiftAlreadySubmittedToReceiver() {
        List<WishListUser> accounts = new CopyOnWriteArrayList<>();
        Map<String, List<String>> wishContainer = new ConcurrentHashMap<>();
        String assertMessage = "Appropriate message for already submitted gift to this user returned.";
        String expected = "[ The same gift for student Mitko was already submitted ]" + System.lineSeparator();

        accounts.add(new WishListUser("Mitko", "123456"));
        wishContainer.put("Mitko", List.of("kolelo", "marulq", "sirene"));

        assertEquals(assertMessage, expected, command.postWishCommand(accounts,
                new String[]{"post-wish", "Mitko", "kolelo"}, new AtomicBoolean(true), out, wishContainer));
    }

    @Test
    public void testPostWishCommandWhenSubmittingGiftToUserWithNoSubmittedGiftsYet() {
        List<WishListUser> accounts = new CopyOnWriteArrayList<>();
        Map<String, List<String>> wishContainer = new ConcurrentHashMap<>();
        String assertMessage = "Appropriate message for correct execution of the method should be returned.";
        String expected = "[ Gift kolelo for student Zdravko submitted successfully ]" + System.lineSeparator();

        accounts.add(new WishListUser("Zdravko", "555555"));

        assertEquals(assertMessage, expected, command.postWishCommand(accounts,
                new String[]{"post-wish", "Zdravko", "kolelo"}, new AtomicBoolean(true), out, wishContainer));
    }

    @Test
    public void testPostWishCommandWhenSubmittingGiftToUserWithMoreThanOneSubmittedGifts() {
        List<WishListUser> accounts = new CopyOnWriteArrayList<>();
        Map<String, List<String>> wishContainer = new ConcurrentHashMap<>();
        String assertMessage = "Appropriate message for correct execution of the method should be returned.";
        String expected = "[ Gift kolelo for student Zdravko submitted successfully ]" + System.lineSeparator();

        accounts.add(new WishListUser("Zdravko", "555555"));
        wishContainer.put("Zdravko", List.of("hilka"));

        assertEquals(assertMessage, expected, command.postWishCommand(accounts,
                new String[]{"post-wish", "Zdravko", "kolelo"}, new AtomicBoolean(true), out, wishContainer));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetWishCommandWithLoggedInNullArgument() {
        Map<String, List<String>> wishContainer = new ConcurrentHashMap<>();

        command.getWishCommand(null, out, wishContainer, "a");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetWishCommandWithPrintWriterNullArgument() {
        Map<String, List<String>> wishContainer = new ConcurrentHashMap<>();

        command.getWishCommand(new AtomicBoolean(false), null, wishContainer, "a'");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetWishCommandWithWishContainerNullArgument() {
        command.getWishCommand(new AtomicBoolean(false), out, null, "a");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetWishCommandWithCurrentUsernameNullArgument() {
        Map<String, List<String>> wishContainer = new ConcurrentHashMap<>();

        command.getWishCommand(new AtomicBoolean(false), out, wishContainer, null);
    }

    @Test
    public void testGetWishCommandWithNoUsersThatHaveGifts() {
        Map<String, List<String>> wishContainer = new ConcurrentHashMap<>();
        String assertMessage = "Appropriate message for no users received a gift yet returned";
        String expected = "[ There are no students present in the wish list ]" + System.lineSeparator();

        assertEquals(assertMessage, expected, command.getWishCommand(new AtomicBoolean(true),
                out, wishContainer, "a'"));
    }

    @Test
    public void testGetWishCommandWhileNotLoggedIn() {
        Map<String, List<String>> wishContainer = new ConcurrentHashMap<>();
        String assertMessage = "Appropriate message for user not being logged in returned.";
        String expected = "[ You are not logged in ]" + System.lineSeparator();

        assertEquals(assertMessage, expected, command.getWishCommand(new AtomicBoolean(false),
                out, wishContainer, "a"));
    }

    @Test
    public void testGetWishCommandWithOneGiftForUser() {
        Map<String, List<String>> wishContainer = new ConcurrentHashMap<>();
        String assertMessage = "Appropriate message for method succession returned.";
        String expected = "[ Valentin: [hvurchilo] ]" + System.lineSeparator();

        wishContainer.put("Valentin", List.of("hvurchilo"));

        assertEquals(assertMessage, expected, command.getWishCommand(new AtomicBoolean(true),
                out, wishContainer, "a"));
    }

    @Test
    public void testGetWishCommandWithMoreThanOneGiftForOneUser() {
        Map<String, List<String>> wishContainer = new ConcurrentHashMap<>();
        String assertMessage = "Appropriate message for method succession returned.";
        String expected = "[ Stanislav: [kniga, turshiq, tablet, med, lego] ]" + System.lineSeparator();

        wishContainer.put("Stanislav", List.of("kniga", "turshiq", "tablet", "med", "lego"));

        assertEquals(assertMessage, expected, command.getWishCommand(new AtomicBoolean(true),
                out, wishContainer, "a"));
    }

    @Test
    public void testGetWishCommandWithMoreThanOneStudentInTheWishList() {
        Map<String, List<String>> wishContainer = new ConcurrentHashMap<>();
        String assertMessage = "Appropriate message for method succession returned.";
        String expected = "[ Georgi: [pergel] ]" + System.lineSeparator();

        wishContainer.put("Ahmed", List.of("guma"));
        wishContainer.put("Georgi", List.of("pergel"));

        assertEquals(assertMessage, expected, command.getWishCommand(new AtomicBoolean(true),
                out, wishContainer, "Ahmed"));
    }

    @Test
    public void testGetWishCommandWhenStudentWhoUsesTheCommandAndIsTheOnlyOneInTheWishList() {
        Map<String, List<String>> wishContainer = new ConcurrentHashMap<>();
        String assertMessage = "Message for no students present in the list returned.";
        String expected = "[ There are no students present in the wish list ]" + System.lineSeparator();

        wishContainer.put("Stefcho", List.of("tuhla"));

        assertEquals(assertMessage, expected, command.getWishCommand(new AtomicBoolean(true),
                out, wishContainer, "Stefcho"));
    }
}
