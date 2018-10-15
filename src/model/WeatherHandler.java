package model;

import org.xml.sax.Attributes;
import java.util.HashMap;

/**
 * Handler used for parsing of weather .xml-files retrieved through
 * https://api.met.no.
 *
 * @author  Oscar Brink
 *          2018-10-15
 */
public class WeatherHandler extends ApplicationDataHandler {

    private String  lookupTime,
                    lookupDate,
                    currentDateTime;

    private boolean dateTimeFound = false,
                    caching = false;

    private HashMap<String, String> weatherData;

    /**
     * This method is called when the parser opens the .xml-file.
     */
    @Override
    public void startDocument() {
        if (this.caching) {
            this.weatherData = new HashMap<String, String>();
        }
    }

    /**
     * This method is called when the parser has reached the end of the
     * .xml-file without having found the desired data. It then gives an error.
     *
     * Or if in caching mode, this signals that all data has been retrieved.
     *
     * @throws WeatherDataException
     *
     * @see org.xml.sax.helpers.DefaultHandler#endDocument
     */
    @Override
    public void endDocument() throws WeatherDataException, XMLDataRetrievedException {
        this.resetState();

        // If not caching data, eof is only reached through lookup failure.
        if (this.caching) {
            endParse();
        } else {
            String message;
            if (this.dateTimeFound) {
                message = "Could not lookup specified date-time D: "
                        + this.lookupDate + " T: " + this.lookupTime + ".";
            } else {
                message = "Could not lookup temperature at date-time D: "
                        + this.lookupDate + " T: " + this.lookupTime + ".";
            }

            throw new WeatherDataException(message);
        }
    }

    /**
     * Runs when parser hits a new element in the .xml-file. Checks for the
     * desired data, when found it throws an XMLDataRetrievedException which
     * gives the data to the parent calling the parser.
     *
     * @param uri
     * @param localName
     * @param qName
     * @param attributes
     * @throws XMLDataRetrievedException
     * @throws WeatherDataException
     *
     * @see org.xml.sax.helpers.DefaultHandler#startElement
     */
    @Override
    public void startElement(String uri, String localName,
                             String qName, Attributes attributes)
            throws XMLDataRetrievedException, WeatherDataException {

        if (this.caching) {
            lookForAllData(qName, attributes);
        } else {
            lookForSpecificData(qName, attributes);
        }
    }

    /*
     * This method is run to look for weather-data at a specific time.
     */
    private void lookForSpecificData(String qName, Attributes attributes)
            throws WeatherDataException, XMLDataRetrievedException {
        if ("time".equals(qName)) {
            String date = attributes.getValue("to").replaceAll("T.*Z", "");
            String time = attributes.getValue("to").replaceAll(".*T|Z", "");

            if (this.lookupDate.equals(date) && this.lookupTime.equals(time)) {
                this.dateTimeFound = true;
            }
        } else if (this.dateTimeFound && "temperature".equals(qName)) {
            String temperature = attributes.getValue("value");
            if (temperature == null) {
                this.incorrectDataError();
            }

            this.weatherData = new HashMap<String, String>(2, 1);
            this.weatherData.put("temperature", attributes.getValue("value"));
            this.endParse();
        }
    }

    /*
     * This method is run when caching, and looks for any temperature data
     * at any time.
     */
    private void lookForAllData(String qName, Attributes attributes)
            throws WeatherDataException {
        if ("time".equals(qName)) {
            this.currentDateTime = attributes.getValue("to");
        } else if ("temperature".equals(qName)) {
            String temperature = attributes.getValue("value");
            if (temperature == null) {
                this.incorrectDataError();
            }
            this.weatherData.put(this.currentDateTime, attributes.getValue("value"));
        }
    }

    @Override
    void incorrectDataError() throws WeatherDataException {
        String message = "Failed to retrieve weather data from file.";
        throw new WeatherDataException(message);
    }

    @Override
    public void endElement(String uri, String localName, String qName) {
        if (this.dateTimeFound && "time".equals(qName)) {
            this.dateTimeFound = false;
        }
    }

    @Override
    void endParse() throws XMLDataRetrievedException {
        this.resetState();
        HashMap<String, String> tempWeatherData = this.weatherData;
        this.weatherData = null;
        throw new XMLDataRetrievedException(tempWeatherData);
    }

    @Override
    void resetState() {
        this.dateTimeFound = false;
    }

    /*
     * Checks that the time recieved as input is valid.
     */
    private void validateLookupTime(String time) {
        if (!time.matches("[0-1]\\d|2[0-3]")) {
            String message = "Invalid time format for " + time + ". Please " +
                    "only specify an hour between 00-23.";
            throw new IllegalArgumentException(message);
        }
    }

    /*
     * Checks that the date recieved as input is valid.
     */
    private void validateLookupDate(String date) {
        if (!date.matches("\\d{4}-(0[1-9]|1[0-2])-([0-2]\\d|3[0-1])")) {
            String message = "Invalid date format for " + date + ". The only " +
                    "accepted format is YYYY-MM-DD.";
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * Sets the desired time and date for the weather data.
     *
     * @param date Date to lookup.
     * @param time Time to lookup.
     */
    void setDateTime(String date, String time) {
        this.setLookupDate(date);
        this.setLookupTime(time);
    }

    /*
     * Sets the time to lookup.
     */
    private void setLookupTime(String time) {
        if (time.length() == 1) {
            time = "0" + time;
        }
        this.validateLookupTime(time);
        this.lookupTime = time + ":00:00";
    }

    /*
     * Sets the date to lookup.
     */
    private void setLookupDate(String date) {
        this.validateLookupDate(date);
        this.lookupDate = date;
    }

    /**
     * Turns on caching mode which means that the data from the next xml-parsing
     * will be cached and not retrieved.
     */
    void setCachingMode() {
        this.caching = true;
    }

    /**
     * Turns off caching mode which means that the data from the next
     * xml-parsing will be retrieved and not cached.
     */
    void resetCachingMode() {
        this.caching = false;
    }
}
