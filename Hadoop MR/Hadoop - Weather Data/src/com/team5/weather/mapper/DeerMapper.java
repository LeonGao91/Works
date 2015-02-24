package com.team5.hw2.mapper;

import java.io.IOException;
import java.util.GregorianCalendar;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import com.team5.hw2.util.NcdcRecordParser;

public class DeerMapper extends Mapper<LongWritable, Text, Text, Text>
{
    Text location = new Text();
    Text criteria = new Text();

    // PA's latitude range
    private static final int paLowLatitude = 39717;
    private static final int paHighLatitude = 42267;

    // Pa's longitude range
    private static final int paLeftLongitude = -80517;
    private static final int paRightLongitude = -74683;

    enum WeatherData
    {
        MALFORMED
    }

    private NcdcRecordParser parser = new NcdcRecordParser();

    @Override
    public void map(LongWritable key, Text value, Context context)
            throws IOException, InterruptedException
    {

        parser.parse(value);
        GregorianCalendar calendar = new GregorianCalendar(parser.getYear(),
                parser.getMonth() - 1, parser.getDay(), parser.getHour(),
                parser.getMinute());

        int latitude = parser.getLatitude();
        int longitude = parser.getLongitude();

        int windSpeed = parser.getWindSpeed();
        String windSpeedQuality = parser.getWindSpeedQuality();

        int visibility = parser.getVisibility();
        String visibilityQuality = parser.getVisibilityQuality();

        int airTemperature = parser.getAirTemperature();
        String airTemperatureQuality = parser.getAirTemperatureQuality();

        int dewPointTemperature = parser.getDewPointTemperature();
        String dewPointTemperatureQuality = parser
                .getDewPointTemperatureQuality();

        if (parser.isMalformed())
        {
            System.err.println("Ignoring possibly corrupt input: " + value);
            context.getCounter(WeatherData.MALFORMED).increment(1);
        }
        else if (isHuntTime(calendar) && isPA(latitude, longitude))
        {
            location.set(latitude + " " + longitude);
            criteria.set(windSpeed + " " + windSpeedQuality + " " + visibility
                    + " " + visibilityQuality + " " + airTemperature + " "
                    + airTemperatureQuality + " " + dewPointTemperature + " "
                    + dewPointTemperatureQuality);
            context.write(location, criteria);
        }
    }

    /**
     * Test whether a position is located in PA, U.S.
     * @param latitude
     * @param longitude
     * @return True if the position is in PA
     */
    private boolean isPA(int latitude, int longitude)
    {
        return (latitude > paLowLatitude) && (latitude < paHighLatitude)
                && (longitude > paLeftLongitude)
                && (longitude < paRightLongitude);
    }

    /**
     * Test whether the specified time is good for hunting deer
     * @param calendar Time to test
     * @return True if the time is good for hunting
     */
    private boolean isHuntTime(GregorianCalendar calendar)
    {
        return (calendar.get(GregorianCalendar.MONTH) == GregorianCalendar.DECEMBER)
                && (calendar.get(GregorianCalendar.DAY_OF_MONTH) >= 1)
                && (calendar.get(GregorianCalendar.DAY_OF_MONTH) <= 13);
    }
}
