package bg.sofia.uni.fmi.mjt.socialmedia;

import bg.sofia.uni.fmi.mjt.socialmedia.comparator.ContentSortByDate;
import bg.sofia.uni.fmi.mjt.socialmedia.comparator.ContentSortByLikes;
import bg.sofia.uni.fmi.mjt.socialmedia.comparator.SortUsernamesByMentions;
import bg.sofia.uni.fmi.mjt.socialmedia.content.Content;
import bg.sofia.uni.fmi.mjt.socialmedia.content.Post;
import bg.sofia.uni.fmi.mjt.socialmedia.content.Story;
import bg.sofia.uni.fmi.mjt.socialmedia.content.Upload;
import bg.sofia.uni.fmi.mjt.socialmedia.exceptions.ContentNotFoundException;
import bg.sofia.uni.fmi.mjt.socialmedia.exceptions.NoUsersException;
import bg.sofia.uni.fmi.mjt.socialmedia.exceptions.UsernameAlreadyExistsException;
import bg.sofia.uni.fmi.mjt.socialmedia.exceptions.UsernameNotFoundException;
import bg.sofia.uni.fmi.mjt.socialmedia.logs.UserActivityLog;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Collection;
import java.util.TreeMap;

public class EvilSocialInator implements SocialMediaInator {

    private List<String> accounts;
    private List<Upload> uploads;
    private int contentCount;
    private Map<String, UserActivityLog> userLogger;

    public EvilSocialInator() {
        this.accounts = new ArrayList<>();
        this.uploads = new ArrayList<>();
        this.contentCount = 0;
        this.userLogger = new HashMap<>();
    }

    @Override
    public void register(String username) {
        if (username == null) {
            throw new IllegalArgumentException("Username is null!");
        }

        if (!accounts.isEmpty()) {
            if (accounts.contains(username)) {
                throw new UsernameAlreadyExistsException("Username already exists!");
            }
        }

        accounts.add(username);
        userLogger.put(username, new UserActivityLog());
    }

    @Override
    public String publishPost(String username, LocalDateTime publishedOn, String description) {

        if (username == null || publishedOn == null || description == null) {
            throw new IllegalArgumentException("One or more of the parameters are null!");
        }

        if (!accounts.contains(username)) {
            throw new UsernameNotFoundException("The inputted username is not found in the platform!");
        }

        String id = username + "-" + contentCount;
        contentCount++;
        if (publishedOn.plusDays(30).isAfter(LocalDateTime.now())) {
            uploads.add(new Post(username, publishedOn, description, id));
        }

        userLogger.get(username).addPost(publishedOn, id);
        return id;
    }

    @Override
    public String publishStory(String username, LocalDateTime publishedOn, String description) {

        if (username == null || publishedOn == null || description == null) {
            throw new IllegalArgumentException("One or more of the parameters are null!");
        }

        if (!accounts.contains(username)) {
            throw new UsernameNotFoundException("The inputted username is not found in the platform!");
        }

        String id = username + "-" + contentCount;
        contentCount++;
        if (publishedOn.plusHours(24).isAfter(LocalDateTime.now())) {
            uploads.add(new Story(username, publishedOn, description, id));
        }

        userLogger.get(username).addStory(publishedOn, id);
        return id;
    }

    @Override
    public void like(String username, String id) {
        if (username == null || id == null) {
            throw new IllegalArgumentException("One or more of the parameters are null!");
        }

        if (!accounts.contains(username)) {
            throw new UsernameNotFoundException("The inputted username is not found in the platform!");
        }

        boolean isFound = false;
        for (Upload current : uploads) {
            if (current.getId().equals(id)) {
                isFound = true;
                current.setLikes(current.getNumberOfLikes() + 1);
                break;
            }
        }

        if (!isFound) {
            throw new ContentNotFoundException("There isn't a story with the inputted id in the platform!");
        }

        userLogger.get(username).addLike(LocalDateTime.now(), id);
    }

    @Override
    public void comment(String username, String text, String id) {

        if (username == null || text == null || id == null) {
            throw new IllegalArgumentException("One or more of the parameters are null!");
        }

        if (!accounts.contains(username)) {
            throw new UsernameNotFoundException("The inputted username is not found in the platform!");
        }

        boolean isFound = false;
        for (Upload current : uploads) {
            if (current.getId().equals(id)) {
                isFound = true;
                current.setComments(current.getNumberOfComments() + 1);
                break;
            }
        }

        if (!isFound) {
            throw new ContentNotFoundException("There isn't a story with the inputted id in the platform!");
        }

        userLogger.get(username).addComment(LocalDateTime.now(), text, id);
    }

    @Override
    public Collection<Content> getNMostPopularContent(int n) {
        if (n < 0) {
            return null;
        }

        ContentSortByLikes comparator = new ContentSortByLikes();
        Collections.sort(uploads, comparator);
        List<Content> mostPopular = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            mostPopular.add(uploads.get(i));
        }

        return mostPopular;
    }

    @Override
    public Collection<Content> getNMostRecentContent(String username, int n) throws UsernameNotFoundException {
        if (username == null || n < 0) {
            return null;
        }
        if (!accounts.contains(username)) {
            throw new UsernameNotFoundException("Username does not exist in the platform!");
        }
        List<Upload> currentUsernameContent = new ArrayList<>();
        ContentSortByDate comparator = new ContentSortByDate();
        for (Upload current : uploads) {
            if (current.getUsername().equals(username)) {
                currentUsernameContent.add(current);
                break;
            }
        }
        Collections.sort(currentUsernameContent, comparator);
        List<Content> mostRecentContent = new ArrayList<>();
        int counter = 0;
        for (Upload current : currentUsernameContent) {
            if (counter == n) {
                break;
            }
            mostRecentContent.add(current);
            counter++;
        }

        return mostRecentContent;
    }

    @Override
    public String getMostPopularUser() {
        if (accounts.isEmpty()) {
            throw new NoUsersException("There are currently no users in the platform!");
        }
        Map<String, Integer> usernameMentions = new HashMap<>();
        for (Upload currentContent : uploads) {
            for (String currentUsername : currentContent.getMentions()) {
                if (accounts.contains(currentUsername)) {
                    if (!usernameMentions.containsKey(currentUsername)) {
                        usernameMentions.put(currentUsername, 1);
                    } else {
                        usernameMentions.put(currentUsername, usernameMentions.get(currentUsername) + 1);
                    }
                }
            }
        }
        if (usernameMentions.isEmpty()) {
            return accounts.get(0);
        }

        SortUsernamesByMentions comparator = new SortUsernamesByMentions(usernameMentions);
        TreeMap<String, Integer> sortedMentions = new TreeMap<>(comparator);
        sortedMentions.putAll(usernameMentions);
        return sortedMentions.firstKey();
    }

    @Override
    public Collection<Content> findContentByTag(String tag) {
        if (tag == null) {
            throw new IllegalArgumentException("Tag is null!");
        }

        List<Content> contentWithTag = new ArrayList<>();
        for (Upload current : uploads) {
            for (String currentTag : current.getTags()) {
                if (currentTag.equals(tag)) {
                    contentWithTag.add(current);
                    break;
                }
            }
        }

        return contentWithTag;
    }

    @Override
    public List<String> getActivityLog(String username) {
        return userLogger.get(username).getLog();
    }
}
