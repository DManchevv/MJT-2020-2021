package bg.sofia.uni.fmi.mjt.wish.list;

import java.io.*;
import java.time.LocalDate;

public class CleanMethods {
    public static class VariableRefactoring {

        public static int findIndexByString(String[] arr, String word) {

            for (int i = 0; i < arr.length; i++) {
                if(arr[i].contains(word)) {
                    return i;
                }
            }

            return -1;
        }

        public static String createDescription(String[] arr, int descIndex) {
            String description;
            description = arr[descIndex].split("=")[1];
            for (int i = descIndex + 1; i < arr.length; i++) {
                description += " ";
                description += arr[i];
            }

            return description;
        }

        public static LocalDate dateConverter(String date) {
            int year = Integer.parseInt(date.split("-")[0]);
            int month = Integer.parseInt(date.split("-")[1]);
            int day = Integer.parseInt(date.split("-")[2]);
            return LocalDate.of(year, month, day);
        }

    }

    public static class FileOperations {

        public static boolean findAccount(String username, String password, File file) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))){
                String line;

                while ((line = reader.readLine()) != null) {
                    String[] lineWords = line.split("\\s+");

                    if (lineWords[0].equals(username) && lineWords[2].equals(password)) {
                        return true;
                    }

                }

            } catch (IOException e) {
                System.out.println("Error reading from file.");
            }

            return false;
        }

        public static void saveNewAccount(String username, String password, File file) {
            try (PrintWriter writer = new PrintWriter(new FileWriter(file, true))) {
                writer.write(username + " : " + password);
                writer.println();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public static boolean findUsername(String username, File file) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))){
                String line;

                while ((line = reader.readLine()) != null) {
                    String[] lineWords = line.split("\\s+");

                    if (lineWords[0].equals(username)) {
                        return true;
                    }

                }

            } catch (IOException e) {
                System.out.println("Error reading from file.");
            }

            return false;
        }

    }

}
