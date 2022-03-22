/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utility;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 *
 * @author Asus
 */
public class Utility {

    public static JButton addButton(String button_text, JPanel buttonPanel) {
        JButton button;
        button = new JButton(button_text);
        JPanel thisButtonPanel = new JPanel();
        thisButtonPanel.setLayout(new FlowLayout());
        thisButtonPanel.add(button);
        buttonPanel.add(thisButtonPanel);
        return button;
    }

    public static void centerWindowOnScreen(JFrame win) {
        Dimension screenSize = (Toolkit.getDefaultToolkit()).getScreenSize();
        win.setLocation(
                ((screenSize.width) / 2) - ((win.getSize().width) / 2),
                ((screenSize.height) / 2) - ((win.getSize().height) / 2));
        win.show();
    }

    public static JPanel getButtonPanel() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(4, 1));
        Insets buttonMargin = new Insets(4, 4, 4, 4);
        return buttonPanel;
    }

    public static void showMessage(String heading, String message, int messageType) {
        JOptionPane.showMessageDialog(new JFrame(), message, heading, messageType);
    }

}
