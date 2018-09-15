package model;

import javafx.util.Pair;
import org.xml.sax.SAXException;

/**
 * This Exception can be thrown to terminate parsing when the desired
 * data from an .xml-file has been retrieved.
 *
 * @author  Oscar Brink
 *          2018-09-15
 */
class XMLDataRetrievedException extends RuntimeException {

    private Pair[] data;

    XMLDataRetrievedException(Pair[] data) {
        super();
        this.data = data;

        // Testing
        System.out.println("In Exc: ");
        for (Pair p : data) {
            System.out.println(p.getKey() +
                    " " + p.getValue()
            );
        }
    }

    Pair[] getData() {
        return data;
    }
}
