package com.team5.sampling;

import java.io.IOException;
import java.util.HashMap;
import java.util.Random;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import com.team5.util.XMLParser;

/**
 * @fileName SamplingMapper.java
 *
 * @author team5
 *
 * @description This class filters input. It randomly
 *              chooses a specific number of records to
 *              flush to output
 *
 */
public class SamplingMapper extends
        Mapper<LongWritable, Text, LongWritable, Text>
{

    
    /**
     * Number of records to choose
     */
    private static final int SIZE = 100;
    
    /**
     * Number of lines from input
     */
    private static int numline;

    private int count = 0;
    
    private XMLParser parser = new XMLParser();
    
    private Random rand = new Random();
    
    private HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.hadoop.mapreduce.Mapper#setup(org.
     * apache.hadoop.mapreduce.Mapper.Context)
     */
    @Override
    protected void setup(
            Mapper<LongWritable, Text, LongWritable, Text>.Context context)
            throws IOException, InterruptedException
    {
        numline = context.getConfiguration().getInt("linespermap", 10000);
        while (map.size() < SIZE)
        {
            int key = rand.nextInt(numline);
            map.put(key, 0);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.hadoop.mapreduce.Mapper#map(java.lang
     * .Object, java.lang.Object,
     * org.apache.hadoop.mapreduce.Mapper.Context)
     */
    @Override
    protected void map(LongWritable key, Text value,
            Mapper<LongWritable, Text, LongWritable, Text>.Context context)
            throws IOException, InterruptedException
    {
        if (map.containsKey(count))
        {
            if (parser.parse(value))
            {
                context.write(null, value);
            }
        }
        count++;
    }
}
