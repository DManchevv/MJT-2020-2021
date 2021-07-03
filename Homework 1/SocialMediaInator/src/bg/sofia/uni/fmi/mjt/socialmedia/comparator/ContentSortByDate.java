package bg.sofia.uni.fmi.mjt.socialmedia.comparator;

import bg.sofia.uni.fmi.mjt.socialmedia.content.Upload;

import java.util.Comparator;

public class ContentSortByDate implements Comparator<Upload> {

    @Override
    public int compare(Upload o1, Upload o2) {
        if (o1.getPublishedOn().isAfter(o2.getPublishedOn())) {
            return -1;
        } else {
            return 1;
        } // we do not want the return 0 case, because it will merge keys
    }
}
