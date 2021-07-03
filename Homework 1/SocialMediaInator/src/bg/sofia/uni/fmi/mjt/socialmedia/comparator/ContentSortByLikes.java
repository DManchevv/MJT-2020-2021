package bg.sofia.uni.fmi.mjt.socialmedia.comparator;

import bg.sofia.uni.fmi.mjt.socialmedia.content.Upload;

import java.util.Comparator;

public class ContentSortByLikes implements Comparator<Upload> {

    @Override
    public int compare(Upload o1, Upload o2) {
        return ((o2.getNumberOfLikes() + o2.getNumberOfComments())
                - (o1.getNumberOfComments() + o1.getNumberOfLikes()));
    }

}
