package model;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import javafx.util.Pair;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

// Testing
// UNIX : String fPath = System.getProperty("user.dir") + "/../testfiles/places.xml";
// WIN  : String fPath = System.getProperty("user.dir") + "\\testfiles\\places.xml";

public class Model {
    
    static SAXParserFactory parserFactory;

    public SAXParser saxParser;
    WeatherHandler weatherHandler;
    public PlacesHandler placesHandler;
    private File placesFile, weatherFile;


    public Model() throws ParserConfigurationException, SAXException, IOException {
        String sep = File.separator;
        String fPath = System.getProperty("user.dir") + sep + "testfiles" + sep + "places.xml";
        this.placesFile = new File(fPath);

        fPath = System.getProperty("user.dir") + sep + "testfiles" + sep + "skelleftea_2242_2018_09_15.xml";
        this.weatherFile = new File(fPath);


        parserFactory = SAXParserFactory.newInstance();

        this.saxParser = parserFactory.newSAXParser();
        this.placesHandler = new PlacesHandler();
        this.weatherHandler = new WeatherHandler();
    }

    public HashMap<String, String> getWeatherData(String placeName) throws SAXException {
        this.getPlaceData(placeName);
        this.weatherHandler.setDateTime("2018-09-17", "20");
        this.weatherHandler.resetCachingMode();

        try {
            this.saxParser.parse(this.weatherFile, this.weatherHandler);
        } catch (XMLDataRetrievedException dataRetriever) {
            HashMap<String, String> data = dataRetriever.getData();
            return data;
        } catch (IOException e) {
            e.printStackTrace();
        }
        throw new SAXException();
    }

    /*
     * This structure might appear weird, since data is retrieved through the
     * catch clause. This is to avoid parsing the entire XML-file after the
     * desired data has been retrieved. See PlacesHandler.java for impl.
     */
    private HashMap<String, String> getPlaceData(String placeName) throws SAXException {
        this.placesHandler.setPlaceName(placeName);
        try {
            this.saxParser.parse(this.placesFile, this.placesHandler);
        } catch (XMLDataRetrievedException dataRetriever) {
            HashMap<String, String> data = dataRetriever.getData();
            this.checkPlaceData(data);
            return data;
        } catch (IOException e) {
            // TODO auto exception
            e.printStackTrace();
        }
        throw new SAXException();
    }

    private void checkPlaceData(HashMap<String, String> data)
            throws PlaceDataException {
        for (HashMap.Entry<String, String> entry : data.entrySet()) {
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


        try {
            model.getWeatherData(placeName);
        } catch (SAXException e) {
            e.printStackTrace();
        }
    }
}
