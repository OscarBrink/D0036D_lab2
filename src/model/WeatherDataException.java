package model;

import org.xml.sax.SAXException;

public class WeatherDataException extends SAXException {
    WeatherDataException(String message) {
        super(message);
    }
}
