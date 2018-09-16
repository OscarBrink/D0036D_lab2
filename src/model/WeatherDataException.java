package model;

import org.xml.sax.SAXException;

/**
 * This exception is linked to the class WeatherHandler which is a handler for
 * xml-parsing. The exception exists for certain exceptional conditions during
 * parsing of weather-data.
 *
 * @author  Oscar Brink
 *          2018-09-16
 */
public class WeatherDataException extends ApplicationDataException {

    /**
     * Constructor.
     *
     * @param message The error message.
     */
    WeatherDataException(String message) {
        super(message);
    }
}
