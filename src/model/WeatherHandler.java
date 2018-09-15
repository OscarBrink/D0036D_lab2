package model;

import javafx.util.Pair;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

public class WeatherHandler extends DefaultHandler {

    private String lookupTime;

    boolean timeFound = false,
            temperatureFound = false;

    private Pair<String, String> weatherData;

    @Override
    public void startElement(String uri, String localName,
                             String qName, Attributes attributes) {

        if ("time".equals(qName)) {

            //Testing
            String t2 = attributes.getValue("from").replaceAll(".*T|Z", "");

            if (lookupTime.equals(attributes.getValue("from").replaceAll(".*T|Z", ""))) {
                this.timeFound = true;
            }
            attributes.getValue("from");
            //System.out.println(attributes.getValue("from") + " " + attributes.getValue(0));
        } else if (timeFound && "temperature".equals(qName)) {
            weatherData = new Pair<>("temperature", attributes.getValue("value"));
            this.endParse();
        }
    }

    private void endParse() {
        this.resetState();
        throw new XMLDataRetrievedException(this.weatherData);
    }

    private void resetState() {
        this.timeFound = false;
        this.temperatureFound = false;
    }

    private void validateLookupTime(String time) {
        if (!time.matches("([0-1][0-9]|2[0-3]):00:00")) {
            throw new IllegalArgumentException();
        }
    }

    public void setlookupTime(String time) {
        this.validateLookupTime(time);
        this.lookupTime = time;
    }
}
