package com.team5.ngram;

import java.io.IOException;
import java.util.Stack;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

/**
 * @fileName NGramReducer.java
 *
 * @author team5
 *
 * @description This class implements the reducer in
 *              suffix-sigma algorithm. It uses two stacks
 *              to emit all n-grams that meet a certain
 *              frequency requirement.
 */
public class NGramReducer extends
        Reducer<Text, LongWritable, Text, IntWritable>
{
    /**
     * Frequency threshold
     */
    private static int TAU;

    /**
     * Stack that holds terms
     */
    private static Stack<String> terms = new Stack<>();

    /**
     * Stack that holds counts of the terms
     */
    private static Stack<Integer> counts = new Stack<>();

    /**
     * This method gets tau (frequency threshold) from
     * configuration to set up the sigma for n-gram
     * 
     * @see org.apache.hadoop.mapreduce.Reducer#setup(org.apache.hadoop.mapreduce.Reducer.Context)
     */
    @Override
    protected void setup(
            Reducer<Text, LongWritable, Text, IntWritable>.Context context)
            throws IOException, InterruptedException
    {
        TAU = context.getConfiguration().getInt("tau", 5);
    }

    /**
     * reduce function
     */
    @Override
    public void reduce(Text key, Iterable<LongWritable> values, Context context)
            throws IOException, InterruptedException
    {
        // simple word count
        int counter = 0;
        int lcp = 0;
        // case insensitive?
        String[] s = key.toString().trim().split(" ");
        StringBuilder sb = new StringBuilder();
        for (LongWritable one : values)
        {
            counter++;
        }
        for (int i = 0; i < Math.min(s.length, terms.size()); i++)
        {
            if (s[i].equals(terms.get(i)))
            {
                lcp++;
            }
            else
                break;
        }
        while (lcp < terms.size())
        {
            if (counts.peek() >= TAU)
            {
                for (int i = 0; i < terms.size(); i++)
                {
                    sb.append(terms.get(i)).append(" ");
                }
                context.write(new Text(sb.toString().trim()), new IntWritable(
                        counts.peek()));
                sb.setLength(0);
            }
            terms.pop();
            if (counts.size() == 1)
                counts.pop();
            else
                counts.push(counts.pop() + counts.pop());
        }
        if (terms.size() == s.length)
            counts.push(counts.pop() + counter);
        else
        {
            for (int i = lcp; i < s.length; i++)
            {
                terms.push(s[i]);
                counts.push((i == s.length - 1 ? counter : 0));
            }
        }
    }

    /**
     * This method emits the rest of terms in the stack.
     * 
     * @see org.apache.hadoop.mapreduce.Reducer#cleanup(org.apache
     *      .hadoop.mapreduce.Reducer.Context)
     */
    @Override
    protected void cleanup(
            Reducer<Text, LongWritable, Text, IntWritable>.Context context)
            throws IOException, InterruptedException
    {
        StringBuilder sb = new StringBuilder();
        while (!terms.isEmpty())
        {
            if (counts.peek() >= TAU)
            {
                for (int i = 0; i < terms.size(); i++)
                {
                    sb.append(terms.get(i)).append(" ");
                }
                context.write(new Text(sb.toString().trim()), new IntWritable(
                        counts.peek()));
                sb.setLength(0);
            }
            terms.pop();
            if (counts.size() == 1)
                counts.pop();
            else
                counts.push(counts.pop() + counts.pop());
        }
    }

}
