package bg.sofia.uni.fmi.mjt.spellchecker;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;

public class ReworkedDictionary {

    public Set<String> readFromDictionary(BufferedReader reader) {
        if (reader == null) {
            throw new IllegalArgumentException("Reader is null!");
        }

        Set<String> dictionary = new HashSet<>();

        try (reader) {
            String line;

            while ((line = reader.readLine()) != null) {
                line = line
                        .replaceAll("[^a-zA-Z0-9]+$", "")
                        .replaceAll("^[^a-zA-Z0-9]+", "");

                if (line.length() > 1) {
                    line = line.toLowerCase();
                    dictionary.add(line);
                }

            }
        } catch (IOException e) {
            throw new IllegalStateException("A problem occured while reading from a file", e);
        }
        return dictionary;
    }

    public Set<String> readFromStopwords(BufferedReader reader) {
        if (reader == null) {
            throw new IllegalArgumentException("Reader is null!");
        }

        Set<String> stopwords = new HashSet<>();
        try (reader) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line
                        .trim()
                        .toLowerCase();
                stopwords.add(line);
            }
        } catch (IOException e) {
            throw new IllegalStateException("A problem occured while reading from a file", e);
        }
        return stopwords;
    }

    public Map<String, Integer> createVectorRepresentationOfWord(String word) {
        if (word == null) {
            throw new IllegalArgumentException("The given word is null!");
        }

        Map<String, Integer> wordSyllables = new HashMap<>();

        for (int i = 0; i < word.length() - 1; i++) {
            String syllable = "" + word.charAt(i) + word.charAt(i + 1);

            if (wordSyllables.containsKey(syllable)) {
                wordSyllables.replace(syllable, wordSyllables.get(syllable) + 1);
            } else {
                wordSyllables.put(syllable, 1);
            }

        }

        return wordSyllables;
    }

    public double cosineSimilarity(Map<String, Integer> firstVector, Map<String, Integer> secondVector) {
        if (firstVector == null || secondVector == null) {
            throw new IllegalArgumentException("One of the given vectors for cosine similarity is null!");
        }

        int dotProduct = 0;

        for (Map.Entry<String, Integer> pair : firstVector.entrySet()) {
            if (secondVector.containsKey(pair.getKey())) {
                dotProduct += pair.getValue() * secondVector.get(pair.getKey());
            }
        }

        double firstVectorLength = getVectorLength(firstVector);
        double secondVectorLength = getVectorLength(secondVector);
        return dotProduct / (firstVectorLength * secondVectorLength);
    }

    public double getVectorLength(Map<String, Integer> wordVector) {
        if (wordVector == null) {
            throw new IllegalArgumentException("The given word vector for vector length getter is null!");
        }

        double vectorLength = 0;

        for (int value : wordVector.values()) {
            vectorLength += value * value;
        }

        vectorLength = Math.sqrt(vectorLength);
        return vectorLength;
    }

}
