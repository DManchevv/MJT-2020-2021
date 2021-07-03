package bg.sofia.uni.fmi.mjt.socialmedia;

import bg.sofia.uni.fmi.mjt.socialmedia.content.*;
import bg.sofia.uni.fmi.mjt.socialmedia.logs.*;
import bg.sofia.uni.fmi.mjt.socialmedia.comparator.*;
import bg.sofia.uni.fmi.mjt.socialmedia.exceptions.*;

import java.sql.SQLOutput;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws NoUsersException, UsernameAlreadyExistsException, UsernameNotFoundException, ContentNotFoundException {
        EvilSocialInator registry = new EvilSocialInator();
        try {
            System.out.println(registry.getMostPopularUser());
        } catch (Exception e) {

        }
        registry.register("Mitko");
        registry.register("Ani");
        registry.register("Momcho");
        registry.register("Spasov");
        registry.register("Londey");
        registry.register("Emberfire");
        System.out.println("Test1: " + registry.getMostPopularUser());
        registry.publishPost("Mitko", LocalDateTime.of(2020, Month.NOVEMBER, 20, 15, 32, 43), "#evil #love #win  @Ani @Ani @Spasov @Pesho @Ivan ");
        System.out.println("Test2: " + registry.getMostPopularUser());
        System.out.println("Test3: " + registry.getNMostRecentContent("Mitko", 2).iterator().next().getId());
        registry.publishPost("Ani", LocalDateTime.of(2020, Month.NOVEMBER, 20, 15, 32, 43), "#evil #love #win  @Ani @Momcho @Duner");
        System.out.println("Test4: " + registry.getNMostRecentContent("Ani", 2).iterator().next().getId());
        registry.comment("Mitko", "Mnogo qko stori momichence", "Ani-1");
        System.out.println("Test5: " + registry.getNMostPopularContent(1).iterator().next().getNumberOfComments());
        registry.comment("Spasov", "Adski qkiq post Mitko", "Mitko-0");
        registry.publishStory("Ani", LocalDateTime.of(2020, Month.DECEMBER, 8, 13, 55, 50), "#breakfast #borito #tasty @Mitko @Londey @Emberfire @Kalata @Kuche");
        registry.publishPost("Londey", LocalDateTime.of(2020, Month.DECEMBER, 1, 16, 30, 00), "#fun #Sliven #kef @Emberfire @Spasov @Londey @Ani");
        registry.publishStory("Spasov", LocalDateTime.of(2019, Month.DECEMBER, 8, 13, 55, 50), "#mitko #zdravei #kaksi @Mitko");
        registry.publishStory("Mitko", LocalDateTime.of(2020, Month.DECEMBER, 8, 15, 55, 00), "#programming #homework #java #mjt #win @Ani @Londey @Momcho");
        registry.like("Londey", "Ani-2");
        registry.like("Emberfire", "Ani-2");
        registry.like("Mitko", "Ani-2");
        registry.like("Emberfire", "Londey-3");
        registry.like("Spasov", "Londey-3");
        registry.like("Momcho", "Ani-1");
        registry.like("Spasov", "Mitko-0");
        registry.like("Ani", "Mitko-0");
        registry.like("Londey", "Mitko-0");
        registry.comment("Ani", "Mnogo mi haresva", "Londey-3");
        List<Content> popular = new ArrayList<>();
        System.out.println(registry.getNMostRecentContent("Momcho", 3));
        System.out.println(registry.getActivityLog("Ani"));
        System.out.println(registry.getMostPopularUser());
        for (Content current : registry.getNMostPopularContent(2)) {
            System.out.println(current.getTags());
        }
    }
}
