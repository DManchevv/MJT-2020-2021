package bg.sofia.uni.fmi.mjt.netflix.platform;

import bg.sofia.uni.fmi.mjt.netflix.account.Account;
import bg.sofia.uni.fmi.mjt.netflix.content.Streamable;
import bg.sofia.uni.fmi.mjt.netflix.content.enums.PgRating;
import bg.sofia.uni.fmi.mjt.netflix.exceptions.ContentNotFoundException;
import bg.sofia.uni.fmi.mjt.netflix.exceptions.ContentUnavailableException;
import bg.sofia.uni.fmi.mjt.netflix.exceptions.UserNotFoundException;

import java.time.LocalDateTime;

public class Netflix implements StreamingService{
    private Account[] accounts;
    private Streamable[] streamableContent;
    private int[] numberOfViews;
    public Netflix(Account[] accounts, Streamable[] streamableContent){
        this.accounts = accounts;
        this.streamableContent = streamableContent;
        numberOfViews = new int [streamableContent.length];
    }

    @Override
    public void watch(Account user, String videoContentName) throws ContentUnavailableException {
        int userIndex = 0;
        int contentIndex = 0;
        for (int i = 0; i < accounts.length; i++) {
            if (accounts[i].getUsername().equals(user.getUsername())) {
                userIndex = i;
                break;
            }
            if (i == accounts.length - 1) {
                throw new UserNotFoundException();
            }
        }
        if (findByName(videoContentName) == null){
            throw new ContentNotFoundException();
        }
        LocalDateTime today = LocalDateTime.now();
        int yearDifference = today.getYear() - user.getBirthdayDate().getYear();
        int monthDifference = today.getMonthValue() - user.getBirthdayDate().getMonthValue();
        int dayDifference = today.getDayOfMonth() - user.getBirthdayDate().getDayOfMonth();
        if (dayDifference < 0) monthDifference--;
        if (monthDifference < 0) yearDifference--;
        for (int i = 0; i < streamableContent.length; i++) {
            if (streamableContent[i].getTitle().equals(videoContentName)) {
                contentIndex = i;
                if ((yearDifference < 14 && streamableContent[i].getRating().equals(PgRating.PG13))
                        || (yearDifference < 18 && streamableContent[i].getRating().equals(PgRating.NC17))) {
                    throw new ContentUnavailableException();
                }
                break;
            }
        }
        accounts[userIndex].setTimeWatch(accounts[userIndex].getTimeWatch() + streamableContent[contentIndex].getDuration());
        numberOfViews[contentIndex]++;
    }

    @Override
    public Streamable findByName(String videoContentName) {
        for (int i = 0; i < streamableContent.length; i++){
            if (streamableContent[i].getTitle().equals(videoContentName)){
                return streamableContent[i];
            }
        }
        return null;
    }

    @Override
    public Streamable mostViewed() {
        int maxIndex = -1;
        int maxViewed = 0;
        for (int i = 0; i < streamableContent.length; i++){
            if (numberOfViews[i] > maxViewed) {
                maxViewed = numberOfViews[i];
                maxIndex = i;
            }
        }
        if (maxIndex == -1) return null;
        else return streamableContent[maxIndex];
    }

    @Override
    public int totalWatchedTimeByUsers() {
        int sum = 0;
        for (int i = 0; i < accounts.length; i++){
            sum += accounts[i].getTimeWatch();
        }
        return sum;
    }
}
