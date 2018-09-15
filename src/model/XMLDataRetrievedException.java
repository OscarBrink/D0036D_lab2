package model;

import javafx.util.Pair;

/**
 * This Exception can be thrown to terminate parsing when the desired
 * data from an .xml-file has been retrieved.
 *
 * @author  Oscar Brink
 *          2018-09-15
 */
class XMLDataRetrievedException extends RuntimeException {

    private Pair<String, String>[] data;

    XMLDataRetrievedException(Pair<String, String>[] data) {
        super();
        this.data = data;

        // Testing
        System.out.println("In Exc: ");
        for (Pair<String, String> p : data) {
            System.out.println(p.getKey() + " " + p.getValue());
        }
    }

    Pair<String, String>[] getData() {
        return data;
    }
}
