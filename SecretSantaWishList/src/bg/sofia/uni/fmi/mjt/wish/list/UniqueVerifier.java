package bg.sofia.uni.fmi.mjt.wish.list;

import java.util.List;

public class UniqueVerifier {

    public boolean isUserAlreadyRegistered(List<WishListUser> accounts, String user) {

        if (accounts == null || user == null) {
            throw new IllegalArgumentException("Null argument in isUserAlreadyRegistered method.");
        }

        for (WishListUser currentUser : accounts) {
            if (currentUser.username().equals(user)) {
                return true;
            }

        }

        return false;
    }

}
