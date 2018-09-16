package model;

import javafx.util.Pair;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Handler used for parsing of weather .xml-files retrieved through
 * https://api.met.no.
 *
 * @author  Oscar Brink
 *          2018-09-16
 */
public class WeatherHandler extends ApplicationDataHandler {

    private String lookupTime, lookupDate;

    private boolean dateTimeFound = false;

    private Pair<String, String> weatherData;

    /**
     * This method is called when the parser has reached the end of the
     * .xml-file without having found the desired data. It then gives an error.
     *
     * @throws WeatherDataException
     *
     * @see org.xml.sax.helpers.DefaultHandler#endDocument
     */
    @Override
    public void endDocument() throws WeatherDataException {
        this.resetState();

        String message;
        if (this.dateTimeFound) {
            message = "Could not lookup specified date-time D: "
                    + lookupDate + " T: " + lookupTime + ".";
        } else {
            message = "Could not lookup temperature at date-time D: "
                    + lookupDate + " T: " + lookupTime + ".";
        }

        throw new WeatherDataException(message);
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

        if ("time".equals(qName)) {
            String date = attributes.getValue("to").replaceAll("T.*Z", "");
            String time = attributes.getValue("to").replaceAll(".*T|Z", "");

            if (lookupDate.equals(date) && lookupTime.equals(time)) {
                this.dateTimeFound = true;
            }
        } else if (dateTimeFound && "temperature".equals(qName)) {
            String temperature = attributes.getValue("value");
            if (temperature == null) {
                this.incorrectDataError();
            }
            weatherData = new Pair<>("temperature", attributes.getValue("value"));
            this.endParse();
        }
    }

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

    void endParse() throws XMLDataRetrievedException {
        this.resetState();
        throw new XMLDataRetrievedException(this.weatherData);
    }

    void resetState() {
        this.dateTimeFound = false;
    }

    private void validateLookupTime(String time) {
        if (!time.matches("[0-1]\\d|2[0-3]")) {
            String message = "Invalid time format for " + time + ". Please " +
                    "only specify an hour between 00-23.";
            throw new IllegalArgumentException(message);
        }
    }

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
        this.setlookupTime(time);
    }

    private void setlookupTime(String time) {
        if (time.length() == 1) {
            time = "0" + time;
        }
        this.validateLookupTime(time);
        this.lookupTime = time + ":00:00";
    }

    private void setLookupDate(String date) {
        this.validateLookupDate(date);
        this.lookupDate = date;
    }
}
