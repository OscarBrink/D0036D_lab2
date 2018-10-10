package model;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.util.HashMap;

/**
 * Class handles writing to XML-files for the application.
 * @author  Oscar Brink
 *          2018-10-10
 */
public class XMLWriter {

    String cacheDirPath;
    long leaseTime;

    DocumentBuilderFactory xmlFactory;
    DocumentBuilder xmlBuilder;

    /**
     * Constructor.
     *
     * @param cacheDirPath Path to directory where the program's cache-files are
     *                     stored.
     */
    public XMLWriter(String cacheDirPath) throws ParserConfigurationException {
        this.cacheDirPath = cacheDirPath;

        this.xmlFactory = DocumentBuilderFactory.newInstance();
        this.xmlBuilder = xmlFactory.newDocumentBuilder();
    }

    public void cacheData(String placeName, HashMap<String, String> data) {

    }

    public void setLeaseTime(long leaseTime) {
        this.leaseTime = leaseTime;
    }

}
