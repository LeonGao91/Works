import java.util.Collection;
import java.util.LinkedList;
import java.util.StringTokenizer;

public class StringStemmer
{
    private StringStemmer()
    {

    }

    /**
     * This method takes a word as input, and return a
     * stemmed word. In fact, it is a wrapper of Port
     * Stemming Algorithm. The whole process consists of 6
     * steps.
     * 
     * Step 1 gets rid of plurals and -ed or -ing.
     * 
     * Step 2 turns terminal y to i when there is another
     * vowel in the stem.
     * 
     * Step 3 maps double suffices to single ones.
     * 
     * Step 4 deals with -ic-, -ful, -ness etc.
     * 
     * Step 5 takes off -ant, -ence etc.
     * 
     * Step 6 removes a final -e.
     * 
     * @param inStr
     *            Word to be stemmed
     * @param useStopWords
     *            True if choose to use stop words
     * @return Word after stem
     */
    public static Collection<String> stem(String inStr, boolean useStopWords)
    {
        LinkedList<String> ll = new LinkedList<String>();
        StringTokenizer st = new StringTokenizer(inStr.replaceAll(
                "\\pP|\\pS|\\pN", " ").toLowerCase());

        while (st.hasMoreTokens())
        {
            Stemmer stemmer = new Stemmer();
            String token = st.nextToken();
            if (useStopWords && isStopWord(token))
                continue;

            stemmer.add(token.toCharArray(), token.length());
            stemmer.stem();
            ll.add(stemmer.toString());
        }

        return ll;
    }

    private static boolean isStopWord(String str)
    {
        switch (str)
        {
        // TODO Could read from file rather than switch
        case "a":
        case "an":
        case "the":
        case "that":
            return true;
        }
        return false;
    }
}
