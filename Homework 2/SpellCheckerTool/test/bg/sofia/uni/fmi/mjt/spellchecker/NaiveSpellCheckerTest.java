package bg.sofia.uni.fmi.mjt.spellchecker;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class NaiveSpellCheckerTest {

    @Test
    public void compilationTest() throws IOException {
        Reader dictionaryReader = new StringReader(String.join(System.lineSeparator(), List.of("cat", "dog", "bird")));
        Reader soptwordsReader = new StringReader(String.join(System.lineSeparator(), List.of("a", "am", "me")));

        // 1. constructor
        SpellChecker spellChecker = new NaiveSpellChecker(dictionaryReader, soptwordsReader);

        // 2. findClosestWords()
        spellChecker.findClosestWords("hello", 2);

        // 3. metadata()
        Reader catTextReader = new StringReader("hello, i am a cat!");
        Metadata metadata = spellChecker.metadata(catTextReader);
        metadata.characters();
        metadata.words();
        metadata.mistakes();

        // 4. analyze()
        Reader dogTextReader = new StringReader("hello, i am a dog!");
        Writer output = new FileWriter("output.txt");
        spellChecker.analyze(dogTextReader, output, 2);

    }

    private static NaiveSpellChecker checker;

    @BeforeClass
    public static void setUp() throws IOException {
        // 1. setting dictionary and stopwords
        Reader dictionaryReader = new StringReader(String.join(System.lineSeparator(), List.of("argh", "aarc", "castle",
                "ote", "diversity", "aargh", "convergement", "convergences", "convergences", "convergency",
                "convergencies", "quashy", "quashing", "quasi", "quasi-absolutely", "quasi-academic",
                "quasi-acceptance", "sumpweed", "sumpweeds", "sunbeamy", "sunbeams", "sundowning", "sundowns",
                "sundra", "sun-drawn", "wedgebill", "wedge-billed", "wedged", "Zwingli", "Zwinglian", "gr-s",
                "grub", "expectedness", "expected", "countertail", "countertally", "hers", "hersall",
                "Hersch", "Herschel", "Bebington", "bebite", "bebization", "beblain", "gargil",
                "gargle", "gargled", "preexaminer", "pre-examiner", "preexamines", "slik", "Slyke",
                "slily", "timoroso", "timorous", "timorously", "DH", "dha", "dhabb", "Dhabi", "Dhahran",
                "neatherd", "neatherdess", "neatherds", "nebular", "nebularization", "nebularize",
                "nebulas", "nebulated", "nebulation", "Aargau", "laboratory's", "kotoite", "gote",
                "mit", "tko", "imit", "kuchen", "kuchean", "kuchens")));

        Reader stopwordsReader = new StringReader(String.join(System.lineSeparator(), List.of("a", "about", "above",
                "after", "again", "against", "all", "am", "an", "and", "any", "are", "aren't", "as", "at", "be",
                "because", "been", "before", "being", "below", "between", "both", "but", "by", "can't", "cannot",
                "could", "couldn't", "did", "didn't", "do", "does", "doesn't", "doing", "don't",
                "down", "during", "each", "few", "for", "from", "further", "had", "hadn't", "has",
                "hasn't", "have", "haven't", "having", "he")));

        // 2. Constructor
        checker = new NaiveSpellChecker(dictionaryReader, stopwordsReader);

    }

    @Test
    public void testFindClosestWordsCalculatedCorrectly() {
        String assertMessage = "Closest words are calculated correctly.";
        assertEquals(assertMessage, List.of("dh", "dha", "dhabi"), checker.findClosestWords("dh", 3));
    }

    @Test
    public void testFindClosestWordsWithWrongOutputExpected() {
        String assertMessage = "Expected closing words output is given wrong.";
        assertNotEquals(assertMessage, List.of("bebite", "bebization"), checker.findClosestWords("beb", 2));
    }

    @Test
    public void testFindClosestWordsWithWrongSortingExpected() {
        String assertMessage = "Expected sorting is given wrong.";
        assertNotEquals(assertMessage, List.of("aargh", "argh"), checker.findClosestWords("argh", 2));
    }

    @Test
    public void testFindClosestWordsAreSortedCorrectly() {
        String assertMessage = "Closest words are sorted correctly.";
        assertEquals(assertMessage, List.of("sundowns", "sundowning"), checker.findClosestWords("sundown", 2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindClosestWordsWithNegativeN() {
        checker.findClosestWords("riptest", -1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindClosestWordsWithNullWord() {
        checker.findClosestWords(null, 3);
    }

    @Test
    public void testMetadataCalculatedCorrectly() {
        Reader textReader = new StringReader(String.join(System.lineSeparator(),
                "he does      his timoroso    $#@!%sunbeams////"));
        String assertMessage = "Metadata is calculated correctly.";
        assertEquals(assertMessage, new Metadata(34, 3, 1), checker.metadata(textReader));
    }

    @Test
    public void testMetadataWithStopwordsOnly() {
        Reader textReader = new StringReader(String.join(System.lineSeparator(), "he having doing does"));
        String assertMessage = "Test metadata with only stopwords given";
        assertEquals(assertMessage, new Metadata(17, 0, 0), checker.metadata(textReader));
    }

    @Test
    public void testMetadataWithNonalphanumericalWord() {
        Reader textReader = new StringReader(String.join(System.lineSeparator(), "@#$% slik"));
        String assertMessage = "Test metadata with nonaplhanumerical word.";
        assertEquals(assertMessage, new Metadata(8, 1, 0), checker.metadata(textReader));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMetaDataWithNullReader() {
        checker.metadata(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAnalyzeWithNullReaderAndWriter() {
        checker.analyze(null, null, 3);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAnalyzeWithNegativeSuggestions() throws IOException {
        Reader textReader = new StringReader(String.join(System.lineSeparator(), "a"));
        Writer textWriter = new FileWriter("output2.txt");
        checker.analyze(textReader, textWriter, -1);
    }

    @Test
    public void testAnalyzeWithBaseCase() throws IOException {
        Reader textReader = new StringReader(String.join(System.lineSeparator(),
                "Zwingli  $#does~! eat    ote../"));
        Writer textWriter = new FileWriter("output3.txt");
        String assertMessage = "Testing analyze with base case.";
        String expected = "Zwingli  $#does~! eat    ote../\n"
                + "= = = Metadata = = =\n"
                + "24 characters, 3 words, 1 spelling issue(s) found\n"
                + "= = = Findings = = =\n"
                + "Line #1, {eat} - Possible suggestions are {neatherd, neatherds}";
        checker.analyze(textReader, textWriter, 2);
        assertEquals(assertMessage, expected, Files.readString(Path.of("output3.txt")));
    }

    @Test
    public void testAnalyzeWithLongText() throws IOException {
        Reader textReader = new StringReader(String.join(System.lineSeparator(), "Mitko argh kote castle\n"
                + "diversity   $$aargh### kuche\n"
                + "!$$$aarc//////////\n"
                + "a    ##$laboratory's'$#"));
        Writer textWriter = new FileWriter("output4.txt");
        String assertMessage = "Testing analyze with long text.";
        String expected = "Mitko argh kote castle\n"
                + "diversity   $$aargh### kuche\n"
                + "!$$$aarc//////////\n"
                + "a    ##$laboratory's'$#\n"
                + "= = = Metadata = = =\n"
                + "80 characters, 9 words, 3 spelling issue(s) found\n"
                + "= = = Findings = = =\n"
                + "Line #1, {kote} - Possible suggestions are {ote, kotoite, gote}\n"
                + "Line #1, {mitko} - Possible suggestions are {mit, tko, imit}\n"
                + "Line #2, {kuche} - Possible suggestions are {kuchen, kuchean, kuchens}";
        checker.analyze(textReader, textWriter, 3);
        assertEquals(assertMessage, expected, Files.readString(Path.of("output4.txt")));
    }

    @Test
    public void testAnalyzeWithNonaplhanumericWordsOnly() throws IOException {
        Reader textReader = new StringReader(String.join(System.lineSeparator(), "!@#$% )((*** /.!/|?.<>,."));
        Writer textWriter = new FileWriter("output5.txt");
        String assertMessage = "Testing analyze with nonaplhanumeric words only.";
        String expected = "!@#$% )((*** /.!/|?.<>,.\n"
                + "= = = Metadata = = =\n"
                + "22 characters, 0 words, 0 spelling issue(s) found\n"
                + "= = = Findings = = =";
        checker.analyze(textReader, textWriter, 5);
        assertEquals(assertMessage, expected, Files.readString(Path.of("output5.txt")));
    }

    @Test
    public void testAnalyzeWithStopwordsOnly() throws IOException {
        Reader textReader = new StringReader(String.join(System.lineSeparator(), "a about against all any\n"
                + "cannot could during had has have"));
        Writer textWriter = new FileWriter("output6.txt");
        String assertMessage = "Testing analyze with stopwords only.";
        String expected = "a about against all any\n"
                + "cannot could during had has have\n"
                + "= = = Metadata = = =\n"
                + "46 characters, 0 words, 0 spelling issue(s) found\n"
                + "= = = Findings = = =";
        checker.analyze(textReader, textWriter, 10);
        assertEquals(assertMessage, expected, Files.readString(Path.of("output6.txt")));
    }
}