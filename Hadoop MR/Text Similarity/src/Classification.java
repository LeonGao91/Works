import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.Scanner;

public class Classification
{
    public static void main(String[] args)
    {
        Properties p = System.getProperties();
        try
        {
            // Load properties from file
            p.load(new FileInputStream("hw1.properties"));
        }
        catch (FileNotFoundException e1)
        {
            e1.printStackTrace();
            System.exit(1);
        }
        catch (IOException e1)
        {
            e1.printStackTrace();
            System.exit(1);
        }

        // Read property values from file
        // File path that saves the word count of William
        // Shakespeare vocabulary
        String WSFilePath = System.getProperty("WSFilePath");

        // File path that saves the word count of Conan
        // Doyle vocabulary
        String CDFilePath = System.getProperty("CDFilePath");

        // File path that saves the word count of William
        // Shakespeare vocabulary plus new file vocabulary
        String newWSFileDirPath = System.getProperty("newWSFilePath");

        // File path that saves the word count of Conan
        // Doyle vocabulary plus new file vocabulary
        String newCDFileDirPath = System.getProperty("newCDFilePath");

        File WSFile = new File(WSFilePath);
        File CDFile = new File(CDFilePath);
        File newWSFile = new File(newWSFileDirPath);
        File newCDFile = new File(newCDFileDirPath);

        try
        {
            int WScount = countLines(WSFile);
            int CDcount = countLines(CDFile);
            int newWSCount = countLines(newWSFile);
            int newCDCount = countLines(newCDFile);

            // Count the number of new words in the new file
            // after comparing new file with William
            // Shakespeare vocabulary
            double catWS = newWSCount - WScount;

            // Count the number of new words in the new file
            // after comparing new file with Conan Doyle
            // vocabulary
            double catCD = newCDCount - CDcount;

            System.out
                    .println("Words increased compared to vocabulary of William Shakespeare: "
                            + catWS);
            System.out
                    .println("Words increased compared to vocabulary of Conan Doyle: "
                            + catCD);

            System.out.println();
            System.out.println("Conclusion: ");

            // Classifying the new file to the certain
            // category that have less new word derived
            if (catWS > catCD)
            {
                System.out
                        .println("The new file is more likely to be writen by Conan Doyle.");
            }
            else if ((catWS < catCD))
            {
                System.out
                        .println("File is more likely to be writen by William Shakespeare.");
            }
            else
            {
                System.out
                        .println("Cannot decide which author the new file belong to");
            }

            System.out.println();
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
    }

    public static int countLines(File f) throws FileNotFoundException
    {
        int count = 0;
        Scanner in = null;
        in = new Scanner(new FileInputStream(f));

        while (in.hasNextLine())
        {
            in.nextLine();
            count++;
        }
        in.close();
        return count;
    }
}
