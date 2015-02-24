package com.team5.sampling;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.NLineInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

/**
 * @fileName SamplingDriver.java
 *
 * @author team5
 *
 * @description This is a driver class to run sampling
 *
 *
 */
public class SamplingDriver extends Configured implements Tool
{
    
    private static final int NUM_LINES = 100000;
    

    @Override
    public int run(String[] args) throws Exception
    {
        if (args.length != 2)
        {
            System.err.printf("Usage: %s [generic options] <input> <output>\n",
                    getClass().getSimpleName());
            ToolRunner.printGenericCommandUsage(System.err);
            return -1;
        }

        Configuration conf = new Configuration();

        FileSystem fs = FileSystem.get(conf);
        if (fs.exists(new Path(args[1])))
            fs.delete(new Path(args[1]), true);

        Job job = Job.getInstance(conf, "N Line Sampling");
        job.setJarByClass(SamplingDriver.class);

        job.setMapperClass(SamplingMapper.class);
        job.setMapOutputKeyClass(LongWritable.class);
        job.setMapOutputValueClass(Text.class);

        job.setNumReduceTasks(0);

        // Split files every NUM_LINES 
        job.setInputFormatClass(NLineInputFormat.class);
        NLineInputFormat.addInputPath(job, new Path(args[0]));
        NLineInputFormat.setNumLinesPerSplit(job, NUM_LINES);
        job.getConfiguration().setInt("linespermap", NUM_LINES);

        FileOutputFormat.setOutputPath(job, new Path(args[1]));

        return job.waitForCompletion(true) ? 0 : 1;
    }

    public static void main(String[] args) throws Exception
    {
        int exitCode = ToolRunner.run(new SamplingDriver(), args);
        System.exit(exitCode);
    }
}
