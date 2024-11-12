package com.company;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

class TrieNodeqwq {
    Map<Character, TrieNodeqwq> childrenqwq = new HashMap<>();
    boolean isEndOfWordqwq = false;
}

class Trieqwq {
    TrieNodeqwq rootqwq;

    public Trieqwq() {
        rootqwq = new TrieNodeqwq();
    }

    // Insert a word into the Trie
    public void insertqwq(String wordqwq) {
        TrieNodeqwq nodeqwq = rootqwq;
        wordqwq = wordqwq.toLowerCase();  // Convert to lowercase
        for (char chqwq : wordqwq.toCharArray()) {
            nodeqwq.childrenqwq.putIfAbsent(chqwq, new TrieNodeqwq());
            nodeqwq = nodeqwq.childrenqwq.get(chqwq);
        }
        nodeqwq.isEndOfWordqwq = true;
    }

    // Search for a word in the Trie
    public boolean searchqwq(String wordqwq) {
        TrieNodeqwq nodeqwq = rootqwq;
        wordqwq = wordqwq.toLowerCase();  // Convert to lowercase
        for (char chqwq : wordqwq.toCharArray()) {
            nodeqwq = nodeqwq.childrenqwq.get(chqwq);
            if (nodeqwq == null) {
                return false;
            }
        }
        return nodeqwq.isEndOfWordqwq;
    }

    // Generate all words stored in the Trie
    public List<String> generateWordsqwq() {
        return generateWordsHelperqwq("", rootqwq);
    }

    private List<String> generateWordsHelperqwq(String prefixqwq, TrieNodeqwq nodeqwq) {
        List<String> wordsqwq = new ArrayList<>();
        if (nodeqwq.isEndOfWordqwq) {
            wordsqwq.add(prefixqwq);
        }
        for (char chqwq : nodeqwq.childrenqwq.keySet()) {
            wordsqwq.addAll(generateWordsHelperqwq(prefixqwq + chqwq, nodeqwq.childrenqwq.get(chqwq)));
        }
        return wordsqwq;
    }
}

class SpellCheckerqwq {
    private final Trieqwq trieqwq;

    public SpellCheckerqwq(List<String> vocabularyqwq) {
        trieqwq = new Trieqwq();
        for (String wordqwq : vocabularyqwq) {
            trieqwq.insertqwq(wordqwq.toLowerCase());  // Convert to lowercase
        }
    }

    // Calculate Levenshtein Distance between two words
    private int editDistanceqwq(String word1qwq, String word2qwq) {
        int mqwq = word1qwq.length();
        int nqwq = word2qwq.length();
        int[][] dpqwq = new int[mqwq + 1][nqwq + 1];

        for (int iqwq = 0; iqwq <= mqwq; iqwq++) dpqwq[iqwq][0] = iqwq;
        for (int jqwq = 0; jqwq <= nqwq; jqwq++) dpqwq[0][jqwq] = jqwq;

        for (int iqwq = 1; iqwq <= mqwq; iqwq++) {
            for (int jqwq = 1; jqwq <= nqwq; jqwq++) {
                if (word1qwq.charAt(iqwq - 1) == word2qwq.charAt(jqwq - 1)) {
                    dpqwq[iqwq][jqwq] = dpqwq[iqwq - 1][jqwq - 1];
                } else {
                    dpqwq[iqwq][jqwq] = 1 + Math.min(dpqwq[iqwq - 1][jqwq - 1], Math.min(dpqwq[iqwq - 1][jqwq], dpqwq[iqwq][jqwq - 1]));
                }
            }
        }
        return dpqwq[mqwq][nqwq];
    }

    // Get alternative suggestions within a maximum edit distance, considering words with one missing or extra character
    public List<String> getSuggestionsqwq(String wordqwq, int maxDistanceqwq) {
        List<String> suggestionsqwq = new ArrayList<>();
        wordqwq = wordqwq.toLowerCase();  // Convert to lowercase
        for (String vocabWordqwq : trieqwq.generateWordsqwq()) {
            int distanceqwq = editDistanceqwq(wordqwq, vocabWordqwq);
            if (distanceqwq <= maxDistanceqwq && Math.abs(vocabWordqwq.length() - wordqwq.length()) <= 1) {
                suggestionsqwq.add(vocabWordqwq);
            }
        }
        return suggestionsqwq;
    }

    // Check if the word is correct or suggest similar alternatives
    public String checkWordqwq(String wordqwq) {
        wordqwq = wordqwq.toLowerCase();  // Convert to lowercase
        if (trieqwq.searchqwq(wordqwq)) {
            return "'" + wordqwq + "' is spelled correctly.";
        } else {
            List<String> suggestionsqwq = getSuggestionsqwq(wordqwq, 2);
            if (!suggestionsqwq.isEmpty()) {
                return "'" + wordqwq + "' is not found. Did you mean: " + String.join(", ", suggestionsqwq) + "?";
            } else {
                return "No suggestions found for '" + wordqwq + "'.";
            }
        }
    }
}

public class Main {
    // Load vocabulary from a file into a list
    private static List<String> loadVocabularyqwq(String filePath) {
        List<String> vocabularyqwq = new ArrayList<>();
        try (BufferedReader readerqwq = new BufferedReader(new FileReader(filePath))) {
            String lineqwq;
            while ((lineqwq = readerqwq.readLine()) != null) {
                vocabularyqwq.add(lineqwq.trim().toLowerCase());  // Convert to lowercase
            }
        } catch (IOException eqwq) {
            System.out.println("Error reading vocabulary file: " + eqwq.getMessage());
        }
        return vocabularyqwq;
    }

    public static void main(String[] argsqwq) {
        // Load vocabulary from a specified file path
        List<String> vocabularyqwq = loadVocabularyqwq("/Users/ashvi/Downloads/words.txt");
        SpellCheckerqwq spellCheckerqwq = new SpellCheckerqwq(vocabularyqwq);

        // Take input from the user for spell-checking
        Scanner scannerqwq = new Scanner(System.in);
        System.out.print("Enter a word to check: ");
        String wordqwq = scannerqwq.nextLine().trim();

        // Display the spell-check result
        System.out.println(spellCheckerqwq.checkWordqwq(wordqwq));
    }
}
