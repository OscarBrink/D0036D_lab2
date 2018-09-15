package model;

import org.xml.sax.SAXException;

public class PlaceDataException extends SAXException {
    PlaceDataException(String message) {
        super(message);
    }
}
