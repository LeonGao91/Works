package com.team5.star;

import java.io.File;
import java.io.IOException;

import org.apache.hadoop.filecache.DistributedCache;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import com.team5.util.Classifier;

/**
 * Mapper class that will get take the lines and give
 * reducer user id and the classfied text
 * 
 * @author hduser
 */
public class WhoIsTheStarAnalysisMapper extends
        Mapper<LongWritable, Text, Text, IntWritable>
{

    private Text user = new Text();
    private IntWritable type = new IntWritable();
    private Classifier classifier;

    /**
     * Set up function that will initialize the classifier
     * by training data in distributed cache.
     */
    @Override
    protected void setup(Context context) throws IOException,
            InterruptedException
    {
        Path[] cache = DistributedCache.getLocalCacheFiles(context
                .getConfiguration());
        System.out.println("Getting path");
        for (Path path : cache)
        {
            System.out.println(path.toString());
            classifier = new Classifier(new File(path.toString()), 8);
        }
    }

    /**
     * map function which will do the mapper job
     */
    @Override
    public void map(LongWritable key, Text value, Context context)
            throws IOException, InterruptedException
    {
        String line = value.toString();
        // get userid and text
        int index1 = line.indexOf("Text=");
        int index2 = line.lastIndexOf(" CreationDate=\"");
        int index3 = line.lastIndexOf(" UserId=\"");
        int index4 = line.lastIndexOf("\" />");
        if (index1 < 0 || index2 < 0 || index3 < 0 || index4 < 0)
            return;
        // classify the text into different categories
        switch (classifier.classify(line.substring(index1 + 6, index2 - 1)))
        {
        case "AnotherWay":
            type.set(0);
            break;
        case "Beginner":
            type.set(1);
            break;
        case "Solution":
            type.set(2);
            break;
        case "Thankyou":
            type.set(3);
            break;
        }
        String u = line.substring(index3 + 9, index4);
        user.set(u);
        context.write(user, type);
    }
}