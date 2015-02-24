package com.team5.hw2.util;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.Writable;

public class LocationWritable implements Writable
{
    private int latitude;
    private int longitude;

    public LocationWritable(int latitude, int longitude)
    {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public LocationWritable()
    {

    }

    @Override
    public void write(DataOutput out) throws IOException
    {
        out.writeInt(latitude);
        out.writeInt(longitude);
    }

    @Override
    public void readFields(DataInput in) throws IOException
    {
        latitude = in.readInt();
        longitude = in.readInt();
    }

    /**
     * @return the latitude
     */
    public int getLatitude()
    {
        return latitude;
    }

    /**
     * @param latitude
     *            the latitude to set
     */
    public void setLatitude(int latitude)
    {
        this.latitude = latitude;
    }

    /**
     * @return the longitude
     */
    public int getLongitude()
    {
        return longitude;
    }

    /**
     * @param longitude
     *            the longitude to set
     */
    public void setLongitude(int longitude)
    {
        this.longitude = longitude;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode()
    {
        return longitude + latitude;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj)
    {
        if (!(obj instanceof LocationWritable))
            return false;
        LocationWritable other = (LocationWritable) obj;
        return (this.latitude == other.latitude)
                && (this.longitude == other.longitude);
    }

}
