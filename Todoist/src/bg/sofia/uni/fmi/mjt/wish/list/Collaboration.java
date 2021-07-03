package bg.sofia.uni.fmi.mjt.wish.list;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Collaboration {
    private final String owner;
    private final String name;
    private List<String> users;
    private Map<String, Task> sharedTasks;
    private Map<String, Task> assignedTasks;

    public Collaboration(String owner, String name) {
        this.owner = owner;
        this.name = name;
        this.users = new ArrayList<>();
        this.sharedTasks = new HashMap<>();
        this.assignedTasks = new HashMap<>();
    }

    public String getOwner() {
        return owner;
    }

    public List<String> getUsers() {
        return users;
    }

    public Map<String, Task> getSharedTasks() {
        return sharedTasks;
    }

    public void addTask(String taskName, Task task) {
        sharedTasks.put(taskName, task);
    }

    public void assignTask(String username, Task task) {
        assignedTasks.put(username, task);
    }

    public void addUser(String username) {
        users.add(username);
    }

    @Override
    public String toString() {
        return "Collaboration: name = " + name + " , owner: " + owner;
    }
}
