package bg.sofia.uni.fmi.mjt.wish.list;

import java.io.*;
import java.time.LocalDate;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class Commands {

    public static final String WRONG_SYNTAX_ERROR = "[ Wrong command syntax ]";
    public static final String NOT_LOGGED_IN_ERROR = "[ You must be logged in ]";

    public String register(String[] lineWords, File file, AtomicBoolean loggedIn) {
        if (lineWords == null || file == null || loggedIn == null) {
            throw new IllegalArgumentException("Null argument(s) in register command!");
        }

        if (loggedIn.get()) {
            return ("[ You cannot register while logged in ]");
        }

        if (lineWords.length != 3) {
            return (WRONG_SYNTAX_ERROR);
        }

        String username = lineWords[1];
        String password = lineWords[2];

        if (!username.matches("[a-zA-Z_0-9]+")) {
            return ("[ Username contains invalid symbols ]");
        }

        if (CleanMethods.FileOperations.findUsername(username, file)) {
            return ("[ User with name " + username + " already exists ]");
        }

        CleanMethods.FileOperations.saveNewAccount(username, password, file);
        return ("[ User " + username + " successfully registered ]");
    }

    public String login(String username, String password, File file, AtomicBoolean loggedIn) {
        if (username == null || password == null || file == null || loggedIn == null) {
            throw new IllegalArgumentException("Null argument(s) in login.");
        }

        if (loggedIn.get()) {
            return ("[ You are already logged in ]");
        }

        if (CleanMethods.FileOperations.findAccount(username, password, file)) {
            return ("[ Hi, " + username + ". You have successfully logged in ]");
        }

        return ("[ Username or password is invalid ]");
    }

    public String logout(AtomicBoolean loggedIn) {
        if (loggedIn == null) {
            throw new IllegalArgumentException("LoggedIn variable in logout method is null!");
        }

        if (!loggedIn.get()) {
            return (NOT_LOGGED_IN_ERROR);
        }

        return ("[ You have been logged out from the system ]");
    }

    public Task createTask(String name, String date, String dueDate, String description) {

        LocalDate convertedDate = null;
        LocalDate convertedDueDate = null;

        if (date != null) {
            convertedDate = CleanMethods.VariableRefactoring.dateConverter(date);
        }

        if (dueDate != null) {
            convertedDueDate = CleanMethods.VariableRefactoring.dateConverter(dueDate);
        }

        return (new Task(name, convertedDate, convertedDueDate, description));
    }

    public String addTask(String[] lineWords, Map<String, Task> personalTasks,
                          Map<String, Task> inbox, AtomicBoolean loggedIn) {
        if (lineWords.length < 2 || lineWords.length > 5) {
            return (WRONG_SYNTAX_ERROR);
        }

        if (!lineWords[1].contains("--name")) {
            return ("[ Every task requires a name ]");
        }

        if (!loggedIn.get()) {
            return (NOT_LOGGED_IN_ERROR);
        }

        String name;
        String date = null;
        String dueDate = null;
        String description = null;
        int dateIndex = CleanMethods.VariableRefactoring.findIndexByString(lineWords, "--date");
        int dueIndex = CleanMethods.VariableRefactoring.findIndexByString(lineWords, "--dueDate");
        int descIndex = CleanMethods.VariableRefactoring.findIndexByString(lineWords, "--description");

        name = lineWords[1].split("=")[1];

        if (dateIndex != -1) {
            date = lineWords[dateIndex].split("=")[1];
        }

        if (dueIndex != -1) {
            dueDate = lineWords[dueIndex].split("=")[1];
        }

        if (descIndex != -1) {
            description = CleanMethods.VariableRefactoring.createDescription(lineWords, descIndex);
        }

        Task task = createTask(name, date, dueDate, description);

        if (task.getDate() != null) {
            for (Task currentTask : personalTasks.values()) {
                if (currentTask.getDate().equals(task.getDate()) &&
                        currentTask.getName().equals(task.getName())) {

                    return ("[ There cannot be 2 tasks with the same name and date ]");
                }
            }

            personalTasks.put(name, task);
        } else {

            if (inbox.containsKey(name)) {
                return ("[ There cannot be 2 tasks with the same name in inbox ]");
            }

            inbox.put(name, task);
        }

        return ("[ The task was successfully added ]");
    }

    public String updateTask(String[] lineWords, Map<String, Task> personalTasks,
                             Map<String, Task> inbox, AtomicBoolean loggedIn) {
        if (lineWords.length < 2 || lineWords.length > 4) {
            return (WRONG_SYNTAX_ERROR);
        }

        if (!loggedIn.get()) {
            return (NOT_LOGGED_IN_ERROR);
        }

        if (!lineWords[1].contains("--name")) {
            return ("[ Every task requires a name ]");
        }

        String name = lineWords[1].split("=")[1];
        int dateIndex = CleanMethods.VariableRefactoring.findIndexByString(lineWords, "--date");
        int dueIndex = CleanMethods.VariableRefactoring.findIndexByString(lineWords, "--dueDate");
        int descIndex = CleanMethods
                .VariableRefactoring.findIndexByString(lineWords, "--description");

        if (personalTasks.containsKey(name)) {
            if (dateIndex != -1) {
                personalTasks.get(name).setDate(CleanMethods
                        .VariableRefactoring.dateConverter(lineWords[dateIndex].split("=")[1]));
            }

            if (dueIndex != -1) {
                personalTasks.get(name).setDate(CleanMethods
                        .VariableRefactoring.dateConverter(lineWords[dueIndex].split("=")[1]));
            }

            if (descIndex != -1) {
                personalTasks.get(name).setDescription(CleanMethods
                        .VariableRefactoring.createDescription(lineWords, descIndex));
            }

            if (dateIndex != -1 || dueIndex != -1 || descIndex != -1) {
                return ("[ Task with name " + name + " was successfully updated ]");
            }

        } else if (inbox.containsKey(name)) {
            inbox.get(name).setDescription(CleanMethods
                    .VariableRefactoring.createDescription(lineWords, descIndex));

            return ("[ Task with name " + name + " was successfully updated ]");
        } else {
            return ("[ There isn't a task with such name ]");
        }

        return (WRONG_SYNTAX_ERROR);
    }

    public String deleteTask(String[] lineWords, Map<String, Task> personalTasks,
                             Map<String, Task> inbox, AtomicBoolean loggedIn) {
        if (lineWords.length < 2 || lineWords.length > 3) {
            return (WRONG_SYNTAX_ERROR);
        }

        if (!lineWords[1].contains("--name")) {
            return ("[ This command must contain --name flag ]");
        }

        if (!loggedIn.get()) {
            return (NOT_LOGGED_IN_ERROR);
        }

        String name = lineWords[1].split("=")[1];
        int dateIndex = CleanMethods.VariableRefactoring.findIndexByString(lineWords, "--date");

        if (personalTasks.containsKey(name)) {
            if (dateIndex != -1) {
                String date = lineWords[dateIndex].split("=")[1];
                LocalDate convertedDate = CleanMethods.VariableRefactoring.dateConverter(date);
                if (personalTasks.get(name).getDate().equals(convertedDate)) {
                    personalTasks.remove(name);
                    return ("[ Task with name " + name + " was successfully removed ]");
                }
            } else {
                personalTasks.remove(name);
                return ("[ Task with name " + name + " was successfully removed ]");
            }
        }

        if (inbox.containsKey(name)) {
            if (dateIndex == -1) {
                inbox.remove(name);
                return ("[ Task with name " + name + " was successfully removed ]");
            }
        }

        return ("[ There isn't task with such name ]");
    }

    public String getTask(String[] lineWords, Map<String, Task> personalTasks,
                          Map<String, Task> inbox, AtomicBoolean loggedIn) {
        if (lineWords.length < 2 || lineWords.length > 3) {
            return (WRONG_SYNTAX_ERROR);
        }

        if (!lineWords[1].contains("--name")) {
            return ("[ This command must contain --name flag ]");
        }

        if (!loggedIn.get()) {
            return (NOT_LOGGED_IN_ERROR);
        }

        String name = lineWords[1].split("=")[1];
        int dateIndex = CleanMethods.VariableRefactoring.findIndexByString(lineWords, "--date");

        if (personalTasks.containsKey(name)) {
            if (dateIndex != -1) {
                String date = lineWords[dateIndex].split("=")[1];
                LocalDate convertedDate = CleanMethods.VariableRefactoring.dateConverter(date);

                if (personalTasks.get(name).getDate().equals(convertedDate)) {
                    return (personalTasks.get(name).toString());
                }

            }
            return (personalTasks.get(name).toString());
        }

        if(inbox.containsKey(name)) {
            return inbox.get(name).toString();
        }

        return "[ There isn't a task with such name ]";
    }

}
