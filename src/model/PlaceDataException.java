package model;

import org.xml.sax.SAXException;

class PlaceDataException extends SAXException {
    PlaceDataException(String message) {
        super(message);
    }
}
