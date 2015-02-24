package com.team5.test;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

/**
 * @fileName TestReducer.java
 *
 * @author team5
 *
 * @description this class simply emit word count. It is
 *              used for testing
 */
public class TestReducer extends Reducer<Text, LongWritable, Text, IntWritable>
{
    /**
     * reduce function
     */
    @Override
    public void reduce(Text key, Iterable<LongWritable> values, Context context)
            throws IOException, InterruptedException
    {
        // simple word count
        int counter = 0;
        for (LongWritable one : values)
        {
            counter++;
        }
        context.write(key, new IntWritable(counter));
    }
}
