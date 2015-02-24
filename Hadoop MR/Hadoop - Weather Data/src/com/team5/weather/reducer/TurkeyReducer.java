package com.team5.hw2.reducer;

import java.io.IOException;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class TurkeyReducer extends Reducer<Text, Text, Text, DoubleWritable>
{

    @Override
    public void reduce(Text key, Iterable<Text> values, Context context)
            throws IOException, InterruptedException
    {
        double[] dimensions = new double[4];
        int[] counter = new int[4];
        for (Text value : values)
        {
            String[] dimensionsValue = value.toString().split(" ");
            for (int i = 0; i < dimensionsValue.length; i = i + 2)
            {
                if ((!dimensionsValue[i + 1].matches("[01459]"))
                        || dimensionsValue[i].contains("9999"))
                {
                    continue;
                }
                else
                {
                    dimensions[i / 2] += Double.parseDouble(dimensionsValue[i]);
                    counter[i / 2]++;
                }
            }
        }
        for (int i = 0; i < 4; i++)
        {
            if (counter[i] > 0)
            {
                dimensions[i] = dimensions[i] / counter[i];
            }
        }

        context.write(key, new DoubleWritable(HuntRate(dimensions)));
    }

    /**
     * This method calculates the idealness rate for hunting
     * turkey. The rate ranges in [0, 4], and the higher,
     * the better. This method takes wind speed, visibility,
     * air temperature, and dew point temperature, to
     * calculates the idealness.
     * 
     * @param dimensions
     *            Array of wind speed, visibility, air
     *            temperature, and dew point temperature.
     *            The sequence should be as stated. E.g.
     *            dimensions[0] should be wind speed,
     *            dimensions[1] should be visibility, etc.
     * @return Rate of idealness
     */
    private double HuntRate(double[] dimensions)
    {
        double windSpeedRating = 10.0 / (dimensions[0] + 10);
        double visibilityRating = dimensions[1] / 160000.0;
        double temperatureRating = (dimensions[2] > 40 && dimensions[2] < 60) ? 1
                : 1 - Math.abs((dimensions[2] - 50) / 50.0);
        temperatureRating = temperatureRating < 0 ? 0 : temperatureRating;
        double humidity = Math
                .pow(((112 - 0.1 * dimensions[2] / 10.0 + dimensions[3] / 10.0) / (112 + 0.9 * dimensions[2] / 10.0)),
                        8) * 100;
        double humidityRating = (humidity > 60 && humidity < 70) ? 1 : 1 - Math
                .abs((humidity - 65) / 65.0);
        
        return windSpeedRating + visibilityRating + temperatureRating
                + humidityRating;
    }
}
