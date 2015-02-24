package com.team5.util;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

/**
 * @fileName ClassifierTest.java
 *
 * @author team5
 *
 * @description This is a test class for {@code Classifier}
 *
 */
public class ClassifierTest
{
    private static Classifier classifier;
    private static XMLParser parser = new XMLParser();
    public static int counter = 0;

    public static void main(String[] args) throws IOException
    {
        classifier = new Classifier(args[0], 8);

        for (String s : classifier.getCategories())
        {
            new File(args[2], s).mkdir();
        }

        Scanner in = new Scanner(new File(args[1]));
        PrintWriter out;
        String line;
        while (in.hasNextLine())
        {
            line = in.nextLine();
            if (!line.isEmpty())
            {
                if (parser.parse(line))
                {
                    String category = classifier.classify(parser.getText());
                    out = new PrintWriter(new File(args[2] + File.separator
                            + category, numGenerator() + ".txt"));
                    out.write(parser.getText());
                    out.close();
                }
            }
        }
        in.close();
    }

    private static String numGenerator()
    {
        String number = "0000000000" + counter;
        counter++;
        return number.substring(number.length() - 8, number.length());
    }

}
