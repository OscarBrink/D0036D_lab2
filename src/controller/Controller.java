package controller;

import model.Model;

import org.xml.sax.SAXException;
import java.io.IOException;

/**
 * Controller class taking input from the GUI in the view, and pushes it
 * through to the model.
 *
 * @author  Oscar Brink
 *          2018-10-15
 */
public class Controller {

    private Model model;

    /**
     * Constructor.
     *
     * @param model Model containing the data-model for the weather
     *              application.
     */
    public Controller(Model model) {
        this.model = model;
    }

    /**
     * Sets the lease-time, which is the time that retrieved weather-data
     * should be cached.
     *
     * @param leaseTime String containing the lease-time
     * @throws IllegalArgumentException
     * @throws NumberFormatException
     */
    public void setLeaseTime(String leaseTime)
            throws IllegalArgumentException, NumberFormatException {
        if (leaseTime == null) {
            return;
        }
        this.model.setLeaseTime(Long.valueOf(leaseTime) * 60);
    }

    /**
     * Takes a request for weather-data and forwards the request to the
     * data-model
     *
     * @param placeName Name of the place that data should be retrieved for.
     * @param date Specified date that data should be retrieved for.
     * @param time Specified hour that data should be retrieved for.
     */
    public void request(String placeName, String date, String time) {
        try {
            this.model.request(placeName, date, time);
        } catch (IOException | SAXException e) {
            e.printStackTrace();
        }
    }

}
