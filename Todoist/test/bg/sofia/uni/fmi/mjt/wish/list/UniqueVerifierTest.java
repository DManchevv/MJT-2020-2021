/*package bg.sofia.uni.fmi.mjt.wish.list;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class UniqueVerifierTest {

    private static UniqueVerifier verifier;

    @Before
    public void setUp() {
        verifier = new UniqueVerifier();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsUserAlreadyRegisteredWithAccountsNullArgument() {
        verifier.isUserAlreadyRegistered(null, "Pesho");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsUserAlreadyRegisteredWithUserNullArgument() {
        List<WishListUser> accounts = new CopyOnWriteArrayList<>();

        verifier.isUserAlreadyRegistered(accounts, null);
    }

    @Test
    public void testIsUserAlreadyRegisteredWithEmptyAccountsArray() {
        List<WishListUser> accounts = new CopyOnWriteArrayList<>();
        String assertMessage = "After getting an empty list as an argument, this method should return true.";

        assertFalse(assertMessage, verifier.isUserAlreadyRegistered(accounts, "Pesho2"));
    }

    @Test
    public void testIsUserAlreadyRegisteredWithNotEmptyArray() {
        List<WishListUser> accounts = new CopyOnWriteArrayList<>();
        String assertMessage = "Return true, because user is already registered.";

        accounts.add(new WishListUser("Zevs", "123"));
        accounts.add(new WishListUser("Mohamed", "456"));

        assertTrue(assertMessage, verifier.isUserAlreadyRegistered(accounts, "Zevs"));
    }

    @Test
    public void testIsUserAlreadyRegisteredReturningFalse() {
        List<WishListUser> accounts = new CopyOnWriteArrayList<>();
        String assertMessage = "Return false, because user is not registered.";

        accounts.add(new WishListUser("Margarita", "345"));

        assertFalse(assertMessage, verifier.isUserAlreadyRegistered(accounts, "Momchil"));
    }

}
*/