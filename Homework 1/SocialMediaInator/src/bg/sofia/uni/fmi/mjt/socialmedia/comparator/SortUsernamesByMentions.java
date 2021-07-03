package bg.sofia.uni.fmi.mjt.socialmedia.comparator;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class SortUsernamesByMentions implements Comparator<String> {

    Map<String, Integer> map = new HashMap<>();

    public SortUsernamesByMentions(Map<String, Integer> map) {
        this.map = map;
    }

    @Override
    public int compare(String o1, String o2) {
        if (map.get(o2) >= map.get(o1)) {
            return 1;
        } else {
            return -1;
        }
    }
}
