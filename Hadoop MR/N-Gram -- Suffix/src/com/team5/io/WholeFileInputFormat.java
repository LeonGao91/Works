package com.team5.io;

import java.io.IOException;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.JobContext;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.input.*;

/**
 * @fileName WholeFileInputFormat.java
 *
 * @usedBy team5
 *
 * @author Tom White
 *
 * @description This class is the sample code in <i>Hadoop:
 *              The Definite Guide </i>.
 *              WholeFileInputFormat An InputFormat for
 *              reading a whole file as a record
 */
public class WholeFileInputFormat extends
        FileInputFormat<NullWritable, BytesWritable>
{

    @Override
    protected boolean isSplitable(JobContext context, Path file)
    {
        return false;
    }

    @Override
    public RecordReader<NullWritable, BytesWritable> createRecordReader(
            InputSplit split, TaskAttemptContext context) throws IOException,
            InterruptedException
    {
        WholeFileRecordReader reader = new WholeFileRecordReader();
        reader.initialize(split, context);
        return reader;
    }
}
