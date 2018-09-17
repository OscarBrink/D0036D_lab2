package controller;

import model.Model;

public class Controller {

    private Model model;

    public Controller(Model model) {
        this.model = model;
    }

    public void setLeaseTime(String cacheTime)
            throws IllegalArgumentException, NumberFormatException {
        this.model.setLeaseTime(Long.valueOf(cacheTime) * 60);
    }

    public String request(String placeName) {
        return "6.4";
    }

}
