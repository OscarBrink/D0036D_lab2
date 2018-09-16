package model;

import javafx.util.Pair;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Handler used for parsing .xml-file containing data about a location.
 *
 * @author  Oscar Brink
 *          2018-09-16
 */
public class PlacesHandler extends ApplicationDataHandler {
    
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
                this.incorrectDataError();
            }
            for (int i = 0; i < 3; i++) {
                placeData[i] = new Pair<>(attributes.getQName(i), attributes.getValue(i));
            }
            this.endParse();

        } else {
            placeNameFound = this.placeName.equals(attributes.getValue("name"));
        }
    }

    @Override
    void endParse() throws XMLDataRetrievedException {
        this.resetState();
        throw new XMLDataRetrievedException(this.placeData);
    }

    void incorrectDataError() throws PlaceDataException {
        String message = "Recieved incorrect place-data. Probably" +
                " incorrectly formatted places.xml.";
        throw new PlaceDataException(message);
    }

    void resetState() {
        this.placeNameFound = false;
    }

    void setPlaceName(String placeName) {
        this.placeName = placeName;
    }
}
