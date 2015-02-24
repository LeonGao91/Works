package com.team5.comments;

import java.net.URI;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.filecache.DistributedCache;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

/**
 * Driver class
 * 
 * @author Team5
 *
 */
public class Driver extends Configured implements Tool
{

    /**
     * run method
     */
    @Override
    public int run(String[] args) throws Exception
    {
        if (args.length != 2)
        {
            System.err.printf("Usage: %s <input> <output>\n", getClass()
                    .getSimpleName());
            ToolRunner.printGenericCommandUsage(System.err);
            return -1;
        }

        Job job = new Job(getConf(), "commentAnalysis");
        Configuration conf = job.getConfiguration();
        // location of the distributed cache
        DistributedCache.addCacheFile(new URI("/user/student006/categories"),
                conf);
        job.setJarByClass(getClass());

        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));

        job.setMapperClass(CommentAnalysisMapper.class);
        job.setReducerClass(CommentAnalysisReducer.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);

        return job.waitForCompletion(true) ? 0 : 1;
    }

    /**
     * Main routine
     * 
     * @param args
     */
    public static void main(String[] args) throws Exception
    {
        int exitCode = ToolRunner.run(new Driver(), args);
        System.exit(exitCode);
    }
}
