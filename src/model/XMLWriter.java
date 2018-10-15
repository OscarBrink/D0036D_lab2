package model;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import java.io.File;

import java.util.HashMap;


/**
 * Class handles writing to XML-files for the application.
 * @author  Oscar Brink
 *          2018-10-15
 */
public class XMLWriter {

    private String cacheDirPath;

    private DocumentBuilderFactory xmlFactory;
    private DocumentBuilder xmlBuilder;

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

    /**
     * Writes data to the .xml cache-file.
     *
     * @param placeName Name of the place that weather-data is for. Also the
     *                  filename for the .xml-file where data is cached.
     * @param data  Weather-data to be written to cache-file.
     */
    public void cacheData(String placeName, HashMap<String, String> data) {

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        try {
            Transformer transformer = transformerFactory.newTransformer();

            DOMSource domSource = new DOMSource(this.setupCacheDocument(data));

            StreamResult streamResult = new StreamResult(
                    new File(this.cacheDirPath + placeName + ".xml")
            );

            // Write to .xml-file
            transformer.transform(domSource, streamResult);
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }

    /*
     * The method sets up the cache-file.
     * The structure is:
     * <cachedata>
     *     <time to="Dyyyy-mm-ddThh">
     *         <temperature value="x.xx"/>
     *     </time>
     * </cachedata>
     *
     * Which emulates the structure from the api.
     */
    private Document setupCacheDocument(HashMap<String, String> data) {
        Document xmlDocument = xmlBuilder.newDocument();

        Element root = xmlDocument.createElement("cachedata");
        xmlDocument.appendChild(root);

        for (HashMap.Entry<String, String> entry : data.entrySet()) {
            // Set up <time>-tag
            Element time = xmlDocument.createElement("time");
            root.appendChild(time);

            // Write date-time data to <time>-tag
            Attr timeAttr = xmlDocument.createAttribute("to");
            timeAttr.setValue(entry.getKey());
            time.setAttributeNode(timeAttr);

            // Set up <temperature>-tag
            Element temperature = xmlDocument.createElement("temperature");
            time.appendChild(temperature);

            // Write temperature data to <temperature>-tag
            Attr temperatureValue = xmlDocument.createAttribute("value");
            temperatureValue.setValue(entry.getValue());
            temperature.setAttributeNode(temperatureValue);
        }

        return xmlDocument;
    }

    /**
     * Stores the cache-leases for use on next program startup.
     *
     * @param data HashMap containing the lease-data.
     *             Key:     place-name
     *             Value:   lease-time
     */
    public void storeCacheLeases(HashMap<String, Long> data) {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        try {
            Transformer transformer = transformerFactory.newTransformer();

            DOMSource domSource = new DOMSource(this.setupLeaseDocument(data));

            StreamResult streamResult = new StreamResult(
                    new File(this.cacheDirPath + "cacheLeases.xml")
            );

            // Write to .xml-file
            transformer.transform(domSource, streamResult);
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }

    /*
     * The method sets up the cacheLease-file.
     * The structure is:
     * <cacheleases>
     *     <locality name="cccccc" lease="xxxxxxxxxx"/>
     * </cachedata>
     */
    private Document setupLeaseDocument(HashMap<String, Long> data) {
        Document xmlDocument = xmlBuilder.newDocument();

        Element root = xmlDocument.createElement("cacheleases");
        xmlDocument.appendChild(root);

        for (HashMap.Entry<String, Long> entry : data.entrySet()) {
            // Set up <placeName>-tag
            Element locality = xmlDocument.createElement("locality");
            root.appendChild(locality);

            // Write placeName data to <placeName>-tag
            Attr placeNameAttr = xmlDocument.createAttribute("name");
            placeNameAttr.setValue(entry.getKey());
            locality.setAttributeNode(placeNameAttr);

            // Write lease-data to <placeName>-tag
            Attr leaseDataAttr = xmlDocument.createAttribute("lease");
            leaseDataAttr.setValue(entry.getValue().toString());
            locality.setAttributeNode(leaseDataAttr);
        }

        return xmlDocument;
    }

}

