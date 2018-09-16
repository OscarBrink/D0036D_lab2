package model;

import org.xml.sax.helpers.DefaultHandler;

/**
 * Abstract handler used for parsing .xml-file.
 *
 * @author  Oscar Brink
 *          2018-09-16
 */
public abstract class ApplicationDataHandler extends DefaultHandler {

    /**
     * Resets state variables (for example booleans used during search for data
     * in the .xml-file).
     */
    void resetState() {}

    /**
     * Method used to wrap exception with message. Called when correct element
     * has been found in .xml-file but data is wrong somehow.
     *
     * @throws ApplicationDataException
     */
    void incorrectDataError() throws ApplicationDataException {}

    /**
     * Called when the desired data has been retrieved. This method then throws
     * an XMLDataRetrievedException containing key-value pair data which can be
     * caught in the parent to the parser.
     *
     * @throws XMLDataRetrievedException
     */
    void endParse() throws XMLDataRetrievedException {}
}
