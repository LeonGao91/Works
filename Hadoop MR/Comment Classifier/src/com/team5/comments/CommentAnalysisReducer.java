package com.team5.comments;

import java.io.IOException;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

/**
 * Reducer that will count the total number of each type of
 * comments
 * 
 * @author team 5
 */
public class CommentAnalysisReducer extends
        Reducer<Text, IntWritable, Text, IntWritable>
{
    Text word = new Text();
    Text filename = new Text();

    /**
     * reduce function
     */
    @Override
    public void reduce(Text key, Iterable<IntWritable> values, Context context)
            throws IOException, InterruptedException
    {
        int sum = 0;
        for (IntWritable val : values)
        {
            sum += val.get();
        }
        context.write(key, new IntWritable(sum));
    }
}
