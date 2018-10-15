package model;

import org.xml.sax.SAXException;

import java.util.HashMap;

/**
 * This Exception can be thrown to terminate parsing when the desired
 * data from an .xml-file has been retrieved.
 *
 * @author  Oscar Brink
 *          2018-09-16
 */
class XMLDataRetrievedException extends SAXException {

    private HashMap<String, String> data;

    /**
     * Constructor.
     *
     * @param data HashMap with key-value data from .xml-files
     *             (attribute - value pair).
     */
    XMLDataRetrievedException(HashMap<String, String> data) {
        super();
        this.data = data;
    }

    /**
     * @return dataArray key-value pair array retrieved from .xml-file
     */
    HashMap<String, String> getData() {
        return this.data;
    }
}

