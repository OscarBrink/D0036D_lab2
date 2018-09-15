package model;

import javafx.util.Pair;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;


public class PlacesHandler extends DefaultHandler {
    
    private String placeName;
    private boolean placeNameFound;

    // place data-length always 3.
    private Pair<String, String>[] placeData = new Pair[3];


    @Override
    public void endDocument() throws PlaceDataException {
        String message = "Could not find data for specified locality \"" +
                placeName + "\".";
        throw new PlaceDataException(message);
    }
    
    @Override
    public void startElement(String uri, String localName,
                             String qName, Attributes attributes)
            throws XMLDataRetrievedException, PlaceDataException {
        if (placeNameFound) {
            if (attributes.getLength() != 3) { // If place data longer than 3.
                incorrectDataError();
            }
            for (int i = 0; i < 3; i++) {
                placeData[i] = new Pair<>(attributes.getQName(i), attributes.getValue(i));
            }
            this.resetState();
            throw new XMLDataRetrievedException(placeData);

        } else {
            placeNameFound = this.placeName.equals(attributes.getValue("name"));
        }
    }

    private void incorrectDataError() throws PlaceDataException {
        String message = "Recieved incorrect place-data. Probably" +
                " incorrectly formatted places.xml.";
        throw new PlaceDataException(message);
    }

    private void resetState() {
        this.placeNameFound = false;
    }

    void setPlaceName(String parameterName) {
        this.placeName = parameterName;
    }
}
