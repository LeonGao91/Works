package com.team5.star;

import java.io.IOException;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

/**
 * Reducer class that will take userid and classified number.
 * And output the userid and counter for each type of texts
 * 
 * @author team5
 *
 */
public class WhoIsTheStarReducer extends Reducer<Text, IntWritable, Text, Text>
{

    /**
     * The reducer function
     */
    @Override
    public void reduce(Text key, Iterable<IntWritable> values, Context context)
            throws IOException, InterruptedException
    {
        // count each type of comment
        int[] counter = new int[4];
        StringBuilder sb = new StringBuilder();
        for (IntWritable val : values)
        {
            counter[val.get()]++;
        }
        sb.append("AnotherWay ").append(counter[0]).append(" Beginner ")
                .append(counter[1]).append(" Solution ").append(counter[2])
                .append(" Thankyou ").append(counter[3]);
        context.write(key, new Text(sb.toString()));
    }
}
