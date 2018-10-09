package main;

import model.Model;
import view.View;
import controller.Controller;

import org.xml.sax.SAXException;
import javax.xml.parsers.ParserConfigurationException;

public class Main {

    private static Model model;
    private static Controller controller;
    private static View view;

    public static void main(String[] args) {
        //System.out.println("fPath: " + fPath);
        try {
            model = new Model();
        } catch (ParserConfigurationException | SAXException e) {
            e.printStackTrace();
        }

        controller = new Controller(model);

        //String placeName = "Skelleftea";

        //String infoStr = "Getting data for " + placeName;
        //System.out.println(infoStr);

        view = new View(controller);
        model.setView(view);

//        try {
//            System.out.println("1: ");
//            model.getWeatherData(placeName);
//            System.out.println("2: ");
//            model.getWeatherData(placeName);
//        } catch (SAXException | IOException e) {
//            e.printStackTrace();
//        }
    }
}

