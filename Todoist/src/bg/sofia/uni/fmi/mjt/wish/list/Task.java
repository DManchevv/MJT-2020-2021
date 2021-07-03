package bg.sofia.uni.fmi.mjt.wish.list;

import java.time.LocalDate;

public class Task {

    private String name;
    private LocalDate date;
    private LocalDate dueDate;
    private String description;
    private Boolean isFinished;

    public Task (String name, LocalDate date, LocalDate dueDate, String description) {
        this.name = name;
        this.date = date;
        this.dueDate = dueDate;
        this.description = description;
        this.isFinished = false;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setFinished(Boolean finished) {
        isFinished = finished;
    }

    public String getName() {
        return name;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public String getDescription() {
        return description;
    }

    public Boolean getFinished() {
        return isFinished;
    }

    @Override
    public String toString() {
        return "Task: --name=" + name + " --date=" + date + " --dueDate=" + dueDate + " --description=" + description;
    }
}