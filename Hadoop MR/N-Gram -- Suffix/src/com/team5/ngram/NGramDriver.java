package com.team5.ngram;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import com.team5.io.WholeFileInputFormat;

/**
 * @fileName NGramDriver.java
 *
 * @author team5
 *
 * @description Driver class for suffix-sigma algorithm.
 */
public class NGramDriver extends Configured implements Tool
{

    @Override
    public int run(String[] args) throws Exception
    {
        if (args.length != 4)
        {
            System.err.printf("Usage: %s [generic options] <input> <output> <sigma> <tau>\n",
                    getClass().getSimpleName());
            ToolRunner.printGenericCommandUsage(System.err);
            return -1;
        }

        Configuration conf = new Configuration();

        FileSystem fs = FileSystem.get(conf);
        if (fs.exists(new Path(args[1])))
            fs.delete(new Path(args[1]), true);

        Job job = Job.getInstance(conf, "N Gram");
        job.setJarByClass(NGramDriver.class);
        job.getConfiguration().setInt("sigma", Integer.parseInt(args[2]));
        job.getConfiguration().setInt("tau", Integer.parseInt(args[3]));

        job.setMapperClass(NGramMapper.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(LongWritable.class);

        job.setPartitionerClass(FirstPartitioner.class);
        job.setSortComparatorClass(KeyComparator.class);

        job.setReducerClass(NGramReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);

        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));
        job.setInputFormatClass(WholeFileInputFormat.class);

        return job.waitForCompletion(true) ? 0 : 1;
    }

    public static void main(String[] args) throws Exception
    {
        int exitCode = ToolRunner.run(new NGramDriver(), args);
        System.exit(exitCode);
    }

}
