package view;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class View extends Frame implements ActionListener {
    private Panel panel;
    private Button button;

    public View() {
        setLayout(new FlowLayout());
        this.panel = new Panel();
        button = new Button("press");
        button.addActionListener(this);
        panel.add(button);
        panel.add(new TextField("hello"));

        setSize(250, 100);

        this.add(this.panel);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println("Action");
    }
}
