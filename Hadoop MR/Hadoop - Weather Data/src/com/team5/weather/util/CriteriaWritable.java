package com.team5.hw2.util;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.Writable;

public class CriteriaWritable implements Writable
{
    // Divide by 10
    private int windSpeed;

    private int visibility;

    private int airTemperature;

    private int dewPointTemperature;

    public CriteriaWritable(int windSpeed, int visibility, int airTemperature,
            int dewPointTemperature)
    {
        this.windSpeed = windSpeed;
        this.visibility = visibility;
        this.airTemperature = airTemperature;
        this.dewPointTemperature = dewPointTemperature;
    }

    public CriteriaWritable()
    {

    }

    @Override
    public void write(DataOutput out) throws IOException
    {
        out.writeInt(windSpeed);
        out.writeInt(visibility);
        out.writeInt(airTemperature);
        out.writeInt(dewPointTemperature);
    }

    @Override
    public void readFields(DataInput in) throws IOException
    {
        windSpeed = in.readInt();
        visibility = in.readInt();
        airTemperature = in.readInt();
        dewPointTemperature = in.readInt();
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode()
    {
        return windSpeed + airTemperature + dewPointTemperature + visibility;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj)
    {
        if (!(obj instanceof CriteriaWritable))
            return false;
        CriteriaWritable other = (CriteriaWritable) obj;
        return (this.airTemperature == other.airTemperature)
                && (this.dewPointTemperature == other.dewPointTemperature)
                && (this.visibility == other.visibility)
                && (this.windSpeed == other.windSpeed);
    }

    /**
     * @return the windSpeed
     */
    public int getWindSpeed()
    {
        return windSpeed;
    }

    /**
     * @param windSpeed
     *            the windSpeed to set
     */
    public void setWindSpeed(int windSpeed)
    {
        this.windSpeed = windSpeed;
    }

    /**
     * @return the visibility
     */
    public int getVisibility()
    {
        return visibility;
    }

    /**
     * @param visibility
     *            the visibility to set
     */
    public void setVisibility(int visibility)
    {
        this.visibility = visibility;
    }

    /**
     * @return the airTemperature
     */
    public int getAirTemperature()
    {
        return airTemperature;
    }

    /**
     * @param airTemperature
     *            the airTemperature to set
     */
    public void setAirTemperature(int airTemperature)
    {
        this.airTemperature = airTemperature;
    }

    /**
     * @return the dewPointTemperature
     */
    public int getDewPointTemperature()
    {
        return dewPointTemperature;
    }

    /**
     * @param dewPointTemperature
     *            the dewPointTemperature to set
     */
    public void setDewPointTemperature(int dewPointTemperature)
    {
        this.dewPointTemperature = dewPointTemperature;
    }

}
