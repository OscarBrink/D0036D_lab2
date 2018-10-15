package model.xmlFileIO.handlers;

import org.xml.sax.SAXException;

/**
 * General exception for a handler for .xml-parsing. Is meant to be used when
 * something is when something is wrong with
 *
 * @author  Oscar Brink
 *          2018-09-16
 */
public abstract class ApplicationDataException extends SAXException {

    /**
     * Constructor.
     *
     * @param message The error message.
     */
    ApplicationDataException(String message) {
        super(message);
    }
}
