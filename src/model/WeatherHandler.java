package model;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

public class WeatherHandler extends DefaultHandler {

    private String lookupTime;

    boolean timeFound = false,
            temperatureFound = false;

    @Override
    public void startElement(String uri, String localName,
                             String qName, Attributes attributes) {
        if ("lookupTime".equals(qName)) {
            System.out.println(attributes.getQName(0) + " " + attributes.getValue(0));
        }
    }

    public void setlookupTime(String time) {
        this.lookupTime = time;
    }
}
