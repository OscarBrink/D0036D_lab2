package model;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
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

    public void cacheData(String placeName, HashMap<String, String> data, long cacheLease) {
        Document xmlDocument = xmlBuilder.newDocument();

        Element root = xmlDocument.createElement("cachedata");

        for (HashMap.Entry<String, String> entry : data.entrySet()) {
            // Set up <time>-tag
            Element time = xmlDocument.createElement("time");
            root.appendChild(time);

            // Write date-time info to <time>-tag 
            Attr timeAttr = xmlDocument.createAttribute("to");
            timeAttr.setValue(entry.getKey());
            time.setAttributeNode(timeAttr);

            // Set up <temperature>-tag
            Element temperature = xmlDocument.createElement("temperature");
            time.appendChild(temperature);

            Attr temperatureValue = xmlDocument.createAttribute("value");
            temperatureValue.setValue(entry.getValue());
            temperature.setAttributeNode(temperatureValue);
        }
    }

    public void setLeaseTime(long leaseTime) {
        this.leaseTime = leaseTime;
    }

    public void main(String[] args) {
        // TODO testing for xml-writer
        XMLWriter xmlWriter = new XMLWriter("Hello");
    }

}

