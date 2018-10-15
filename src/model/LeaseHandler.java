package model;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import java.util.HashMap;

/**
 * Handler used for parsing .xml-file containing data about cache-leases used
 * in the weather application.
 *
 * @author  Oscar Brink
 *          2018-10-15
 */
public class LeaseHandler extends ApplicationDataHandler {

    private HashMap<String, String> leaseData;

    @Override
    public void startDocument() throws SAXException {
        this.leaseData = new HashMap<String, String>();
    }

    @Override
    public void startElement(String uri, String localName,
                             String qName, Attributes attributes) {
        if ("locality".equals(qName)) {
            this.leaseData.put(
                    attributes.getValue("name"),
                    attributes.getValue("lease"));
        }
    }

    @Override
    public void endDocument() throws XMLDataRetrievedException {
        this.endParse();
    }

    @Override
    void endParse() throws XMLDataRetrievedException {
        HashMap<String, String> tempLeaseData = this.leaseData;
        this.leaseData = null;
        throw new XMLDataRetrievedException(tempLeaseData);
    }
}
