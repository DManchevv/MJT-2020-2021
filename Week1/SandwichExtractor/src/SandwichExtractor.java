import org.w3c.dom.ls.LSOutput;

import java.util.Arrays;

public class SandwichExtractor {
    public static String[] extractIngredients(String sandwich){
        String[] empty = {};
        String[] checkBread = sandwich.split("bread");
        if (checkBread.length != 3){
            return empty;
        }
        String[] ingredients = sandwich.split("-");
        int counter = 0;
        boolean firstBreadFound = false;
        for (String a : ingredients){
            if (!a.equals("olives")) {
                String[] bread = a.split("bread");
                if (bread.length == 1) counter++;
                else if (bread.length == 2 && !firstBreadFound){
                    firstBreadFound = true;
                    if (!bread[1].equals("olives")) counter++;
                }
                else if (bread.length == 2 && firstBreadFound){
                    if (!bread[0].equals("olives")) counter++;
                }
            }
        }
        String[] result = new String[counter];
        firstBreadFound = false;
        int index = 0;
        for (String a : ingredients){
            if (!a.equals("olives")) {
                String[] bread = a.split("bread");
                if (bread.length == 2 && !firstBreadFound) {
                    firstBreadFound = true;
                    if (!bread[1].equals("olives")){
                        result[index] = bread[1];
                        index++;
                    }
                } else if (bread.length == 2 && !bread[0].equals("olives")) {
                    result[index] = bread[0];
                    index++;
                } else if (bread.length == 1) {
                    result[index] = bread[0];
                    index++;
                }
            }
        }
        Arrays.sort(result);
        return result;
    }

    public static void main(String[] args) {
        System.out.println(Arrays.toString(SandwichExtractor.extractIngredients("asdbreadham-tomato-mayobreadblabla")));
       // Some Tests
       // System.out.println(Arrays.toString(SandwichExtractor.extractIngredients("asdbreadham-olivesbreadblabla")));
       // System.out.println(Arrays.toString(SandwichExtractor.extractIngredients("asdbreadham-olives-tomato-olives-mayobreadblabla")));
       // System.out.println(Arrays.toString(SandwichExtractor.extractIngredients("asdbreadham")));
       // System.out.println(Arrays.toString(SandwichExtractor.extractIngredients("olivesbreadolives-olives-tomato-olives-olivesbreadblabla")));
    }
}
