package com.team5.hw2.util;

import org.apache.hadoop.io.Text;

public class NcdcRecordParser
{

    private static final int MISSING_TEMPERATURE = 9999;

    private int year;

    private int month;

    private int day;

    // 24-hour
    private int hour;

    private int minute;

    // Divide by 1000
    private int latitude;

    // Divide by 1000
    private int longitude;

    // From 0 - 360
    private int windDirectionAngle;

    // 0, 1, 4, 5, 9
    private String windDirectionQuality;

    // Used in rating
    private String windTypeCode;

    // Divide by 10
    private int windSpeed;

    // 0, 1, 4, 5, 9
    private String windSpeedQuality;

    private int visibility;

    // 0, 1, 4, 5, 9
    private String visibilityQuality;

    // visibility-variablity code： 86（？？N： not
    // variable，v：variable，9：missing）
    private String visibilityVarCode;

    private int airTemperature;

    // 0, 1, 4, 5, 9
    private String airTemperatureQuality;

    private int dewPointTemperature;

    // 0, 1, 4, 5, 9
    private String dewPointTemperatureQuality;

    private int airPressure;

    private String airPressureQuality;

    private boolean malformed;

    public void parse(String record)
    {
        malformed = false;

        // Date. 16-23
        year = Integer.parseInt(record.substring(15, 19));
        month = Integer.parseInt(record.substring(19, 21));
        day = Integer.parseInt(record.substring(21, 23));

        // Time. 24-27
        hour = Integer.parseInt(record.substring(23, 25));
        minute = Integer.parseInt(record.substring(25, 27));

        // Latitude. 29-34
        if (record.charAt(28) == '+')
        {
            latitude = Integer.parseInt(record.substring(29, 34));
        }
        else if (record.charAt(28) == '-')
        {
            latitude = Integer.parseInt(record.substring(28, 34));
        }
        else
        {
            malformed = true;
        }

        // Longitude. 35-41
        if (record.charAt(34) == '+')
        {
            longitude = Integer.parseInt(record.substring(35, 41));
        }
        else if (record.charAt(34) == '-')
        {
            longitude = Integer.parseInt(record.substring(34, 41));
        }
        else
        {
            malformed = true;
        }

        // wind-direction angle：61-63
        windDirectionAngle = Integer.parseInt(record.substring(60, 63));

        // wind-direction quality code：64 ([01459]）
        windDirectionQuality = record.substring(63, 64);

        // wind type code：65 ( used in rating)
        windTypeCode = record.substring(64, 65);

        // wind speed rate：66-69 (divided by 10)
        windSpeed = Integer.parseInt(record.substring(65, 69));

        // wind-speed rate quality code：70 ([01459]）
        windSpeedQuality = record.substring(69, 70);

        // visibility-distance：79-84
        visibility = Integer.parseInt(record.substring(78, 84));

        // visibility-distance quality code：85 ([01459])
        visibilityQuality = record.substring(84, 85);

        // visibility-variablity code： 86（？？N： not
        // variable，v：variable，9：missing）
        visibilityVarCode = record.substring(85, 86);

        // air temperature：88-92 (divide by 10)
        // Remove leading plus sign as parseInt doesn't like
        // them
        if (record.charAt(87) == '+')
        {
            airTemperature = Integer.parseInt(record.substring(88, 92));
        }
        else if (record.charAt(87) == '-')
        {
            airTemperature = Integer.parseInt(record.substring(87, 92));
        }
        else
        {
            malformed = true;
        }

        // air temperature quality code： 93 ([01459])
        airTemperatureQuality = record.substring(92, 93);

        // air temperature dew point temperature：94-98
        // (divide by 10)
        if (record.charAt(93) == '+')
        {
            dewPointTemperature = Integer.parseInt(record.substring(94, 98));
        }
        else if (record.charAt(93) == '-')
        {
            dewPointTemperature = Integer.parseInt(record.substring(93, 98));
        }
        else
        {
            malformed = true;
        }

        // air temperature dew point
        // temperature-quality：99([01459])
        dewPointTemperatureQuality = record.substring(98, 99);

        // ATMOSPHERIC-PRESSURE:100-104 (divide by 10)
        airPressure = Integer.parseInt(record.substring(99, 104));

        // ATMOSPHERIC-PRESSURE quality：105 ([01459])
        airPressureQuality = record.substring(104, 105);

    }

    public void parse(Text record)
    {
        parse(record.toString());
    }

    public boolean isValidTemperature()
    {
        return !malformed && airTemperature != MISSING_TEMPERATURE
                && airTemperatureQuality.matches("[01459]");
    }

    /**
     * @return the year
     */
    public int getYear()
    {
        return year;
    }

    /**
     * @return the month
     */
    public int getMonth()
    {
        return month;
    }

    /**
     * @return the day
     */
    public int getDay()
    {
        return day;
    }

    /**
     * @return the hour
     */
    public int getHour()
    {
        return hour;
    }

    /**
     * @return the minute
     */
    public int getMinute()
    {
        return minute;
    }

    /**
     * @return the latitude
     */
    public int getLatitude()
    {
        return latitude;
    }

    /**
     * @return the longitude
     */
    public int getLongitude()
    {
        return longitude;
    }

    /**
     * @return the windDirectionAngle
     */
    public int getWindDirectionAngle()
    {
        return windDirectionAngle;
    }

    /**
     * @return the windDirectionQuality
     */
    public String getWindDirectionQuality()
    {
        return windDirectionQuality;
    }

    /**
     * @return the windTypeCode
     */
    public String getWindTypeCode()
    {
        return windTypeCode;
    }

    /**
     * @return the windSpeed
     */
    public int getWindSpeed()
    {
        return windSpeed;
    }

    /**
     * @return the windSpeedQuality
     */
    public String getWindSpeedQuality()
    {
        return windSpeedQuality;
    }

    /**
     * @return the visibility
     */
    public int getVisibility()
    {
        return visibility;
    }

    /**
     * @return the visibilityQuality
     */
    public String getVisibilityQuality()
    {
        return visibilityQuality;
    }

    /**
     * @return the visibilityVarCode
     */
    public String getVisibilityVarCode()
    {
        return visibilityVarCode;
    }

    /**
     * @return the airTemperature
     */
    public int getAirTemperature()
    {
        return airTemperature;
    }

    /**
     * @return the airTemperatureQuality
     */
    public String getAirTemperatureQuality()
    {
        return airTemperatureQuality;
    }

    /**
     * @return the dewPointTemperature
     */
    public int getDewPointTemperature()
    {
        return dewPointTemperature;
    }

    /**
     * @return the dewPointTemperatureQuality
     */
    public String getDewPointTemperatureQuality()
    {
        return dewPointTemperatureQuality;
    }

    /**
     * @return the airPressure
     */
    public int getAirPressure()
    {
        return airPressure;
    }

    /**
     * @return the airPressureQuality
     */
    public String getAirPressureQuality()
    {
        return airPressureQuality;
    }

    /**
     * @return the malformed
     */
    public boolean isMalformed()
    {
        return malformed;
    }

    public static void main(String[] args)
    {
        String record = "0188010010999992013010100004"
                + "+70933-008667FM-12+0009ENJA "
                + "V0203501N009010012019N0035001N1"
                + "-00331-00411099021ADDAA106000021"
                + "AY151061AY221061GF10899108106100150"
                + "1999999MA1999999098901MD1210101+9999MW1561REMSYN088AAXX"
                + "  01001 01001 11235 83509 11033 21041 39890 49902 52010"
                + " 69901 75652 886// 333 91116;";

        NcdcRecordParser parser = new NcdcRecordParser();
        parser.parse(record);
        System.out.println(parser.getLatitude() + " " + parser.getLongitude());
        
        System.out.println(parser.getWindSpeed());
        System.out.println(parser.getWindDirectionQuality());
        
        System.out.println(parser.getVisibility());
        System.out.println(parser.getVisibilityQuality());
        
        System.out.println(parser.getAirTemperature());
        System.out.println(parser.getAirTemperatureQuality());
        
        System.out.println(parser.getDewPointTemperature());
        System.out.println(parser.getDewPointTemperatureQuality());
        
        
        
    }

}
