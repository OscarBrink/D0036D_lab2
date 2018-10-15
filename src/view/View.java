package view;

import controller.Controller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


/**
 * Class handling the GUI of the application.
 *
 * @author  Oscar Brink
 *          2018-10-15
 */
public class View extends JFrame {
    private JButton cacheButton;
    private JLabel tempLabel, placeLabel;


    private Controller controller;
    private JButton requestButton;
    private TextField   placeNameField,
                        dateInputField,
                        timeInputField;
    private Box menu, cacheMenu;

    /**
     * Constructor. Starts the GUI.
     *
     * @param controller Controller that input from the GUI can be pushed to.
     */
    public View(Controller controller) {
        this.controller = controller;
        startGUI();
    }

    /*
     * Method starting up the GUI.
     */
    private void startGUI() {

        // Creating the GUI-components.
        addTemperatureLabel();
        addPlaceLabel();
        createMenu();
        createCacheMenu();

        this.pack();

        this.setTitle("Temperature Application");
        this.setSize(500, 600);

        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setVisible(true);
    }

    /*
     * Creates the main menu for the application, where the user can
     * input time data and request weather data.
     */
    private void createMenu() {
        menu = new Box(BoxLayout.X_AXIS);

        addPlaceNameField();
        addDateInputField();
        addTimeInputField();
        addRequestButton();

        this.add(menu, BorderLayout.SOUTH);
    }

    /*
     * Creates the cache-menu where the user can set a new lease-time
     * for caching.
     */
    private void createCacheMenu() {
        cacheMenu = new Box(BoxLayout.Y_AXIS);
        addCacheButton();

        this.add(cacheMenu, BorderLayout.EAST);
    }

    /*
     * Method adds the temperature data text to the GUI.
     */
    private void addTemperatureLabel() {
        // Temperature displayed with degree-symbol (\u00b0).
        tempLabel = new JLabel("XX.X\u00b0C", JLabel.CENTER);
        tempLabel.setFont(new Font(
                tempLabel.getFont().getName(),
                tempLabel.getFont().getStyle(),
                40
        ));

        this.add(tempLabel, BorderLayout.CENTER);
    }

    /*
     * Method adds the Location and Time display which is updated when new
     * weather-data has been retrieved.
     */
    private void addPlaceLabel() {
        // Text can be displayed with linebreak if wrapped in html-tags.
        placeLabel = new JLabel("<html>Location:<br/>Time:</html>", JLabel.LEFT);
        placeLabel.setFont(new Font(
                placeLabel.getFont().getName(),
                placeLabel.getFont().getStyle(),
                20
        ));

        this.add(placeLabel, BorderLayout.NORTH);
    }

    /*
     * Method adds the place-name input box.
     */
    private void addPlaceNameField() {
        placeNameField = new TextField("Skelleftea");
        menu.add(placeNameField);
    }

    /*
     * Method adds the date input box.
     */
    private void addDateInputField() {
        dateInputField = new TextField("YYYY-MM-DD", 10);
        menu.add(dateInputField);
    }

    /*
     * Method adds the time input box.
     */
    private void addTimeInputField() {
        timeInputField = new TextField("HH", 2);
        menu.add(timeInputField);
    }

    /*
     * Method adds the button that is used to request data from the
     * application. Reads from the input boxes.
     */
    private void addRequestButton() {
        requestButton = new JButton("Get");

        this.requestButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    View.this.controller.request(
                            placeNameField.getText(),
                            dateInputField.getText(),
                            timeInputField.getText()
                    );
                    View.this.updatePlaceTime();
                } catch (IllegalArgumentException ex) {}
            }
        });

        menu.add(requestButton);
    }

    /*
     * Method adds the button to change the lease-time.
     */
    private void addCacheButton() {
        cacheButton = new JButton("Set caching time");

        this.cacheButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String  message = "Enter new cache-time (min):";
                boolean success = false;
                while(!success) {
                    try {
                        controller.setLeaseTime(JOptionPane.showInputDialog(
                                View.this,
                                message,
                                "Set cache-time",
                                JOptionPane.PLAIN_MESSAGE
                        ));
                        success = true;
                    } catch (NumberFormatException ex) {
                        message = "Please only enter a whole number!\n" +
                                "Enter new cache-time:";
                    } catch (IllegalArgumentException ex) {
                        message = ex.getMessage() + "\n" +
                                "Enter new cache-time: (min)";
                    }
                }

            }
        });

        cacheMenu.add(cacheButton);
    }

    /*
     * Updates the place-time data displayed in the application.
     */
    private void updatePlaceTime() {
        // Text can be displayed with linebreak if wrapped in html-tags.
        String newString = "<html>Location: " + this.placeNameField.getText() +
                "<br/>Time: " + this.dateInputField.getText() + ":" +
                this.timeInputField.getText() + "</html>";
        this.placeLabel.setText(newString);
    }

    /*
     * Updates the temperature displayed in the application.
     */
    public void updateTemperature(String value) {
        this.tempLabel.setText(value + "\u00b0C");
    }
}
