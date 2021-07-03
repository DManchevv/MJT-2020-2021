package bg.sofia.uni.fmi.mjt.socialmedia.logs;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class UserActivityLog {
    List<String> log;

    public UserActivityLog() {
        this.log = new ArrayList<>();
    }

    public void addComment(LocalDateTime eventTime, String comment, String id) {
        String currentLog = eventTime.getHour() + ":" + eventTime.getMinute() + ":" + eventTime.getSecond() + " " +
                eventTime.getDayOfMonth() + "." + eventTime.getMonthValue() + "." + eventTime.getYear() +
                ": Commented \"" + comment + " on a content with id " + id;
        this.log.add(currentLog);
    }

    public void addLike(LocalDateTime eventTime, String id) {
        String currentLog = eventTime.getHour() + ":" + eventTime.getMinute() + ":" + eventTime.getSecond() + " " +
                eventTime.getDayOfMonth() + "." + eventTime.getMonthValue() + "." + eventTime.getYear() +
                ": Liked a content with id " + id;
        this.log.add(currentLog);
    }

    public void addPost(LocalDateTime eventTime, String id) {
        String currentLog = eventTime.getHour() + ":" + eventTime.getMinute() + ":" + eventTime.getSecond() + " " +
                eventTime.getDayOfMonth() + "." + eventTime.getMonthValue() + "." + eventTime.getYear() +
                ": Created a post with id " + id;
        this.log.add(currentLog);
    }

    public void addStory(LocalDateTime eventTime, String id) {
        String currentLog = eventTime.getHour() + ":" + eventTime.getMinute() + ":" + eventTime.getSecond() + " " +
                eventTime.getDayOfMonth() + "." + eventTime.getMonthValue() + "." + eventTime.getYear() +
                ": Created a story with id " + id;
        this.log.add(currentLog);
    }

    public List<String> getLog() {
        return log;
    }

}
