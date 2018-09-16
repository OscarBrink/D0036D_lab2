package model;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

// Testing
// UNIX : String fPath = System.getProperty("user.dir") + "/../testfiles/places.xml";
// WIN  : String fPath = System.getProperty("user.dir") + "\\testfiles\\places.xml";

public class Model {
    
    static SAXParserFactory parserFactory;

    private SAXParser saxParser;
    private WeatherHandler weatherHandler;
    private PlacesHandler placesHandler;
    private File placesFile, weatherFile;

    private HashMap<String, Long> cacheLeases;

    private HTTPRequester httpRequester;


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
        this.httpRequester = new HTTPRequester(
                "https://api.met.no/weatherapi/locationforecast/1.9/?lat=latitude&lon=longitude&msl=altitude",
                System.getProperty("user.dir") + sep + "testfiles" + sep + "test.xml"
        );
    }

    private boolean checkCacheLease(String placeName) {
        for (HashMap.Entry<String, Long> entry : this.cacheLeases.entrySet()) {
            if (entry.getKey().equals(placeName)) {
                // Return false if lease-time has passed.
                return ((entry.getValue() - (System.currentTimeMillis() / 1000)) <= 0);
            }
        }
        return false;
    }

    private void setCacheLease(String placeName, Long leaseSeconds) {
        this.cacheLeases.put(
                placeName,
                (System.currentTimeMillis() / 1000) + leaseSeconds
        );
    }

    public HashMap<String, String> getWeatherData(String placeName)
            throws SAXException, IOException {
        //this.httpRequester.request(this.getPlaceData(placeName));
        this.weatherHandler.setDateTime("2018-09-16", "22");
        this.weatherHandler.resetCachingMode();

        if (checkCacheLease(placeName)) {
            try {
                this.saxParser.parse(
                        this.httpRequester.request(this.getPlaceData(placeName)),
                        this.weatherHandler
                );
            } catch (XMLDataRetrievedException dataRetriever) {
                HashMap<String, String> data = dataRetriever.getData();
                return data;
            }
        } else {
            // TODO Parse data from cache.
        }
        throw new SAXException();
    }


    private HashMap<String, String> getPlaceData(String placeName)
            throws SAXException, IOException {
        this.placesHandler.setPlaceName(placeName);

        /*
         * This structure might appear weird, since data is retrieved through
         * the catch clause. This is to avoid parsing the entire XML-file after
         * the desired data has been retrieved. See PlacesHandler.java for impl.
         */
        try {
            this.saxParser.parse(this.placesFile, this.placesHandler);
        } catch (XMLDataRetrievedException dataRetriever) {
            return dataRetriever.getData();
        }
        throw new SAXException();
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
        } catch (SAXException | IOException e) {
            e.printStackTrace();
        }
    }
}
