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
        if ("time".equals(qName)) {
            String t2 = attributes.getValue("from").replaceAll(".*T|Z", "");
            attributes.getValue("from");
            //System.out.println(attributes.getValue("from") + " " + attributes.getValue(0));
        }
    }

    private void validateLookupTime(String time) {
        if (!time.matches("([0-1][0-9]|2[0-3]):00:00")) {
            System.out.println("No");
            throw new IllegalArgumentException();
        } else {
            System.out.println("yes");
        }
    }

    public void setlookupTime(String time) {
        this.validateLookupTime(time);
        this.lookupTime = time;
    }
}
