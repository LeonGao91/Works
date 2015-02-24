package com.team5.util;

import java.io.StringReader;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.apache.hadoop.io.Text;

/**
 * @fileName XMLParser.java
 *
 * @author team5
 *
 * @description This class parses comments.xml into specific
 *              fields
 *
 */
public class XMLParser
{
    private String id;
    private String postId;
    private String score;
    private String text;
    private String creationDate;
    private String userId;

    private boolean isValid;

    public boolean parse(String record)
    {
        if (record.equals("<?xml version=\"1.0\" encoding=\"utf-8\"?>")
                || record.equals("<comments>") || record.equals("</comments>"))
        {
            return false;
        }

        StringReader in = new StringReader(record);
        XMLInputFactory f = XMLInputFactory.newInstance();
        XMLStreamReader parser = null;
        try
        {
            parser = f.createXMLStreamReader(in);
            while (parser.hasNext())
            {
                int event = parser.next();
                if (event == XMLStreamConstants.START_ELEMENT)
                {
                    if (parser.getLocalName().equals("row"))
                    {
                        id = parser.getAttributeValue(null, "Id");
                        postId = parser.getAttributeValue(null, "PostId");
                        score = parser.getAttributeValue(null, "Score");
                        text = parser.getAttributeValue(null, "Text");
                        creationDate = parser.getAttributeValue(null,
                                "CreationDate");
                        userId = parser.getAttributeValue(null, "UserId");
                    }
                    else
                    {
                        return false;
                    }
                }
            }
        }
        catch (XMLStreamException e)
        {
            e.printStackTrace();
        }
        return true;

    }

    public boolean parse(Text record)
    {
        return parse(record.toString());
    }

    /**
     * @return the id
     */
    public String getId()
    {
        return id;
    }

    /**
     * @return the postId
     */
    public String getPostId()
    {
        return postId;
    }

    /**
     * @return the score
     */
    public String getScore()
    {
        return score;
    }

    /**
     * @return the text
     */
    public String getText()
    {
        return text;
    }

    /**
     * @return the creationDate
     */
    public String getCreationDate()
    {
        return creationDate;
    }

    /**
     * @return the userId
     */
    public String getUserId()
    {
        return userId;
    }

    /**
     * @return the isValid
     */
    public boolean isValid()
    {
        return isValid;
    }

}
