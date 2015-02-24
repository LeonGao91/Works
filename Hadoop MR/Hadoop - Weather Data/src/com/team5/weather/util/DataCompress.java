package com.team5.hw2.util;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.FileUtil;
import org.apache.hadoop.fs.Path;

public class DataCompress
{
    public static void main(String[] args)
    {
        String srcPath = args[0];
        String destPath = args[1];

        Configuration conf = new Configuration();
        try
        {
            FileSystem fs = FileSystem.get(conf);

            System.out.println("Start Merging...");
            long start = System.currentTimeMillis();

            FileUtil.copyMerge(fs, new Path(srcPath), fs, new Path(destPath),
                    false, conf, null);

            long end = System.currentTimeMillis();
            System.out.println("Successfully merged, taking " + (end - start)
                    + "milliseconds");

        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
