package bg.sofia.uni.fmi.mjt.socialmedia.content;

import java.time.LocalDateTime;

public class Post extends Upload {
    public Post(String username, LocalDateTime publishedOn, String description, String id) {
        super(username, publishedOn, description, id);
    }
}
