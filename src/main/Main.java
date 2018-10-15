package main;

import model.Model;
import view.View;
import controller.Controller;

import org.xml.sax.SAXException;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Main class of the application. Sets up the MVC-structure and runs the program.
 */
public class Main {

    private static Model model;
    private static Controller controller;
    private static View view;

    /**
     * Static standard java method running on startup.
     */
    public static void main(String[] args) {
        try {
            model = new Model();
        } catch (ParserConfigurationException | SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Could not create application directories");
            System.exit();
        }

        controller = new Controller(model);

        view = new View(controller);
        model.setView(view);
    }
}

