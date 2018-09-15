package model;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import javafx.util.Pair;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;

// UNIX : String fPath = System.getProperty("user.dir") + "/../testfiles/places.xml";
// WIN  : String fPath = System.getProperty("user.dir") + "\\testfiles\\places.xml";

public class Model {
    
    static SAXParserFactory parserFactory;

    public SAXParser saxParser;
    WeatherHandler weatherHandler;
    public PlacesHandler placesHandler;
    private File placesFile;


    public Model() throws ParserConfigurationException, SAXException, IOException {
        String sep = File.separator;
        String fPath = System.getProperty("user.dir") + sep + "testfiles" + sep + "places.xml";
        this.placesFile = new File(fPath);

        parserFactory = SAXParserFactory.newInstance();

        this.saxParser = parserFactory.newSAXParser();
        this.placesHandler = new PlacesHandler();
        this.weatherHandler = new WeatherHandler();
    }

    public void getWeatherData(String placeName) {
        try {
            this.getPlaceData(placeName);
        } catch (SAXException e) {
            e.printStackTrace();
        }
    }

    /*
     * This structure might appear weird, since data is retrieved through the
     * catch clause. This is to avoid
     */
    private Pair<String, String>[] getPlaceData(String placeName) throws SAXException {
        this.placesHandler.setPlaceName(placeName);
        try {
            this.saxParser.parse(this.placesFile, this.placesHandler);
        } catch (XMLDataRetrievedException dataRetriever) {
            Pair<String, String>[] data = dataRetriever.getData();
            this.checkPlaceData(data);
            return data;
        } catch (IOException e) {
            // TODO auto exception
            e.printStackTrace();
        }
        throw new SAXException();
    }

    private void checkPlaceData(Pair<String, String>[] data)
            throws PlaceDataException {
        for (Pair<String, String> p: data) {
            if (p.getKey().equals("altitude")
                    || p.getKey().equals("latitude")
                    || p.getKey().equals("longitude")) {
                try {
                    Float.valueOf(p.getValue());
                } catch (NumberFormatException e) {
                    String message = "Value of " + p.getKey() + " is NaN.";
                    throw new PlaceDataException(message);
                }
            } else {
                String message = "Unknown parameter name \"" +
                        p.getKey() + "\".";
                throw new PlaceDataException(message);
            }
        }
    }

}


class Main {
    public static void main(String[] args) {
        //System.out.println("fPath: " + fPath);
        Model model = null;
        try {
            model = new Model();
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }

        String placeName = "Skelleftea";

        String infoStr = "Getting data for " + placeName;
        System.out.println(infoStr);


        model.getWeatherData(placeName);
    }
}
