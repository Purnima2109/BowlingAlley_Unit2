package view;

/* AddPartyView.java
 *
 *  Version
 *  $Id$
 * 
 *  Revisions:
 * 		$Log: NewPatronView.java,v $
 * 		Revision 1.3  2003/02/02 16:29:52  ???
 * 		Added ControlDeskEvent and ControlDeskObserver. Updated Queue to allow access to Vector so that contents could be viewed without destroying. Implemented observer model for most of ControlDesk.
 * 		
 * 
 */
/**
 * Class for GUI components need to add a patron
 *
 */
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import utility.Utility;

public class NewPatronView implements ActionListener {

    private JFrame win;
    private JButton abort;
    private JButton finished;
    private JLabel nickLabel;
    private JLabel fullLabel;
    private JLabel emailLabel;
    private JTextField nickField;
    private JTextField fullField;
    private JTextField emailField;
    private String nick;
    private String full;
    private String email;
    private static final String ADDPATRON = "Add Patron";

    private boolean done;

    private AddPartyView addParty;

    public NewPatronView(AddPartyView v) {

        addParty = v;
        done = false;

        win = new JFrame(ADDPATRON);
        win.getContentPane().setLayout(new BorderLayout());
        ((JPanel) win.getContentPane()).setOpaque(false);

        JPanel colPanel = new JPanel();
        colPanel.setLayout(new BorderLayout());

        // Patron Panel
        JPanel patronPanel = new JPanel();
        patronPanel.setLayout(new GridLayout(3, 1));
        patronPanel.setBorder(new TitledBorder("Your Info"));

        JPanel nickPanel = new JPanel();
        nickPanel.setLayout(new FlowLayout());
        nickLabel = new JLabel("Nick Name");
        nickField = new JTextField("", 15);
        nickPanel.add(nickLabel);
        nickPanel.add(nickField);

        JPanel fullPanel = new JPanel();
        fullPanel.setLayout(new FlowLayout());
        fullLabel = new JLabel("Full Name");
        fullField = new JTextField("", 15);
        fullPanel.add(fullLabel);
        fullPanel.add(fullField);

        JPanel emailPanel = new JPanel();
        emailPanel.setLayout(new FlowLayout());
        emailLabel = new JLabel("E-Mail");
        emailField = new JTextField("", 15);
        emailPanel.add(emailLabel);
        emailPanel.add(emailField);

        patronPanel.add(nickPanel);
        patronPanel.add(fullPanel);
        patronPanel.add(emailPanel);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(4, 1));

        Insets buttonMargin = new Insets(4, 4, 4, 4);

        finished = new JButton("Add Patron");
        JPanel finishedPanel = new JPanel();
        finishedPanel.setLayout(new FlowLayout());
        finished.addActionListener(this);
        finishedPanel.add(finished);

        abort = new JButton("Abort");
        JPanel abortPanel = new JPanel();
        abortPanel.setLayout(new FlowLayout());
        abort.addActionListener(this);
        abortPanel.add(abort);

        buttonPanel.add(abortPanel);
        buttonPanel.add(finishedPanel);
        // Clean up main panel
        colPanel.add(patronPanel, "Center");
        colPanel.add(buttonPanel, "East");

        win.getContentPane().add("Center", colPanel);

        win.pack();

        // Center Window on Screen
        Dimension screenSize = (Toolkit.getDefaultToolkit()).getScreenSize();
        win.setLocation(
                ((screenSize.width) / 2) - ((win.getSize().width) / 2),
                ((screenSize.height) / 2) - ((win.getSize().height) / 2));
        win.setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(abort)) {
            done = true;
            win.setVisible(false);
        }

        if (e.getSource().equals(finished)) {
            nick = nickField.getText();
            full = fullField.getText();
            email = emailField.getText();
            if (nick.isEmpty()) {
                Utility.showMessage("Error", "Nickname of the player cannot be Empty!!!", JOptionPane.ERROR_MESSAGE);
            } else if (full.isEmpty()) {
                Utility.showMessage("Error", "Full name of the player cannot be Empty!!!", JOptionPane.ERROR_MESSAGE);
            } else if (email.isEmpty()) {
                Utility.showMessage("Error", "Email of the player cannot be Empty!!!", JOptionPane.ERROR_MESSAGE);
            } else {
                boolean result = addParty.updateNewPatron(this);
                if (result) {
                    done = true;
                    win.setVisible(false);
                }
            }
        }

    }

    public boolean done() {
        return done;
    }

    public String getNick() {
        return nick;
    }

    public String getFull() {
        return full;
    }

    public String getEmail() {
        return email;
    }

}
