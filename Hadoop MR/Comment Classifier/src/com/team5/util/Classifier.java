package com.team5.util;

import java.io.File;
import java.io.IOException;
import com.aliasi.classify.Classification;
import com.aliasi.classify.Classified;
import com.aliasi.classify.DynamicLMClassifier;
import com.aliasi.lm.NGramProcessLM;
import com.aliasi.util.Files;

/**
 * @fileName Classifier.java
 *
 * @author team5
 *
 * @description This class uses n-gram language model to
 *              train text files and to classify new text.
 *              It is a wrapper of
 *              {@code DynamicLMClassifier} in LingPipe
 *
 */
public class Classifier
{
    private String[] categories;


    private DynamicLMClassifier<NGramProcessLM> classifier;

    /**
     * This constructor takes a path and a number to
     * initialize the classifier.
     * 
     * @param path
     *            Directory which contains files being
     *            training. Each directory under
     *            {@code path} is considered as a category.
     * @param nGram
     *            N gram number for the language model
     * @throws IOException
     *             If file does not exit
     */
    public Classifier(File path, int nGram) throws IOException
    {
        if (!path.isDirectory())
            throw new IllegalArgumentException("Illegal Directory: " + path);

        if (nGram < 1)
            throw new IllegalArgumentException("Illegal N Gram: " + nGram);

        // Determine categories using sub-directory
        categories = path.list();

        // Initial n gram language model
        classifier = DynamicLMClassifier.createNGramProcess(categories, nGram);

        train(path);
    }

    /**
     * This constructor takes a path and a number to
     * initialize the classifier.
     * 
     * @param path
     *            Directory which contains files being
     *            training. Each directory under
     *            {@code path} is considered as a category.
     * @param nGram
     *            N gram number for the language model
     * @throws IOException
     *             If file does not exit
     */
    public Classifier(String path, int nGram) throws IOException
    {
        this(new File(path), nGram);
    }

    /**
     * This constructor takes a path to initialize the
     * classifier.
     * 
     * @param path
     *            Directory which contains files being
     *            training. Each directory under
     *            {@code path} is considered as a category.
     * @throws IOException
     *             If file does not exit
     */
    public Classifier(String path) throws IOException
    {
        this(path, 8);
    }

    /**
     * This constructor takes a path to initialize the
     * classifier.
     * 
     * @param path
     *            Directory which contains files being
     *            training. Each directory under
     *            {@code path} is considered as a category.
     * @throws IOException
     *             If file does not exit
     */
    public Classifier(File path) throws IOException
    {
        this(path, 8);
    }
    
    /**
     * @return the categories
     */
    public String[] getCategories()
    {
        return categories;
    }

    /**
     * Train the model using files under {@link Classifier#path}
     * @throws IOException
     */
    private void train(File path) throws IOException
    {
        int numTrainingCases = 0;
        int numTrainingChars = 0;

        System.out.println("\nTraining.");

        // For each file in each category, memorize its
        // category
        for (int i = 0; i < categories.length; i++)
        {
            String category = categories[i];

            // Given the best category, initialize
            // classification model
            Classification classification = new Classification(category);

            // Directory of the current category
            File file = new File(path, categories[i]);
            File[] trainFiles = file.listFiles();
            for (int j = 0; j < trainFiles.length; j++)
            {
                File trainFile = trainFiles[j];
                numTrainingCases++;
                String review = Files.readFromFile(trainFile, "ISO-8859-1");
                numTrainingChars += review.length();

                // Memorize a classified object
                Classified<CharSequence> classified = new Classified<CharSequence>(
                        review, classification);
                classifier.handle(classified);
            }
        }
        System.out.println(" # Training Cases=" + numTrainingCases);
        System.out.println(" # Training Chars=" + numTrainingChars);
    }

    /**
     * This method use provided sample to evaluates the model 
     * @param path Sample directory
     * @param category Category of the sample
     * @throws IOException If file does not exist
     */
    public void evaluate(File path, String category) throws IOException
    {
        if (!path.isDirectory())
            throw new IllegalArgumentException("Illegal Directory: " + path);
            
        System.out.println("Evaluating.");
        int numTests = 0;
        int numCorrect = 0;
        File[] evalFiles = path.listFiles();
        for (int j = 0; j < evalFiles.length; j++)
        {
            File evalFile = evalFiles[j];
            String review = Files.readFromFile(evalFile, "ISO-8859-1");
            numTests++;
            Classification classification = classifier.classify(review);

            System.out.println(evalFile.getName() + " classified as "
                    + classification.bestCategory() + "(" + category + ")");
            if (classification.bestCategory().equals(category))
                numCorrect++;
        }
        System.out.println(" # Test Cases=" + numTests);
        System.out.println(" # Correct=" + numCorrect);
        System.out.println(" % Correct="
                + ((double) numCorrect / (double) numTests));

    }

    /**
     * Classify new text using trained model.
     * @param text Text to be tested
     * @return Category to which text belongs 
     */
    public String classify(CharSequence text)
    {
        return classifier.classify(text).bestCategory();
    }
    
}
