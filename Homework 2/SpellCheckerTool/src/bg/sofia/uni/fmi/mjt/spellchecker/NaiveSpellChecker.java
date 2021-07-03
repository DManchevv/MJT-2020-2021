package bg.sofia.uni.fmi.mjt.spellchecker;

import bg.sofia.uni.fmi.mjt.comparator.CompareByCosineSimilarity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Reader;
import java.io.Writer;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.HashMap;
import java.util.List;
import java.util.LinkedList;
import java.util.Collections;
import java.util.ArrayList;


public class NaiveSpellChecker implements SpellChecker {
    /**
     * Creates a new instance of NaiveSpellCheckTool, based on a dictionary of words and stop words
     *
     * @param dictionary a java.io.Reader input stream containing list of words which will serve as a dictionary for
     * the tool
     * @param stopwords a java.io.Reader input stream containing list of stopwords
     */


    private final Set<String> stopWords;
    private final Set<String> dictionaryWords;
    private final Map<String, Integer> mistakenWords;
    private String text;

    public NaiveSpellChecker(Reader dictionaryReader, Reader stopwordsReader) {
        ReworkedDictionary dictionary = new ReworkedDictionary();
        this.text = "";
        this.mistakenWords = new TreeMap<>();
        this.stopWords = dictionary.readFromStopwords(new BufferedReader(stopwordsReader));
        this.dictionaryWords = dictionary.readFromDictionary(new BufferedReader(dictionaryReader));
    }

    @Override
    public void analyze(Reader textReader, Writer output, int suggestionsCount) {
        if (textReader == null || output == null || suggestionsCount < 1) {
            throw new IllegalArgumentException("Illegal arguments in analyze method");
        }

        BufferedWriter writer = new BufferedWriter(output);
        Metadata textMetadata = metadata(textReader);

        List<Map.Entry<String, Integer>> convertMapToList =
                new LinkedList<>(mistakenWords.entrySet());

        Collections.sort(convertMapToList, Map.Entry.comparingByValue());

        HashMap<String, Integer> sortedMistakenWords = new LinkedHashMap<>();
        for (Map.Entry<String, Integer> pair : convertMapToList) {
            sortedMistakenWords.put(pair.getKey(), pair.getValue());
        }

        try {
            writer.write(text);
            writer.write("= = = Metadata = = =");
            writer.newLine();
            writer.write(textMetadata.characters() + " characters, "
                    + textMetadata.words() + " words, "
                    + textMetadata.mistakes() + " spelling issue(s) found");
            writer.newLine();
            writer.write("= = = Findings = = =");
            for (Map.Entry<String, Integer> pair : sortedMistakenWords.entrySet()) {
                writer.newLine();
                String suggestions = findClosestWords(pair.getKey(), suggestionsCount).toString();
                suggestions = suggestions.substring(1, suggestions.length() - 1);
                writer.write("Line #" + pair.getValue() + ", {" + pair.getKey()
                        + "} - Possible suggestions are {" + suggestions + "}");
            }
            writer.flush();
        } catch (IOException e) {
            throw new IllegalStateException("Failed to write in file!");
        }
    }

    @Override
    public Metadata metadata(Reader textReader) {
        if (textReader == null) {
            throw new IllegalArgumentException("Given text reader is null!");
        }

        text = "";
        mistakenWords.clear();
        BufferedReader reader = new BufferedReader(textReader);
        List<String> textLines = new LinkedList<>();
        int charactersCounter = 0;
        int lineCounter = 0;
        int wordsCounter = 0;
        int mistakesCounter = 0;

        try (reader) {
            String line;

            while ((line = reader.readLine()) != null) {
                lineCounter++;
                textLines.add(line);
                String[] lineWords = line.split("\\s+");

                for (String word : lineWords) {
                    String strippedWord = word
                            .replaceAll("[^a-zA-Z0-9]+$", "")
                            .replaceAll("^[^a-zA-Z0-9]+", "")
                            .toLowerCase();

                    if (!stopWords.contains(strippedWord) && !strippedWord.isEmpty()) {
                        if (!dictionaryWords.contains(strippedWord)) {
                            mistakesCounter++;
                            mistakenWords.put(strippedWord, lineCounter);
                        }
                        wordsCounter++;
                    }

                    word = word.replaceAll("\\s+", "");
                    charactersCounter += word.length();
                }

            }

        } catch (IOException e) {
            throw new IllegalStateException("A problem occured when reading from text file.");
        }

        for (String currentLine : textLines) {
            text += currentLine;
            text += System.lineSeparator();
        }

        return new Metadata(charactersCounter, wordsCounter, mistakesCounter);
    }

    @Override
    public List<String> findClosestWords(String word, int n) {
        if (word == null || n < 1) {
            throw new IllegalArgumentException("Illegal arguments in findClosestWords method");
        }

        List<String> closestWords = new LinkedList<>();
        List<String> words = new ArrayList<String>(dictionaryWords);
        CompareByCosineSimilarity comparator = new CompareByCosineSimilarity(word.toLowerCase());
        int counter = 0;

        Collections.sort(words, comparator);

        for (String currentWord : words) {
            if (counter == n) {
                break;
            }

            closestWords.add(currentWord);
            counter++;
        }

        return closestWords;
    }

}
