package com.team5.ngram;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Partitioner;

/**
 * @fileName FirstPartitioner.java
 *
 * @author team5
 *
 * @description This class implements the partitioner in
 *              suffix-sigma algorithm. It partitions
 *              suffices based on their first term only, so
 *              as to guarantees a single reducer receives
 *              all suffixes which start will the same term.
 */
public class FirstPartitioner extends Partitioner<Text, LongWritable>
{

    @Override
    public int getPartition(Text key, LongWritable value, int numPartitions)
    {
        /**
         * HashCode & Integer.MAX_VALUE to eliminate
         * negative value
         */
        return (key.toString().split("\\W")[0].hashCode() & Integer.MAX_VALUE)
                % numPartitions;
    }

}
