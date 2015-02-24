package com.team5.comments;

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
 * This mapper is a simple word-count style mapper that will
 * take the text as input, classify it and pass to reducer
 * 
 * @author team 5
 *
 */
public class CommentAnalysisMapper extends
        Mapper<LongWritable, Text, Text, IntWritable>
{

    private Text type = new Text();
    private final static IntWritable one = new IntWritable(1);
    private Classifier classifier;

    /**
     * The setup function will initiate the classifier by
     * training data in distributed cache
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
     * map function
     */
    @Override
    public void map(LongWritable key, Text value, Context context)
            throws IOException, InterruptedException
    {
        String line = value.toString();
        int index1 = line.indexOf("Text=");
        int index2 = line.lastIndexOf(" CreationDate=\"");
        // get rid of corner cases
        if (index1 < 0 || index2 < 0)
            return;
        type.set(classifier.classify(line.substring(index1 + 6, index2 - 1)));
        context.write(type, one);
    }

}