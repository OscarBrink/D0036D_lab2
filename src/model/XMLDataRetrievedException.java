package model;

import javafx.util.Pair;

/**
 * This Exception can be thrown to terminate parsing when the desired
 * data from an .xml-file has been retrieved.
 *
 * @author  Oscar Brink
 *          2018-09-16
 */
class XMLDataRetrievedException extends RuntimeException {

    private Pair<String, String>[] dataArray;
    private Pair<String, String> data;

    /**
     * Constructor.
     *
     * @param dataArray Key-value pair-array from .xml-files
     *             (attribute - value pair).
     */
    XMLDataRetrievedException(Pair<String, String>[] dataArray) {
        super();
        this.dataArray = dataArray;

        // Testing
        System.out.println("In Exc: ");
        for (Pair<String, String> p : this.dataArray) {
            System.out.println(p.getKey() + " " + p.getValue());
        }
    }

    /**
     * Constructor.
     *
     * @param data Key-value pair from .xml-files (attribute - value pair).
     */
    XMLDataRetrievedException(Pair<String, String> data) {
        super();
        this.data = data;

        // Testing
        System.out.println("In Exc: ");
        System.out.println(this.data.getKey() + " " + this.data.getValue());
    }

    /**
     *
     * @return dataArray key-value pair array retrieved from .xml-file
     */
    Pair<String, String>[] getDataArray() {
        return this.dataArray;
    }

    /**
     *
     * @return data key-value pair array retrieved from .xml-file
     */
    Pair<String, String> getData() {
        return this.data;
    }
}
