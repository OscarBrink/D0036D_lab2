package view;

import controller.Controller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class View extends JFrame {
    private JButton cacheButton;
    private JLabel tempLabel;


    private Controller controller;
    private JButton requestButton;
    private TextField   placeNameField,
                        dateInputField,
                        timeInputField;
    private Box menu, cacheMenu;

    public View(Controller controller) {
        this.controller = controller;
        startGUI();
    }

    private void startGUI() {

        addTemperatureText();
        createMenu();
        createCacheMenu();

        this.pack();

        this.setTitle("Temperature Application");
        this.setSize(500, 600);
        //this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setVisible(true);
    }

    private void createMenu() {
        menu = new Box(BoxLayout.X_AXIS);

        addPlaceNameField();
        addDateInputField();
        addTimeInputField();
        addRequestButton();

        this.add(menu, BorderLayout.SOUTH);
    }

    private void createCacheMenu() {
        cacheMenu = new Box(BoxLayout.Y_AXIS);
        addCacheButton();

        this.add(cacheMenu, BorderLayout.EAST);
    }

    private void addTemperatureText() {
        tempLabel = new JLabel("XX.X\u00b0C", JLabel.CENTER);
        tempLabel.setFont(new Font(
                tempLabel.getFont().getName(),
                tempLabel.getFont().getStyle(),
                40
        ));

        this.add(tempLabel, BorderLayout.CENTER);
    }

    private void addPlaceNameField() {
        placeNameField = new TextField("Skelleftea");
        menu.add(placeNameField);
    }

    private void addDateInputField() {
        dateInputField = new TextField("YYYY-MM-DD", 10);
        menu.add(dateInputField);
    }

    private void addTimeInputField() {
        timeInputField = new TextField("HH", 2);
        menu.add(timeInputField);
    }

    private void addRequestButton() {
        requestButton = new JButton("Get");

        this.requestButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String date = dateInputField.getText();
                String time = timeInputField.getText();
                System.out.println(date + " " + time);
            }
        });

        menu.add(requestButton);
    }

    private void addCacheButton() {
        cacheButton = new JButton("Set caching time");

        this.cacheButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String  message = "Enter new cache-time (min):",
                        temp;
                Long cacheTime;
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



}