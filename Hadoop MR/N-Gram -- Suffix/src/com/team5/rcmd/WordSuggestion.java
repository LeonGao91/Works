package com.team5.rcmd;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Scanner;

/**
 * @fileName WordSuggestion.java
 *
 * @author team5
 *
 * @description This class suggests the next word based on
 *              the previous words users type in. It reads
 *              from standard input. This class uses n-gram
 *              to infer the next word.
 */
public class WordSuggestion
{
    /**
     * Underlying data structure to hold dictionary
     */
    private ArrayList<WordFrequency> dicWords = new ArrayList<>();

    /**
     * delimiter to parse input
     */
    public static final String INPUT_DELIMITER = "[^a-zA-Z']+";
    
    public static final String FILE_DELIMITER = "[\\s]+";

    public WordSuggestion(File dic) throws FileNotFoundException
    {

        Scanner in = new Scanner(dic);
        while (in.hasNextLine())
        {
            String[] splittedLine = in.nextLine().split(FILE_DELIMITER);
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < splittedLine.length - 1; i++)
            {
                sb.append(splittedLine[i] + " ");
            }
            dicWords.add(new WordFrequency(sb.toString().trim(), Integer
                    .parseInt(splittedLine[splittedLine.length - 1])));
        }
        in.close();
    }

    /**
     * This method suggests {@code num} of words based on
     * the previous {@code words}. The return array is
     * sorted in a descending order based on the probability
     * to be the next word.
     * 
     * @param num
     *            Number of words to suggest
     * @param words
     *            Words used for suggestion
     * @return An array of suggested words
     */
    public String[] suggest(int num, Collection<String> words)
    {

        String[] suggestions = new String[num];

        StringBuffer sb = new StringBuffer();

        Iterator<String> wordsItr = words.iterator();
        while (wordsItr.hasNext())
        {
            sb.append(wordsItr.next() + " ");
        }
        
        String phrase = sb.toString().trim();

        int index = dicWords
                .indexOf(new WordFrequency(phrase, 0));

        if (index == -1)
            return suggestions;

        LinkedList<WordFrequency> terms = new LinkedList<>();

        int i = index + 1;
        while (i < dicWords.size())
        {
            WordFrequency wf = dicWords.get(i);
            String[] splittedWord = wf.word.split(FILE_DELIMITER);
            if (splittedWord.length == words.size() + 1)
            {
                String[] commonTerms = new String[words.size()];
                System.arraycopy(splittedWord, 0, commonTerms, 0,
                        commonTerms.length);
                if (Arrays.equals(commonTerms, words.toArray()))
                {
                    String word = splittedWord[splittedWord.length - 1];
                    int frequency = wf.frequency;
                    terms.add(new WordFrequency(word, frequency));
                }
            }
            else if (!wf.word.contains(phrase))
            {
                break;
            }
            i++;
        }

        Collections.sort(terms, new Comparator<WordFrequency>()
        {

            @Override
            public int compare(WordFrequency o1, WordFrequency o2)
            {
                return o1.frequency < o2.frequency ? 1
                        : (o1.frequency == o2.frequency ? 0 : -1);
            }
        });

        i = 0;

        Iterator<WordFrequency> itr = terms.iterator();
        while (i < num && itr.hasNext())
        {
            suggestions[i] = itr.next().word;
            i++;
        }

        return suggestions;
    }

    /**
     * This method suggests {@code num} of words based on
     * the previous {@code words}. The return array is
     * sorted in a descending order based on the probability
     * to be the next word.
     * 
     * @param num
     *            Number of words to suggest
     * @param words
     *            Words used for suggestion
     * @return An array of suggested words
     */
    public String[] suggest(int num, String... words)
    {
        return suggest(num, Arrays.asList(words));
    }

    /**
     * This method suggests {@code num} of words based on
     * the {@code phrase}. The return array is sorted in a
     * descending order based on the probability to be the
     * next word.
     * 
     * @param num
     *            Number of words to suggest
     * @param phrase
     *            phrase used for suggestion
     * @return An array of suggested words
     */
    public String[] suggest(int num, String phrase)
    {
        return suggest(num, Arrays.asList(phrase.split(INPUT_DELIMITER)));
    }

    /**
     * Inner class used for holding the word and its
     * frequency
     * 
     * @author team5
     * 
     */
    private static class WordFrequency
    {
        private String word;
        private int frequency;

        public WordFrequency(String word, int frequency)
        {
            this.word = word;
            this.frequency = frequency;
        }

        /*
         * (non-Javadoc)
         * 
         * @see java.lang.Object#equals(java.lang.Object)
         */
        @Override
        public boolean equals(Object obj)
        {
            if (obj == null)
                return false;
            if (this.getClass() != obj.getClass())
                return false;
            WordFrequency otherObj = (WordFrequency) obj;
            return this.word.equals(otherObj.word);
        }

    }

    public static void main(String[] args) throws FileNotFoundException
    {
        if (args.length != 1)
        {
            System.err.println("Usage: java WordSuggestion <dictionary path>");
            System.exit(-1);
        }
        
        Scanner in = new Scanner(System.in);
        System.out
                .println("Please input phrase for suggestion (press [Ctrl + C] to exit): ");

        WordSuggestion ws = new WordSuggestion(new File(args[0]));
        while (true)
        {

            String phrase = in.nextLine().toLowerCase().trim();
            String[] suggest = ws.suggest(5, phrase);

            System.out.println(Arrays.toString(suggest));
            System.out.println();
        }

    }
}
