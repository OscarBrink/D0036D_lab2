package model;

import org.xml.sax.SAXException;

/**
 * This exception is linked to the class PlacesHandler which is a handler for
 * xml-parsing. The exception exists for certain exceptional conditions during
 * parsing of place-data.
 *
 * @author  Oscar Brink
 *          2018-09-16
 */
public class PlaceDataException extends SAXException {

    /**
     * Constructor.
     *
     * @param message The error message.
     */
    PlaceDataException(String message) {
        super(message);
    }
}
