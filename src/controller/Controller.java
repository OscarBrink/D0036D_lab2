package controller;

import model.Model;
import org.xml.sax.SAXException;

import java.io.IOException;

public class Controller {

    private Model model;

    public Controller(Model model) {
        this.model = model;
    }

    public void setLeaseTime(String cacheTime)
            throws IllegalArgumentException, NumberFormatException {
        if (cacheTime == null) {
            return;
        }
        this.model.setLeaseTime(Long.valueOf(cacheTime) * 60);
    }

    public void request(String placeName, String date, String time) {
        try {
            this.model.request(placeName, date, time);
        } catch (IOException | SAXException e) {
            e.printStackTrace();
        }
    }

}
