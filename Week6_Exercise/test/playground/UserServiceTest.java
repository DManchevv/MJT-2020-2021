package playground;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;


public class UserServiceTest {

    @Test(expected = UserAlreadyExistsException.class)
    public void testRegisterThrowsAppropriateExceptionWhenUserAlreadyExists() {
        UserRepository repository = new PositiveUserRepositoryStubImpl();
        UserService service = new UserService(repository);
        service.register("test@test.com", "weak");
    }
}
