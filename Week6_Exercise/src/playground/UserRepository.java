package playground;

public interface UserRepository {
    boolean exists(String mail);

    void save(User user);

}
