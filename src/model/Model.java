package model;

import javax.xml.parsers.*;
import org.xml.sax.*;
import java.io.File;

public class Model {
    
    static SAXParserFactory factory = SAXParserFactory.newInstance();
    
    public SAXParser saxParser;
    public Handler dh;

    public Model() {
        try {
            this.saxParser = factory.newSAXParser();
            this.dh = new Handler();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void parse(File f) {
        try {
            this.saxParser.parse(f, this.dh);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}


class Main {
    public static void main(String[] args) {

        String fPath = "/nfs/students/oscbri-7/d0036d/lab2/testfiles/places.xml";
        
        System.out.println("fPath: " + fPath);
        Model model = new Model();
        
        File f = new File(fPath);
        
        model.parse(f);
    }
}
