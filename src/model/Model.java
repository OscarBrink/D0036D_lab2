package model;

import javax.xml.parsers.*;
import org.xml.sax.*;
import java.io.File;

public class Model {
    
    static SAXParserFactory factory = SAXParserFactory.newInstance();
    
    public SAXParser saxParser;
    public Handler dh;
    private File file;       

    public Model() {
        try {
            String fPath = System.getProperty("user.dir") + "/../testfiles/places.xml";
            this.file = new File(fPath);
            this.saxParser = factory.newSAXParser();
            this.dh = new Handler();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getWeatherData(String locationName) {
        this.dh.setParameter(locationName);
        this.parse();
    }
    
    private void parse() {
        try {
            this.saxParser.parse(this.file, this.dh);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}


class Main {
    public static void main(String[] args) {
        //System.out.println("fPath: " + fPath);
        Model model = new Model();
        
        String locationName = "Skelleftea";

        model.getWeatherData(locationName);
    }
}

