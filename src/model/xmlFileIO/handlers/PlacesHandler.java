package model.xmlFileIO.handlers;

import model.xmlFileIO.XMLDataRetrievedException;
import org.xml.sax.Attributes;

import java.util.HashMap;

/**
 * Handler used for parsing .xml-file containing data about a location.
 *
 * @author  Oscar Brink
 *          2018-10-15
 */
public class PlacesHandler extends ApplicationDataHandler {
    
    private String placeName;
    private boolean placeNameFound;

    private HashMap<String, String> placeData;


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

            // place data-length always 3
            this.placeData = new HashMap<>(4, 1);

            for (int i = 0; i < 3; i++) {
                placeData.put(attributes.getQName(i), attributes.getValue(i));
            }
            this.checkPlaceData();
            this.endParse();

        } else {
            placeNameFound = this.placeName.equals(attributes.getValue("name"));
        }
    }

    @Override
    void endParse() throws XMLDataRetrievedException {
        this.resetState();
        HashMap<String, String> tempPlaceData = this.placeData;
        this.placeData = null;
        throw new XMLDataRetrievedException(tempPlaceData);
    }

    @Override
    void incorrectDataError() throws PlaceDataException {
        String message = "Recieved incorrect place-data. Probably" +
                " incorrectly formatted places.xml.";
        throw new PlaceDataException(message);
    }

    @Override
    void resetState() {
        this.placeNameFound = false;
    }

    /*
     * Checks that the place-data that has been retrieved is correctly
     * formatted.
     */
    private void checkPlaceData()
            throws PlaceDataException {
        for (HashMap.Entry<String, String> entry : this.placeData.entrySet()) {
            if (entry.getKey().equals("altitude")
                    || entry.getKey().equals("latitude")
                    || entry.getKey().equals("longitude")) {
                try {
                    Float.valueOf(entry.getValue());
                } catch (NumberFormatException e) {
                    String message = "Value of " + entry.getKey() + " is NaN.";
                    throw new PlaceDataException(message);
                }
            } else {
                String message = "Unknown parameter name \"" +
                        entry.getKey() + "\".";
                throw new PlaceDataException(message);
            }
        }
    }

    /**
     * Sets the place name to lookup in the places-file.
     *
     * @param placeName Place to lookup.
     */
    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }
}
