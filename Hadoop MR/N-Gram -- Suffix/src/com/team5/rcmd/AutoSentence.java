package com.team5.rcmd;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

/**
 * @fileName AutoSentence.java
 *
 * @author team5
 *
 * @description This class automatically completes a
 *              sentence based on the first few words. It
 *              uses n-gram to suggest the next word.
 */
public class AutoSentence
{
    /**
     * Int value for "<b>.</b>" (period).
     */
    public static final int PERIOD = 46;

    /**
     * Number of suggestion choices of each round
     */
    public static final int NUM_CHOICE = 5;

    /**
     * Delimiter to parse input
     */
    public static final String DELIMITER = "[^a-zA-Z'.]+";

    /**
     * Random generator to determine which word is used for
     * the sentence
     */
    public static Random rand = new Random();

    public static void main(String[] args) throws FileNotFoundException
    {
        if (args.length != 1)
        {
            System.err.println("Usage: java AutoSentence <dictionary path>");
            System.exit(-1);
        }
        
        Scanner in = new Scanner(System.in);
        System.out.println("Please input phrase for suggestion: (press [Ctrl + C] to stop or exit): ");
        
        WordSuggestion ws = new WordSuggestion(new File(args[0]));

        String start = in.nextLine();
        
        in.close();

        // Hold the base words for suggestion
        ArrayDeque<String> queue = new ArrayDeque<>();

        queue.addAll(Arrays.asList(start.toLowerCase().trim().split(DELIMITER)));
        System.out.print(start + " ");

        StringBuffer sb = new StringBuffer();

        while (true)
        {
            String[] suggestions = ws.suggest(NUM_CHOICE, queue);
            String choice = suggestions[rand.nextInt(NUM_CHOICE)];
            
            /*
             * If there is no candidate for the next word
             */
            if (choice == null)
            {
                /*
                 * If there still exist word in queue,
                 * delete one and use the queue to generate
                 * new word
                 */
                if (queue.size() > 1)
                {
                    sb.append(queue.poll() + " ");
                    continue;
                }
                else
                {
                    break;
                }
            }
            /*
             * If the candidate words denotes the end of a sentence
             */
            else if (choice.lastIndexOf(PERIOD) == choice.length() - 1)
            {
                queue.add(choice);
                System.out.print(choice + " ");
                
                break;
            }
            else
            {
                queue.add(choice);
                System.out.print(choice + " ");
            }

            // Let the user see clearly
            try
            {
                Thread.sleep(250);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }

        // Append rest of the words in the queue
        while (!queue.isEmpty())
        {
            sb.append(queue.poll() + " ");
        }

        System.out.println();
        System.out.println();
        System.out.println("Final sentence: " + sb.toString());
    }
}
