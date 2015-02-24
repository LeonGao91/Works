package com.team5.ngram;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;

/**
 * @fileName KeyComparator.java
 *
 * @author team5
 *
 * @description This class implements the comparator in
 *              suffix-sigma algorithm. It sorts texts in a
 *              reverse order.
 */
public class KeyComparator extends WritableComparator
{
    protected KeyComparator()
    {
        super(Text.class, true);
    }

    /**
     * This method compares two texts and order in a reverse
     * alphabetical order. e.g. Return 1 if
     * {@code a = "abc"} and {@code b = "abd"}.
     * 
     * @param a
     *            Text a
     * @param b
     *            Text b
     * @return 1 if a > b, 0 if 1 == b, -1 if a < b, in a
     *         reverse alphabetical order
     * 
     * @see org.apache.hadoop.io.WritableComparator#compare(org
     *      .apache.hadoop.io.WritableComparable,
     *      org.apache.hadoop.io.WritableComparable)
     */
    @Override
    public int compare(WritableComparable a, WritableComparable b)
    {
        Text t1 = (Text) a;
        Text t2 = (Text) b;
        // Return the reverse order of two strings
        return -t1.compareTo(t2);
    }

}
