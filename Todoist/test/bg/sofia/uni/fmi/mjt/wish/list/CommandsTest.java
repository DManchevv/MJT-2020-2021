package bg.sofia.uni.fmi.mjt.wish.list;

import org.junit.Before;
import org.junit.Test;
import org.mockito.cglib.core.Local;

import static org.junit.Assert.assertEquals;

import java.io.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class CommandsTest {

    private static Commands command;
    private static java.io.File database;
    private static Map<String, Task> personalTasks;
    private static Map<String, Task> inbox;

    @Before
    public void setUp() {
        command = new Commands();
        database = new File("test-database.db");
        database.deleteOnExit();
        personalTasks = new HashMap<>();
        inbox = new HashMap<>();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRegisterCommandWithLineWordsNullArgument() {
        command.register(null, database, new AtomicBoolean(true));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRegisterCommandWithFileNullArgument() {
        command.register(new String[]{"123"}, null, new AtomicBoolean(true));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRegisterCommandWithLoggedInNullArgument() {
        command.register(new String[]{"123"}, database, null);
    }

    @Test
    public void testRegisterCommandWhileLoggedIn() {
        String assertMessage = "Appropriate message for user already logged in is returned.";
        String expected = "[ You cannot register while logged in ]";

        assertEquals(assertMessage, expected, command.register(new String[]{"register", "hristo", "9999"}, database,
                new AtomicBoolean(true)));
    }

    @Test
    public void testRegisterCommandWithUsernameContainingIllegalSymbols() {
        String assertMessage = "Appropriate message for illegal symbols in username is returned.";
        String expected = "[ Username contains invalid symbols ]";

        assertEquals(assertMessage, expected, command.register(new String[]{"register", "petur$#_33", "9999"},
                database, new AtomicBoolean(false)));
    }

    @Test
    public void testRegisterCommandWithUsernameContainingSpecialAllowedSymbols() {
        String assertMessage = "Successfully registered the user and returned appropriate message.";
        String expected = "[ User petur_33 successfully registered ]";

        assertEquals(assertMessage, expected, command.register(new String[]{"register", "petur_33", "8888"},
                database, new AtomicBoolean(false)));
    }

    @Test
    public void testRegisterCommandWithNormalUsername() {
        String assertMessage = "Successfully registered the user and appropriate message returned.";
        String expected = "[ User Stefan33 successfully registered ]";

        assertEquals(assertMessage, expected, command.register(new String[]{"register", "Stefan33", "555"},
                database, new AtomicBoolean(false)));
    }

    @Test
    public void testRegisterCommandWithAlreadyTakenUsername() {
        String assertMessage = "Appropriate message for already taken username is returned.";
        String expected = "[ User with name Stefan33 already exists ]";

        assertEquals(assertMessage, expected, command.register(new String[]{"register", "Stefan33", "123"}, database, new AtomicBoolean(false)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLoginCommandWithUsernameNullArgument() {
        command.login(null, "1234", database, new AtomicBoolean(false));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLoginCommandWithPasswordNullArgument() {
        command.login("mitio", null, database, new AtomicBoolean(false));
    }


    @Test(expected = IllegalArgumentException.class)
    public void testLoginCommandWithFileNullArgument() {
        command.login("mitio", "123", null, new AtomicBoolean(true));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLoginCommandWithLoggedInNullArgument() {
        command.login("mitio", "123", database, null);
    }

    @Test
    public void testLoginCommandWhileLoggedIn() {
        String assertMessage = "Appropriate message for user already logged in is returned.";
        String expected = "[ You are already logged in ]";

        assertEquals(assertMessage, expected, command.login("pesho", "456", database,
                new AtomicBoolean(true)));
    }

    @Test
    public void testLoginCommandWithNonexistentUser() {
        String assertMessage = "Appropriate message for non-existent user is returned.";
        String expected = "[ Username or password is invalid ]";

        assertEquals(assertMessage, expected, command.login("zack", "555",
                database, new AtomicBoolean(false)));
    }

    @Test
    public void testLoginCommandWithInvalidPassword() {
        String assertMessage = "Appropriate message for invalid password is returned.";
        String expected = "[ Username or password is invalid ]";

        assertEquals(assertMessage, expected, command.login("mitio", "456", database,
                new AtomicBoolean(false)));
    }

    @Test
    public void testLoginCommandWithValidCredentials() {
        String assertMessage = "Successfully logged in the user and returned appropriate message.";
        String expected = "[ Hi, Stefan33. You have successfully logged in ]";

        assertEquals(assertMessage, expected, command.login("Stefan33", "555", database,
                new AtomicBoolean(false)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLogoutCommandWithLoggedInNullArgument() {
        command.logout(null);
    }

    @Test
    public void testLogoutCommandWhileNotLoggedIn() {
        String assertMessage = "Appropriate message for user not being logged in returned.";
        String expected = Commands.NOT_LOGGED_IN_ERROR;

        assertEquals(assertMessage, expected, command.logout(new AtomicBoolean(false)));
    }

    @Test
    public void testLogoutCommandWhileLoggedIn() {
        String assertMessage = "Logout successfully and return appropriate message.";
        String expected = "[ You have been logged out from the system ]";

        assertEquals(assertMessage, expected, command.logout(new AtomicBoolean(true)));
    }

    @Test
    public void testAddTaskCommandWithLengthBelowTwo() {
        String assertMessage = "Invalid length message returned.";
        String expected = Commands.WRONG_SYNTAX_ERROR;
        assertEquals(assertMessage, expected, command.addTask(new String[]{"add-task"},
                personalTasks, inbox, new AtomicBoolean(true)));
    }

    @Test
    public void testAddTaskCommandWithLengthAboveFive() {
        String assertMessage = "Invalid length message returned.";
        String expected = Commands.WRONG_SYNTAX_ERROR;
        assertEquals(assertMessage, expected, command.addTask(new String[]{"add-task", "a", "b", "c", "d", "e"},
                personalTasks, inbox, new AtomicBoolean(true)));
    }

    @Test
    public void testAddTaskCommandWithoutNameFlag() {
        String assertMessage = "Name flag needed returned.";
        String expected = "[ Every task requires a name ]";
        assertEquals(assertMessage, expected, command.addTask(new String[]{"add-task", "--date=2021-02-16"},
                personalTasks, inbox, new AtomicBoolean(true)));
    }

    @Test
    public void testAddTaskCommandWhileNotLoggedIn() {
        String assertMessage = "Login error returned.";
        String expected = Commands.NOT_LOGGED_IN_ERROR;
        assertEquals(assertMessage, expected, command.addTask(new String[]{"add-task", "--name=java-izpit"},
                personalTasks, inbox, new AtomicBoolean(false)));
    }

    @Test
    public void testAddTaskCommandWorkingProperlyForPersonalTasks() {
        String assertMessage = "Message for successfully added task returned";
        String expected = "[ The task was successfully added ]";
        assertEquals(assertMessage, expected,
                command.addTask(new String[]{"add-task", "--name=java-izpit", "--date=2021-02-16"},
                        personalTasks, inbox, new AtomicBoolean(true)));
    }

    @Test
    public void testAddTaskCommandWorkingProperlyForInbox() {
        String assertMessage = "Message for successfully added task returned";
        String expected = "[ The task was successfully added ]";
        assertEquals(assertMessage, expected,
                command.addTask(new String[]{"add-task", "--name=java-izpit"},
                        personalTasks, inbox, new AtomicBoolean(true)));
    }

    @Test
    public void testAddTaskCommandWithSameNameAndDate() {
        String assertMessage = "Message for task with same name and date already existing returned";
        String expected = "[ There cannot be 2 tasks with the same name and date ]";
        Task task = new Task("java-izpit", LocalDate.of(2021,2,16),
                null, null);
        personalTasks.put("java-izpit", task);
        assertEquals(assertMessage, expected,
                command.addTask(new String[]{"add-task", "--name=java-izpit", "--date=2021-02-16"},
                        personalTasks, inbox, new AtomicBoolean(true)));
    }

    @Test
    public void testAddTaskCommandWithSameNameInInbox() {
        String assertMessage = "Message for task with same name in inbox returned";
        String expected = "[ There cannot be 2 tasks with the same name in inbox ]";
        Task task = new Task("java-izpit", null, null, null);
        inbox.put("java-izpit", task);
        assertEquals(assertMessage, expected,
                command.addTask(new String[]{"add-task", "--name=java-izpit"},
                        personalTasks, inbox, new AtomicBoolean(true)));
    }

    @Test
    public void testUpdateTaskCommandWithLengthBelowTwo() {
        String assertMessage = "Message for invalid length returned";
        String expected = Commands.WRONG_SYNTAX_ERROR;
        assertEquals(assertMessage, expected, command.updateTask(new String[]{"update-task"},
                personalTasks, inbox, new AtomicBoolean(false)));
    }

    @Test
    public void testUpdateTaskCommandWithLengthAboveFour() {
        String assertMessage = "Message for invalid length returned";
        String expected = Commands.WRONG_SYNTAX_ERROR;
        assertEquals(assertMessage, expected, command.updateTask(new String[]{"update-task", "a", "b", "c", "d"},
                personalTasks, inbox, new AtomicBoolean(false)));
    }

    @Test
    public void testUpdateTaskCommandWithoutBeingLoggedIn() {
        String assertMessage = "Message for not being logged in returned";
        String expected = Commands.NOT_LOGGED_IN_ERROR;
        assertEquals(assertMessage, expected,
                command.updateTask(new String[]{"update-task", "--name=java-izpit", "--description=abz"},
                        personalTasks, inbox, new AtomicBoolean(false)));
    }

    @Test
    public void testUpdateTaskCommandWithoutNameFlag() {
        String assertMessage = "Message for missing name flag returned.";
        String expected = "[ Every task requires a name ]";
        assertEquals(assertMessage, expected,
                command.updateTask(new String[]{"update-task", "--description=abz"},
                        personalTasks, inbox, new AtomicBoolean(true)));
    }

    @Test
    public void testUpdateTaskCommandWorkingProperlyInInbox() {
        String assertMessage = "Message for successfully updated task returned.";
        String expected = "[ Task with name java-izpit was successfully updated ]";
        Task task = new Task("java-izpit", null, null, null);
        inbox.put("java-izpit", task);
        assertEquals(assertMessage, expected,
                command.updateTask(new String[]{"update-task", "--name=java-izpit", "--description=abz"},
                        personalTasks, inbox, new AtomicBoolean(true)));
    }

    @Test
    public void testUpdateTaskCommandWorkingProperlyInPersonalTasks() {
        String assertMessage = "Message for successfully updated task returned.";
        String expected = "[ Task with name java-izpit was successfully updated ]";
        Task task = new Task("java-izpit", LocalDate.of(2021,2,16), null, null);
        personalTasks.put("java-izpit", task);
        assertEquals(assertMessage, expected,
                command.updateTask(new String[]{"update-task", "--name=java-izpit", "--description=abz"},
                        personalTasks, inbox, new AtomicBoolean(true)));
    }

    @Test
    public void testUpdateTaskCommandWithoutUsingAnyOfTheOptionalFlags() {
        String assertMessage = "Message for wrong syntax returned.";
        String expected = Commands.WRONG_SYNTAX_ERROR;
        Task task = new Task("java-izpit", LocalDate.of(2021,2,16), null, null);
        personalTasks.put("java-izpit", task);
        assertEquals(assertMessage, expected,
                command.updateTask(new String[]{"update-task", "--name=java-izpit", "--dfg=gfd", "--xyz=zyx"},
                        personalTasks, inbox, new AtomicBoolean(true)));
    }

    @Test
    public void testUpdateTaskCommandWithNotFoundName() {
        String assertMessage = "Message for no such task existing returned.";
        String expected = "[ There isn't a task with such name ]";
        assertEquals(assertMessage, expected,
                command.updateTask(new String[]{"update-task", "--name=mitio", "--description=ne znam"},
                        personalTasks, inbox, new AtomicBoolean(true)));
    }

    @Test
    public void testCreateTaskCommandWithNullDueDate() {
        String assertMessage = "Correctly creating a task.";
        Task expected = new Task("java", LocalDate.of(2021,2,16), null, null);
        assertEquals(assertMessage, expected.toString(),
                command.createTask("java", "2021-02-16", null, null).toString());
    }

    @Test
    public void testCreateTaskCommandWithNullDate() {
        String assertMessage = "Correctly creating a task.";
        Task expected = new Task("java", null, null, null);
        assertEquals(assertMessage, expected.toString(),
                command.createTask("java", null, null, null).toString());
    }

    @Test
    public void testCreateTaskWithoutNullDates() {
        String assertMessage = "Correctly creating a task.";
        Task expected = new Task("java",
                LocalDate.of(2021,2,16),
                LocalDate.of(2021, 2, 20), null);
        assertEquals(assertMessage, expected.toString(),
                command.createTask("java", "2021-02-16", "2021-02-20", null).toString());
    }

    @Test
    public void testDeleteTaskWithLengthBelowTwo() {
        String assetMessage = "Message for invalid length returned";
        String expected = Commands.WRONG_SYNTAX_ERROR;
        assertEquals(assetMessage, expected, command.deleteTask(new String[]{"delete-task"},
                personalTasks, inbox, new AtomicBoolean(true)));
    }

    @Test
    public void testDeleteTaskWithLengthAboveThree() {
        String assetMessage = "Message for invalid length returned";
        String expected = Commands.WRONG_SYNTAX_ERROR;
        assertEquals(assetMessage, expected, command.deleteTask(new String[]{"delete-task","a","b","c"},
                personalTasks, inbox, new AtomicBoolean(true)));
    }

    @Test
    public void testDeleteTaskWithoutNameFlag() {
        String assetMessage = "Message for missing name flag returned";
        String expected = "[ This command must contain --name flag ]";
        assertEquals(assetMessage, expected, command.deleteTask(new String[]{"delete-task", "--big=slow"},
                personalTasks, inbox, new AtomicBoolean(true)));
    }

    @Test
    public void testDeleteTaskWithoutBeingLoggedIn() {
        String assertMessage = "Message for not being logged in returned";
        String expected = Commands.NOT_LOGGED_IN_ERROR;
        assertEquals(assertMessage, expected,
                command.deleteTask(new String[]{"delete-task", "--name=java-izpit"},
                        personalTasks, inbox, new AtomicBoolean(false)));
    }

    @Test
    public void testDeleteTaskWithCertainNameAndDate() {
        String assertMessage = "Message for successfully removed a task returned";
        String expected = "[ Task with name java-izpit was successfully removed ]";
        Task task = new Task("java-izpit", LocalDate.of(2021,2,16), null, null);
        personalTasks.put("java-izpit", task);
        assertEquals(assertMessage, expected,
                command.deleteTask(new String[]{"delete-task", "--name=java-izpit", "--date=2021-02-16"},
                        personalTasks, inbox, new AtomicBoolean(true)));
    }

    @Test
    public void testDeleteTaskWithCertainNameInPersonalTasks() {
        String assertMessage = "Message for successfully removed a task returned";
        String expected = "[ Task with name java-izpit was successfully removed ]";
        Task task = new Task("java-izpit", LocalDate.of(2021,2,16), null, null);
        personalTasks.put("java-izpit", task);
        assertEquals(assertMessage, expected,
                command.deleteTask(new String[]{"delete-task", "--name=java-izpit"},
                        personalTasks, inbox, new AtomicBoolean(true)));
    }

    @Test
    public void testDeleteTaskWithCertainNameInInbox() {
        String assertMessage = "Message for successfully removed a task returned";
        String expected = "[ Task with name java-izpit was successfully removed ]";
        Task task = new Task("java-izpit", null, null, null);
        inbox.put("java-izpit", task);
        assertEquals(assertMessage, expected,
                command.deleteTask(new String[]{"delete-task", "--name=java-izpit"},
                        personalTasks, inbox, new AtomicBoolean(true)));
    }

    @Test
    public void testDeleteTaskWithNameAndDateNotFound() {
        String assertMessage = "Message for not found name returned.";
        String expected = "[ There isn't task with such name ]";
        Task task = new Task("java-izpit", null, null, null);
        inbox.put("java-izpit", task);
        assertEquals(assertMessage, expected,
                command.deleteTask(new String[]{"delete-task", "--name=java-izpit", "--date=2021-02-16"},
                        personalTasks, inbox, new AtomicBoolean(true)));
    }

    @Test
    public void testDeleteTaskWithNameNotFound() {
        String assertMessage = "Message for not found name returned.";
        String expected = "[ There isn't task with such name ]";
        assertEquals(assertMessage, expected,
                command.deleteTask(new String[]{"delete-task", "--name=java-izpit"},
                        personalTasks, inbox, new AtomicBoolean(true)));
    }

    @Test
    public void testGetTaskWithLengthBelowTwo() {
        String assertMessage = "Message for invalid length returned.";
        String expected = Commands.WRONG_SYNTAX_ERROR;
        assertEquals(assertMessage, expected,
                command.getTask(new String[]{"get-task"},
                        personalTasks,inbox, new AtomicBoolean(true)));
    }

    @Test
    public void testGetTaskWithLengthAboveThree() {
        String assertMessage = "Message for invalid length returned.";
        String expected = Commands.WRONG_SYNTAX_ERROR;
        assertEquals(assertMessage, expected,
                command.getTask(new String[]{"get-task", "a", "b", "c"},
                        personalTasks, inbox, new AtomicBoolean(true)));
    }

    @Test
    public void testGetTaskWithMissingNameFlag() {
        String assertMessage = "Message for missing name flag returned.";
        String expected = "[ This command must contain --name flag ]";
        assertEquals(assertMessage, expected,
                command.getTask(new String[]{"get-task", "--abc=java-izpit"},
                        personalTasks, inbox, new AtomicBoolean(true)));
    }

    @Test
    public void testGetTaskWithoutBeingLoggedIn() {
        String assertMessage = "Message for not being logged in returned.";
        String expected = Commands.NOT_LOGGED_IN_ERROR;
        assertEquals(assertMessage, expected,
                command.getTask(new String[]{"get-task", "--name=java-izpit"},
                        personalTasks, inbox, new AtomicBoolean(false)));
    }

    @Test
    public void testGetTaskFromPersonalTasks() {
        String assertMessage = "Message displaying the task we need.";
        String expected = "Task: --name=java-izpit --date=2021-02-16 --dueDate=null --description=null";
        Task task = new Task("java-izpit", LocalDate.of(2021,2,16), null, null);
        personalTasks.put("java-izpit", task);
        assertEquals(assertMessage, expected,
                command.getTask(new String[]{"get-task", "--name=java-izpit"},
                        personalTasks, inbox, new AtomicBoolean(true)));
    }

    @Test
    public void testGetTaskFromInbox() {
        String assertMessage = "Message displaying the task we need.";
        String expected = "Task: --name=java-izpit --date=null --dueDate=null --description=null";
        Task task = new Task("java-izpit", null, null, null);
        inbox.put("java-izpit", task);
        assertEquals(assertMessage, expected,
                command.getTask(new String[]{"get-task", "--name=java-izpit"},
                        personalTasks, inbox, new AtomicBoolean(true)));
    }

    @Test
    public void testGetTaskWithNotFoundName() {
        String assertMessage = "Message for not found name returned.";
        String expected = "[ There isn't a task with such name ]";
        assertEquals(assertMessage, expected,
                command.getTask(new String[]{"get-task", "--name=java-izpit"},
                        personalTasks, inbox, new AtomicBoolean(true)));
    }


}
