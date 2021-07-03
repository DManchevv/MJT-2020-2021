package playground;

public class UserService {

    private UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public User register(String email, String password) {
        if (repository.exists(email)) {
            throw new UserAlreadyExistsException("User already exists");
        }

        User user = new User(email, password);
        repository.save(user);
        return user;
    }

}
