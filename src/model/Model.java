package model;

import javax.xml.parsers.*;

import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;

// UNIX : String fPath = System.getProperty("user.dir") + "/../testfiles/places.xml";
// WIN  : String fPath = System.getProperty("user.dir") + "\\testfiles\\places.xml";

public class Model {
    
    static SAXParserFactory parserFactory = SAXParserFactory.newInstance();

    public SAXParser saxParser;
    public PlacesHandler placesHandler;
    private File file;


    public Model() throws ParserConfigurationException, SAXException, IOException {
        String sep = File.separator;
        String fPath = System.getProperty("user.dir") + sep + "testfiles" + sep + "places.xml";
        this.file = new File(fPath);

        this.saxParser = parserFactory.newSAXParser();
        this.placesHandler = new PlacesHandler();
    }

    public void getWeatherData(String locationName) {
        this.placesHandler.setPlaceName(locationName);
        this.parse();
    }
    
    private void parse() {
        try {
            this.saxParser.parse(this.file, this.placesHandler);
        } catch (SAXException ignored) {
        } catch (IOException e) {
            e.printStackTrace();
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

        String locationName = "Kage";

        String infoStr = "Getting data for " + locationName;
        System.out.println(infoStr);


        model.getWeatherData(locationName);
    }
}
