package model;

import javax.xml.parsers.*;

import javafx.util.Pair;
import jdk.nashorn.internal.runtime.regexp.joni.exception.ValueException;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;

// UNIX : String fPath = System.getProperty("user.dir") + "/../testfiles/places.xml";
// WIN  : String fPath = System.getProperty("user.dir") + "\\testfiles\\places.xml";

public class Model {
    
    static SAXParserFactory parserFactory = SAXParserFactory.newInstance();

    public SAXParser saxParser;
    public PlacesHandler placesHandler;
    private File placesFile;


    public Model() throws ParserConfigurationException, SAXException, IOException {
        String sep = File.separator;
        String fPath = System.getProperty("user.dir") + sep + "testfiles" + sep + "places.xml";
        this.placesFile = new File(fPath);

        this.saxParser = parserFactory.newSAXParser();
        this.placesHandler = new PlacesHandler();
    }

    public void getWeatherData(String placeName) {
        this.getPlaceData(placeName);
    }

    /*
     * This structure might appear weird, since data is retrieved through the
     * catch clause. This is to avoid
     */
    private Pair[] getPlaceData(String placeName) {
        this.placesHandler.setPlaceName(placeName);
        try {
            this.saxParser.parse(this.placesFile, this.placesHandler);
        } catch (XMLDataRetrievedException dataRetriever) {
            return dataRetriever.getData();
        } catch (SAXException | IOException e) {
            // TODO auto exception
            e.printStackTrace();
        }
        throw new ;
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
