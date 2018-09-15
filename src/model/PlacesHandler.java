package model;

import javafx.util.Pair;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;


public class PlacesHandler extends DefaultHandler {
    
    private String placeName;
    private boolean placeNameFound;

    // place data-length always 3.g
    private Pair<String, String>[] placeData = new Pair[3];


    @Override
    public void endDocument() {
        System.out.println("eof");
    }
    
    @Override
    public void startElement(String uri, String localName,
                             String qName, Attributes attributes)
            throws XMLDataRetrievedException, PlaceDataFormatException {
        System.out.println(qName + " ");
        if (placeNameFound) {
            String infoStr = "";
            if (attributes.getLength() != 3) { // If place data longer than 3.
                String errorString = "Recieved incorrect place-data. Probably" +
                        " incorrectly formatted places.xml.";
                throw new PlaceDataFormatException(errorString);
            }
            for (int i = 0; i < 3; i++) {
                placeData[i] = new Pair<>(attributes.getQName(i), attributes.getValue(i));
                infoStr += attributes.getQName(i) + " " + attributes.getValue(i) + " ";
            }
            System.out.println(infoStr);
            this.resetState();
            throw new XMLDataRetrievedException();

        } else {
            placeNameFound = this.placeName.equals(attributes.getValue(0));
        }
    }

    private void resetState() {
        this.placeNameFound = false;
    }

    void setPlaceName(String parameterName) {
        this.placeName = parameterName;
    }

    /*
     * This Exception needed to terminate parsing when the desired placeData has been
     * retrieved.
     */
    private class XMLDataRetrievedException extends SAXException {
        XMLDataRetrievedException() {
            super();
            System.out.println("In Exc: ");
            for (int i = 0; i < PlacesHandler.this.placeData.length; i++) {
                System.out.println(PlacesHandler.this.placeData[i].getKey() +
                        " " + PlacesHandler.this.placeData[i].getValue()
                );
            }
        }
    }

    private class PlaceDataFormatException extends SAXException {
        PlaceDataFormatException(String message) {
            super(message);
        }
    }
}
