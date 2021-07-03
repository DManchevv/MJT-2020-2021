package bg.sofia.uni.fmi.mjt.netflix.account;

import java.time.LocalDateTime;

public class Account {
    private String username;
    private LocalDateTime birthdayDate;
    private int timeWatch;
    public Account(String username, LocalDateTime birthdayDate){
        this.username = username;
        this.birthdayDate = birthdayDate;
        this.timeWatch = 0;
    }
    public String getUsername(){
        return username;
    }
    public LocalDateTime getBirthdayDate(){
        return birthdayDate;
    }

    public int getTimeWatch() {
        return timeWatch;
    }

    public void setTimeWatch(int timeWatch) {
        this.timeWatch = timeWatch;
    }
}
