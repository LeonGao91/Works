package com.team5.hw2.huntloc;

import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import com.team5.hw2.mapper.DeerMapper;
import com.team5.hw2.mapper.TurkeyMapper;
import com.team5.hw2.reducer.DeerReducer;
import com.team5.hw2.reducer.TurkeyReducer;

//Identical to v5 except for v7 mapper
public class HuntLocationDriver extends Configured implements Tool {

  @Override
  public int run(String[] args) throws Exception {
    if (args.length != 3) {
      System.err.printf("Usage: %s [generic options] <huntee> <input> <output>\n",
          getClass().getSimpleName());
      ToolRunner.printGenericCommandUsage(System.err);
      return -1;
    }
    
    Job job = new Job(getConf(), "Hunting Location");
    job.setJarByClass(getClass());
    
    String huntee = args[0];

    FileInputFormat.addInputPath(job, new Path(args[1]));
    FileOutputFormat.setOutputPath(job, new Path(args[2]));
    
    if (huntee.equals("turkey"))
    {
    job.setMapperClass(TurkeyMapper.class);
    job.setReducerClass(TurkeyReducer.class);
    }
    else if (huntee.equals("deer"))
    {
        job.setMapperClass(DeerMapper.class);
        job.setReducerClass(DeerReducer.class);
    }
    else
    {
        System.err.print("<huntee> usage: [turkey | deer]");
        return -1;
    }

    job.setOutputKeyClass(Text.class);
    job.setOutputValueClass(Text.class);
    
    return job.waitForCompletion(true) ? 0 : 1;
  }
  
  public static void main(String[] args) throws Exception {
    int exitCode = ToolRunner.run(new HuntLocationDriver(), args);
    System.exit(exitCode);
  }
}
