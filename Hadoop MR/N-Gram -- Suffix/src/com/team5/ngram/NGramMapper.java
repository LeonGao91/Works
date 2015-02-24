package com.team5.ngram;

import java.io.IOException;

import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

/**
 * @fileName NGramMapper.java
 *
 * @author team5
 *
 * @description This class implements the mapper in
 *              suffix-sigma algorithm. It emits all the
 *              n-grams in a file. Notice that it uses all
 *              non-English characters as delimiters, and
 *              turns all characters into lower cases.
 *
 */
public class NGramMapper extends
        Mapper<NullWritable, BytesWritable, Text, LongWritable>
{

    /**
     * Delimiter. It uses all non-English characters as
     * delimiters
     */
    public static final String DELIMITER = "[^a-zA-Z']+";

    /**
     * Length of the n-gram
     */
    private static int SIGMA;

    /**
     * Dummy one representing one occurrence of the n-gram
     */
    private static final LongWritable ONE = new LongWritable(1);

    /**
     * This method gets sigma (n of n-gram) from
     * configuration to set up the sigma for n-gram
     * 
     * @param context
     * 
     * @see org.apache.hadoop.mapreduce.Mapper#setup(org.apache.hadoop.mapreduce.Mapper.Context)
     */

    @Override
    protected void setup(
            Mapper<NullWritable, BytesWritable, Text, LongWritable>.Context context)
            throws IOException, InterruptedException
    {
        SIGMA = context.getConfiguration().getInt("sigma", 4);
    }

    /**
     * this method emits all n-grams, from 1 (inclusive) to
     * n (inclusive), along with dummy value
     * 
     * @param key
     * @param value
     * @param context
     * @see org.apache.hadoop.mapreduce.Mapper#map(java.lang.
     *      Object, java.lang.Object,
     *      org.apache.hadoop.mapreduce.Mapper.Context)
     */
    @Override
    protected void map(
            NullWritable key,
            BytesWritable value,
            Mapper<NullWritable, BytesWritable, Text, LongWritable>.Context context)
            throws IOException, InterruptedException
    {
        String[] terms = new String(value.getBytes()).toLowerCase().split(
                DELIMITER);

        if (terms == null)
            return;
        if (terms.length == 0)
            return;

        int back = 0;
        while (back < terms.length - SIGMA)
        {
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < SIGMA - 1; i++)
            {
                sb.append(terms[back + i] + " ");
            }
            sb.append(terms[back + SIGMA - 1]);
            context.write(new Text(sb.toString()), ONE);
            back++;
        }

        while (back < terms.length)
        {
            StringBuffer sb = new StringBuffer();
            for (int i = back; i < terms.length - 1; i++)
            {
                sb.append(terms[i] + " ");
            }
            sb.append(terms[terms.length - 1]);
            context.write(new Text(sb.toString()), ONE);
            back++;
        }
    }

}
