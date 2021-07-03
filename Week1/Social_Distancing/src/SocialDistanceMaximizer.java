public class SocialDistanceMaximizer {
    public static int maxDistance(int[] seats){
        int max_distance = -1;
        int cur_distance = 0;
        int special_distance = 0;
        boolean isStartingWithFree = false;
        for (int i = 0 ; i < seats.length; i++){
            if (seats[i] == 0){
                if (i == 0) isStartingWithFree = true;
                cur_distance++;
            }
            else {
                if (isStartingWithFree) {
                    special_distance = cur_distance;
                    isStartingWithFree = false;
                }
                else if (cur_distance > max_distance){
                    max_distance = cur_distance;
                }
                cur_distance = 0;
            }
        }
        if (special_distance != 0){
            if (special_distance > cur_distance && special_distance > max_distance * 2){
                return special_distance;
            }
        }
        if (cur_distance > max_distance * 2) {
            return cur_distance;
        }
        if (max_distance%2 == 0) max_distance = max_distance / 2;
        else max_distance = max_distance / 2 + 1;
        return max_distance;
    }
    public static void main(String[] args) {
        System.out.println(SocialDistanceMaximizer.maxDistance(new int[]{0, 0, 0, 0, 1, 0, 1}));
        System.out.println(SocialDistanceMaximizer.maxDistance(new int[]{0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0 ,0 ,0 ,0 ,1 ,0 ,0 ,1 ,0 ,0 ,0, 0}));
        System.out.println(SocialDistanceMaximizer.maxDistance(new int[]{1,0,0,0}));
        System.out.println(SocialDistanceMaximizer.maxDistance(new int[]{0, 1}));
        System.out.println(SocialDistanceMaximizer.maxDistance(new int[]{1, 0, 0, 0, 0, 1}));
    }
}
