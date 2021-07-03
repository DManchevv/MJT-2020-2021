package bg.sofia.uni.fmi.mjt.socialmedia.content;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class Upload implements Content {
    private String username;
    private LocalDateTime publishedOn;
    private String description;
    private String id;
    private int likes;
    private int comments;

    public Upload(String username, LocalDateTime publishedOn, String description, String id) {
        this.username = username;
        this.publishedOn = publishedOn;
        this.description = description;
        this.id = id;
        this.likes = 0;
        this.comments = 0;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public void setComments(int comments) {
        this.comments = comments;
    }

    @Override
    public int getNumberOfLikes() {
        return likes;
    }

    @Override
    public int getNumberOfComments() {
        return comments;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public Collection<String> getTags() {
        List<String> tags = new ArrayList<>();
        for (int i = 0; i < description.length(); i++) {
            if (description.charAt(i) == '#') {
                i++;
                String currentMention = "";
                while (description.charAt(i) != ' ' && i + 1 != description.length()) {
                    currentMention += description.charAt(i);
                    i++;
                }
                if (i + 1 == description.length()) {
                    currentMention += description.charAt(i);
                    tags.add(currentMention);
                    break;
                } else {
                    tags.add(currentMention);
                }
            }
        }
        return tags;
    }

    @Override
    public Collection<String> getMentions() {
        List<String> mentions = new ArrayList<>();
        for (int i = 0; i < description.length(); i++) {
            if (description.charAt(i) == '@') {
                i++;
                String currentMention = "";
                while (description.charAt(i) != ' ' && i + 1 != description.length()) {
                    currentMention += description.charAt(i);
                    i++;
                }
                if (i + 1 == description.length()) {
                    currentMention += description.charAt(i);
                    mentions.add(currentMention);
                    break;
                } else {
                    mentions.add(currentMention);
                }
            }
        }
        return mentions;
    }

    public LocalDateTime getPublishedOn() {
        return publishedOn;
    }

    public String getUsername() {
        return username;
    }
}
