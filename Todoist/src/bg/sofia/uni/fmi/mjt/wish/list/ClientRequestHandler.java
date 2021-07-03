package bg.sofia.uni.fmi.mjt.wish.list;

import java.io.*;
import java.net.Socket;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class ClientRequestHandler implements Runnable {

    private final Socket socket;
    private final AtomicBoolean loggedIn;
    private final Commands commandMenu;
    private final File database;
    private String currentUser;
    private final Map<String, Task> personalTasks;
    private final Map<String, Task> inbox;
    private final Map<String, Collaboration> collaborations;
    public static final String WRONG_SYNTAX_ERROR = "[ Wrong command syntax ]";
    public static final String NOT_LOGGED_IN_ERROR = "[ You must be logged in ]";

    public ClientRequestHandler(Socket socket, Map<String, Collaboration> collaborations) {
        this.socket = socket;
        this.loggedIn = new AtomicBoolean(false);
        this.commandMenu = new Commands();
        this.database = new File("database.db");
        this.personalTasks = new HashMap<>();
        this.inbox = new HashMap<>();
        this.collaborations = collaborations;
    }

    @Override
    public void run() {
        //TODO refactor the whole method in order to have cleaner code
        try (PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            String inputLine;

            outer:
            while (!Thread.currentThread().isInterrupted()) {
                if (in.ready()) {
                    inputLine = in.readLine();

                    String[] lineWords = inputLine.split("\\s+");

                    if ("register".equals(lineWords[0])) {
                        String result = commandMenu.register(lineWords, database, loggedIn);

                        if (result.contains("successfully")) {
                            currentUser = lineWords[1];
                            loggedIn.set(true);
                        }

                        out.println(result);
                    }
                    else if ("add-task".equals(lineWords[0])) {
                        String result = commandMenu.addTask(lineWords, personalTasks, inbox, loggedIn);
                        out.println(result);
                    }
                    else if ("update-task".equals(lineWords[0])) {
                        String result = commandMenu.updateTask(lineWords, personalTasks, inbox, loggedIn);
                        out.println(result);
                    }
                    else if ("delete-task".equals(lineWords[0])) {
                       String result = commandMenu.deleteTask(lineWords, personalTasks, inbox, loggedIn);
                       out.println(result);
                    }
                    else if ("get-task".equals(lineWords[0])) {
                        String result = commandMenu.getTask(lineWords, personalTasks, inbox, loggedIn);
                        out.println(result);
                    }
                    else if ("list-tasks".equals(lineWords[0])) {
                        if (!loggedIn.get()) {
                            out.println(NOT_LOGGED_IN_ERROR);
                            continue;
                        }

                        int completedIndex;
                        int dateIndex;
                        boolean completed = false;
                        boolean sameDateFound = false;
                        boolean completedFound = false;
                        LocalDate date = null;

                        completedIndex = CleanMethods
                                .VariableRefactoring.findIndexByString(lineWords, "--completed");

                        dateIndex = CleanMethods
                                .VariableRefactoring.findIndexByString(lineWords, "--date");

                        if (completedIndex != -1) {
                            if (lineWords[completedIndex].split("=")[1].equals("true")) {
                                completed = true;
                            }
                        }

                        if (dateIndex != -1) {
                            date = CleanMethods
                                    .VariableRefactoring.dateConverter(lineWords[dateIndex].split("=")[1]);

                        }

                        for (Task task : personalTasks.values()) {
                            if (date != null) {
                                if (date.equals(task.getDate())) {
                                    out.println(task);
                                    sameDateFound = true;
                                }
                            } else if (completed) {
                                if (task.getFinished()) {
                                    out.println(task);
                                    completedFound = true;
                                }
                            } else {
                                out.println(task);
                            }
                        }

                        for (Task task : inbox.values()) {
                            if (date != null) {
                                if (date.equals(task.getDate())) {
                                    out.println(task);
                                    sameDateFound = true;
                                }
                            } else if (completed) {
                                if (task.getFinished()) {
                                    out.println(task);
                                    completedFound = true;
                                }
                            } else {
                                out.println(task);
                            }
                        }

                        if (dateIndex != -1 && !sameDateFound) {
                            out.println("[ There are no tasks with this date ]");
                        }

                        if (completed && !completedFound) {
                            out.println("[ There are no completed tasks ]");
                        }

                    }
                    else if ("list-dashboard".equals(lineWords[0])) {
                        if (lineWords.length != 1) {
                            out.println(WRONG_SYNTAX_ERROR);
                            continue;
                        }

                        if (!loggedIn.get()) {
                            out.println(NOT_LOGGED_IN_ERROR);
                            continue;
                        }

                        if (personalTasks.size() == 0) {
                            out.println("[ There are no tasks for today ]");
                            continue;
                        }

                        for (Task task : personalTasks.values()) {
                            if (task.getDate().equals(LocalDate.now())) {
                                out.println(task);
                            }
                        }

                    }
                    else if ("finish-task".equals(lineWords[0])) {
                        if (lineWords.length != 2) {
                            out.println(WRONG_SYNTAX_ERROR);
                            continue;
                        }

                        if (!loggedIn.get()) {
                            out.println(NOT_LOGGED_IN_ERROR);
                            continue;
                        }

                        String name = lineWords[1].split("=")[1];
                        personalTasks.get(name).setFinished(true);
                        out.println("[ Task with name " + name + " was successfully marked as finished ]");
                    }
                    else if ("add-collaboration".equals(lineWords[0])) {
                        if (lineWords.length != 2) {
                            out.println(WRONG_SYNTAX_ERROR);
                            continue;
                        }

                        if (!loggedIn.get()) {
                            out.println(NOT_LOGGED_IN_ERROR);
                            continue;
                        }

                        String name = lineWords[1].split("=")[1];
                        Collaboration newCollaboration = new Collaboration(currentUser, name);

                        collaborations.put(name, newCollaboration);
                        out.println("[ Collaboration with name " + name + " was successfully added ]");

                    }
                    else if ("delete-collaboration".equals(lineWords[0])) {
                        if (lineWords.length != 2) {
                            out.println(WRONG_SYNTAX_ERROR);
                            continue;
                        }

                        if (!loggedIn.get()) {
                            out.println(NOT_LOGGED_IN_ERROR);
                            continue;
                        }

                        String name = lineWords[1].split("=")[1];

                        if (currentUser.equals(collaborations.get(name).getOwner())) {
                            collaborations.remove(name);
                        } else {
                            out.println("[ Only the owner of the collaboration can delete it ]");
                            continue;
                        }

                        out.println("[ Collaboration was successfully deleted ]");
                    }
                    else if ("add-user".equals(lineWords[0])) {
                        if (lineWords.length != 3) {
                            out.println(WRONG_SYNTAX_ERROR);
                            continue;
                        }

                        if (!loggedIn.get()) {
                            out.println(NOT_LOGGED_IN_ERROR);
                            continue;
                        }

                        int collaborationIndex = CleanMethods
                                .VariableRefactoring.findIndexByString(lineWords, "--collaboration");

                        int userIndex = CleanMethods
                                .VariableRefactoring.findIndexByString(lineWords, "--user");

                        if (collaborationIndex != 1 || userIndex != 2) {
                            out.println(WRONG_SYNTAX_ERROR);
                            continue;
                        }

                        String collaborationName = lineWords[1].split("=")[1];
                        String username = lineWords[2].split("=")[1];

                        collaborations.get(collaborationName).addUser(username);
                        out.println("[ User " + username + " was successfully added to collaboration with name " +
                                collaborationName + " ]");

                    }
                    else if ("collaboration-add-task".equals(lineWords[0])) {
                        if (lineWords.length < 4 || lineWords.length > 6) {
                            out.println(WRONG_SYNTAX_ERROR);
                            continue;
                        }

                        // TODO name-flags check and validate the input
                        String collaborationName;
                        String taskName;
                        String date = null;
                        String dueDate = null;
                        String description = null;
                        int dateIndex = CleanMethods.VariableRefactoring.findIndexByString(lineWords, "--date");
                        int dueIndex = CleanMethods.VariableRefactoring.findIndexByString(lineWords, "--dueDate");
                        int descIndex = CleanMethods.VariableRefactoring.findIndexByString(lineWords, "--description");

                        collaborationName = lineWords[1].split("=")[1];
                        taskName = lineWords[2].split("=")[1];

                        if (dateIndex != -1) {
                            date = lineWords[dateIndex].split("=")[1];
                        }

                        if (dueIndex != -1) {
                            dueDate = lineWords[dueIndex].split("=")[1];
                        }

                        if (descIndex != -1) {
                            description = CleanMethods.VariableRefactoring.createDescription(lineWords, descIndex);
                        }

                        Task task = commandMenu.createTask(taskName, date, dueDate, description);

                        collaborations.get(collaborationName).addTask(taskName, task);

                    }
                    else if ("assign-task".equals(lineWords[0])) {
                        if (lineWords.length != 4) {
                            out.println(WRONG_SYNTAX_ERROR);
                            continue;
                        }

                        if (!loggedIn.get()) {
                            out.println(NOT_LOGGED_IN_ERROR);
                            continue;
                        }

                        int collaborationIndex = CleanMethods
                                .VariableRefactoring.findIndexByString(lineWords, "--collaboration");

                        int userIndex = CleanMethods
                                .VariableRefactoring.findIndexByString(lineWords, "--user");

                        int taskIndex = CleanMethods
                                .VariableRefactoring.findIndexByString(lineWords, "--task");

                        if (collaborationIndex != 1 || userIndex != 2 || taskIndex != 3) {
                            out.println("[ Wrong command syntax ]");
                            continue;
                        }

                        String collaborationName = lineWords[1].split("=")[1];
                        String username = lineWords[2].split("=")[1];
                        String taskName = lineWords[3].split("=")[1];

                        Task task = collaborations.get(collaborationName).getSharedTasks().get(taskName);
                        collaborations.get(collaborationName).assignTask(username, task);

                    }
                    else if ("list-users".equals(lineWords[0])) {
                        if (lineWords.length != 2) {
                            out.println(WRONG_SYNTAX_ERROR);
                            continue;
                        }

                        if (!lineWords[1].split("=")[0].equals("--collaboration")) {
                            out.println(WRONG_SYNTAX_ERROR);
                            continue;
                        }

                        if (!loggedIn.get()) {
                            out.println(NOT_LOGGED_IN_ERROR);
                            continue;
                        }

                        String name = lineWords[1].split("=")[1];

                        for (String user : collaborations.get(name).getUsers()) {
                            out.println(user);
                        }

                    }
                    else if ("list-collaborations".equals(lineWords[0])) {
                        if (lineWords.length != 1) {
                            out.println(WRONG_SYNTAX_ERROR);
                            continue;
                        }

                        for (Collaboration current : collaborations.values()) {
                            if (current.getOwner().equals(currentUser)) {
                                out.println(current);
                                continue outer;
                            } else if (current.getUsers().contains(currentUser)) {
                                out.println(current);
                                continue outer;
                            }
                        }


                    }
                    else if ("list-collaboration-tasks".equals(lineWords[0])) {
                        if (lineWords.length != 2) {
                            out.println(WRONG_SYNTAX_ERROR);
                            continue;
                        }

                        //TODO validation

                        String name = lineWords[1].split("=")[1];

                        if (!collaborations.containsKey(name)) {
                            out.println("[ There isn't collaboration with such name ]");
                            continue;
                        }

                        for (Task task : collaborations.get(name).getSharedTasks().values()) {
                            out.println(task);
                        }

                    }
                    else if ("login".equals(lineWords[0])) {
                        String result = commandMenu.login(lineWords[1], lineWords[2], database, loggedIn);

                        if (result.contains("successfully")) {
                            currentUser = lineWords[1];
                            loggedIn.set(true);
                        }

                        out.println(result);

                    }
                    else if ("logout".equals(lineWords[0])) {
                        String result = commandMenu.logout(loggedIn);

                        if (!result.contains("must")) {
                            loggedIn.set(false);
                        }

                        out.println(result);
                    }
                    else if ("help".equals(lineWords[0])) {
                        out.println("[ Welcome to Todoist! You can use the following commands. ]");
                        out.println("[ Flags that have < > are required and flags that have [ ] are optional. ]");
                        out.println("[ add-task --name=<name> --date=[date] --due-date=[due-date] --desc=[desc] ]");
                        out.println("[ update-task --name=[name] --date=[date] --due-date=[due-date] --desc=[desc] ]");
                        out.println("[ delete-task --name=<task_name> ]");
                        out.println("[ delete-task --name=<task_name> --date=[date] ]");
                        out.println("[ get-task --name=<task_name> ]");
                        out.println("[ get-task --name=<task_name> --date=[date] ]");
                        out.println("[ list-tasks ]");
                        out.println("[ list-tasks --completed=[true] ]");
                        out.println("[ list-tasks --date=[date] ]");
                        out.println("[ list-dashboard ]");
                        out.println("[ finish-task --name=<name> ]");
                        out.println("[ add-collaboration --name=<name> ]");
                        out.println("[ delete-collaboration --name=<name> ]");
                        out.println("[ list-collaborations ]");
                        out.println("[ add-user --collaboration=<name> --user=<username> ]");
                        out.println("[ collaboration-add-task --collaboration=<name> --task-name=<name> " +
                                        "--task-date=[date] --task-duedate=[duedate] " +
                                        "--task-description=[description] ]");
                        out.println("[ assign-task --collaboration=<name> --user=<username> --task=<name> ]");
                        out.println("[ list-collaboration-tasks --collaboration=<name> ]");
                        out.println("[ list-users --collaboration=<name> ]");
                    }
                    else if ("disconnect".equals(lineWords[0])) {
                        loggedIn.set(false);
                        out.println("[ Disconnected from server ]");
                        break;
                    }
                    else {
                        out.println("[ Unknown Command ]");
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