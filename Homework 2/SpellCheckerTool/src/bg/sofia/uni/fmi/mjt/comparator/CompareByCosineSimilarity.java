package bg.sofia.uni.fmi.mjt.comparator;

import bg.sofia.uni.fmi.mjt.spellchecker.ReworkedDictionary;

import java.util.Comparator;

public class CompareByCosineSimilarity implements Comparator<String> {
    String commonWord;
    ReworkedDictionary dictionary = new ReworkedDictionary();

    public CompareByCosineSimilarity(String commonWord) {
        this.commonWord = commonWord;
    }

    @Override
    public int compare(String o1, String o2) {
        double firstCosine = dictionary.cosineSimilarity(dictionary.createVectorRepresentationOfWord(o1),
                dictionary.createVectorRepresentationOfWord(commonWord));

        double secondCosine = dictionary.cosineSimilarity(dictionary.createVectorRepresentationOfWord(o2),
                dictionary.createVectorRepresentationOfWord(commonWord));

        return Double.compare(secondCosine, firstCosine);
    }
}
