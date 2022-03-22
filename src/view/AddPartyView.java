package view;

/* AddPartyView.java
 *
 *  Version:
 * 		 $Id$
 * 
 *  Revisions:
 * 		$Log: AddPartyView.java,v $
 * 		Revision 1.7  2003/02/20 02:05:53  ???
 * 		Fixed addPatron so that duplicates won't be created.
 * 		
 * 		Revision 1.6  2003/02/09 20:52:46  ???
 * 		Added comments.
 * 		
 * 		Revision 1.5  2003/02/02 17:42:09  ???
 * 		Made updates to migrate to observer model.
 * 		
 * 		Revision 1.4  2003/02/02 16:29:52  ???
 * 		Added ControlDeskEvent and ControlDeskObserver. Updated Queue to allow access to Vector so that contents could be viewed without destroying. Implemented observer model for most of ControlDesk.
 * 		
 * 
 */
/**
 * Class for GUI components need to add a party
 *
 */
import model.BowlerFile;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import pojo.Bowler;
import utility.Utility;

/**
 * Constructor for GUI used to Add Parties to the waiting party queue.
 *
 */
public class AddPartyView implements ActionListener, ListSelectionListener {

    private int maxSize;

    private JFrame win;
    private JButton addPatron;
    private JButton newPatron;
    private JButton remPatron;
    private JButton finished;
    private JList<String> partyList;
    private JList<String> allBowlers;
    private Vector<String> party;
    private Vector<String> bowlerdb;

    private ControlDeskView controlDesk;

    private String selectedNick;
    private String selectedMember;
    Logger logger
            = Logger.getLogger(
                    AddPartyView.class.getName());

    public AddPartyView(ControlDeskView controlDesk, int max) {

        this.controlDesk = controlDesk;
        maxSize = max;
        win = new JFrame("Add Party");
        win.getContentPane().setLayout(new BorderLayout());
        ((JPanel) win.getContentPane()).setOpaque(false);

        JPanel colPanel = new JPanel();
        colPanel.setLayout(new GridLayout(1, 3));

        // Party Panel
        JPanel partyPanel = new JPanel();
        partyPanel.setLayout(new FlowLayout());
        partyPanel.setBorder(new TitledBorder("Your Party"));

        party = new Vector<>();
        Vector<String> empty = new Vector<>();
        empty.add("(Empty)");

        partyList = new JList<>(empty);
        partyList.setFixedCellWidth(120);
        partyList.setVisibleRowCount(6);
        partyList.addListSelectionListener(this);
        JScrollPane partyPane = new JScrollPane(partyList);

        partyPanel.add(partyPane);

        // Bowler Database
        JPanel bowlerPanel = new JPanel();
        bowlerPanel.setLayout(new FlowLayout());
        bowlerPanel.setBorder(new TitledBorder("Bowler Database"));

        try {
            bowlerdb = new Vector<>(BowlerFile.getBowlers());
        } catch (Exception e) {
            logger.log(Level.SEVERE, "File open error while trying to read from it");
            bowlerdb = new Vector<>();
        }
        allBowlers = new JList<>(bowlerdb);
        allBowlers.setVisibleRowCount(8);
        allBowlers.setFixedCellWidth(120);
        JScrollPane bowlerPane = new JScrollPane(allBowlers);
        bowlerPane.setVerticalScrollBarPolicy(
                javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        allBowlers.addListSelectionListener(this);
        bowlerPanel.add(bowlerPane);

        JPanel buttonPanel = Utility.getButtonPanel();

        addPatron = Utility.addButton("Add to Party", buttonPanel);
        remPatron = Utility.addButton("Remove Member", buttonPanel);
        newPatron = Utility.addButton("New Patron", buttonPanel);
        finished = Utility.addButton("Finished", buttonPanel);

        addPatron.addActionListener(this);
        remPatron.addActionListener(this);
        newPatron.addActionListener(this);
        finished.addActionListener(this);

        // Clean up main panel
        colPanel.add(partyPanel);
        colPanel.add(bowlerPanel);
        colPanel.add(buttonPanel);
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
        if (e.getSource().equals(addPatron)) {
            if (selectedNick == null) {
                Utility.showMessage("Error", "Please select a player to add to the party", JOptionPane.ERROR_MESSAGE);
            } else if (party.size() >= maxSize) {
                Utility.showMessage("Error", "Number of players in a party cannot be more than "+maxSize, JOptionPane.ERROR_MESSAGE);
            } else {
                if (party.contains(selectedNick)) {
                    logger.log(Level.INFO, "Member already in Party");
                    Utility.showMessage("Error", selectedNick + " is already in the party", JOptionPane.ERROR_MESSAGE);
                } else {
                    party.add(selectedNick);
                    partyList.setListData(party);
                }

            }
        }
        if (e.getSource().equals(remPatron)) {
            if (selectedMember == null) {
                if (party.size() == 0) {
                    Utility.showMessage("Error", "Party list is empty!!!!", JOptionPane.ERROR_MESSAGE);
                } else {
                    Utility.showMessage("Error", "Please select a player to remove from party", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                party.removeElement(selectedMember);
                partyList.setListData(party);
            }

        }
        System.out.println(e.getSource());
        if (e.getSource().equals(newPatron)) {
            new NewPatronView(this);
        }
        if (e.getSource().equals(finished) && party != null && !party.isEmpty()) {

            controlDesk.updateAddParty(this);

            win.setVisible(false);
        }

    }

    /**
     * Handler for List actions
     *
     * @param e the ListActionEvent that triggered the handler
     */
    public void valueChanged(ListSelectionEvent e) {
        if (e.getSource().equals(allBowlers)) {
            selectedNick
                    = ((String) ((JList) e.getSource()).getSelectedValue());
        }
        if (e.getSource().equals(partyList)) {
            selectedMember
                    = ((String) ((JList) e.getSource()).getSelectedValue());
        }
    }

    /**
     * Accessor for Party
     */
    /**
     * Called by NewPatronView to notify AddPartyView to update
     *
     * @param newPatron the NewPatronView that called this method
     */
    public boolean updateNewPatron(NewPatronView newPatron) {
        try {
            Bowler checkBowler = BowlerFile.getBowlerInfo(newPatron.getNick());
            if (checkBowler == null) {
                BowlerFile.putBowlerInfo(
                        newPatron.getNick(),
                        newPatron.getFull(),
                        newPatron.getEmail());
                bowlerdb = new Vector<>(BowlerFile.getBowlers());
                allBowlers.setListData(bowlerdb);
                party.add(newPatron.getNick());
                partyList.setListData(party);
                return true;
            } else {
                logger.log(Level.WARNING, "A Bowler with that name already exists.");
                Utility.showMessage("Error", newPatron.getNick()+" is already present, try with some other name",JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } catch (Exception e2) {
            logger.log(Level.INFO, "File I/O Error");
            return false;
        }
    }

    /**
     * Accessor for Party
     */
    public Vector<String> getParty() {
        return party;
    }

}
