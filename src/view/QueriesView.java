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
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import model.BowlerFile;
import pojo.Bowler;
import static utility.Records.*;
import utility.Utility;

/**
 * Constructor for GUI used to Add Parties to the waiting party queue.
 *
 */
public class QueriesView implements ActionListener, ListSelectionListener {

    private int maxSize;

    private JFrame win;
    private JButton highestScore;
    private JButton lowestScore;
    private JButton topPlayer;
    private JButton viewRecords;
    private JButton leaderBoard;
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

    public QueriesView(ControlDeskView controlDesk, int max) {

        this.controlDesk = controlDesk;
        maxSize = max;
        win = new JFrame("View Records");
        win.getContentPane().setLayout(new BorderLayout());
        ((JPanel) win.getContentPane()).setOpaque(false);

        JPanel recordPanel = new JPanel();
        recordPanel.setLayout(new GridLayout(1, 2));

        // Bowler Database
        JPanel bowlerPanel = new JPanel();
        bowlerPanel.setLayout(new FlowLayout());
        bowlerPanel.setBorder(new TitledBorder("All Players:"));

        try {
            bowlerdb = new Vector<>(BowlerFile.getBowlers());
        } catch (Exception e) {
            logger.log(Level.SEVERE, "File open error while trying to read from it");
            bowlerdb = new Vector<>();
        }
        allBowlers = new JList<>(bowlerdb);
        allBowlers.setVisibleRowCount(8);
        allBowlers.setFixedCellWidth(200);
        JScrollPane bowlerPane = new JScrollPane(allBowlers);
        bowlerPane.setVerticalScrollBarPolicy(
                javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        allBowlers.addListSelectionListener(this);
        bowlerPanel.add(bowlerPane);

        viewRecords = Utility.addButton("View Records", bowlerPanel);
        viewRecords.addActionListener(this);

        JPanel buttonPanel = Utility.getButtonPanel();

        highestScore = Utility.addButton("Highest Score", buttonPanel);
        lowestScore = Utility.addButton("Lowest Score", buttonPanel);
        topPlayer = Utility.addButton("Top Player", buttonPanel);
        leaderBoard = Utility.addButton("LeaderBoard", buttonPanel);
        highestScore.addActionListener(this);
        lowestScore.addActionListener(this);
        topPlayer.addActionListener(this);
        leaderBoard.addActionListener(this);

        recordPanel.add(bowlerPanel);
        recordPanel.add(buttonPanel);
        win.getContentPane().add("Center", recordPanel);

        win.pack();

        // Center Window on Screen
        Dimension screenSize = (Toolkit.getDefaultToolkit()).getScreenSize();
        win.setLocation(
                ((screenSize.width) / 2) - ((win.getSize().width) / 2),
                ((screenSize.height) / 2) - ((win.getSize().height) / 2));
        win.setVisible(true);

    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(viewRecords)) {
            if (selectedNick == null) {
                Utility.showMessage("Error", "Please select a player to view its records", JOptionPane.ERROR_MESSAGE);
            } else {
                try {
                    String msg = getRecordForBowler(selectedNick);
                    Utility.showMessage(selectedNick + "'s Record:", msg, JOptionPane.INFORMATION_MESSAGE);
                } catch (IOException ex) {
                    Logger.getLogger(QueriesView.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } else if (e.getSource().equals(highestScore)) {
            try {
                String highestScore = getHighestScore();
                Utility.showMessage("Hishest Score", highestScore, JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException ex) {
                Logger.getLogger(QueriesView.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (e.getSource().equals(lowestScore)) {
            try {
                String lowestScore = getLowestScore();
                Utility.showMessage("Records", lowestScore, JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException ex) {
                Logger.getLogger(QueriesView.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (e.getSource().equals(topPlayer)) {

            try {
                String topPlayer = getTopPlayer();
                Utility.showMessage("Records", topPlayer, JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException ex) {
                Logger.getLogger(QueriesView.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (e.getSource().equals(leaderBoard)) {

            try {
                DefaultTableModel tableModel = new DefaultTableModel();
                JTable table = new JTable(tableModel);
                tableModel.addColumn("Name");
                tableModel.addColumn("Score");
                HashMap<String, Integer> leaderBoardData = leaderBoard();
                HashMap<String, Integer> leaders2 = new HashMap<>();
//                leaderBoardData.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())).forEachOrdered(x -> leaders2.put(x.getKey(), x.getValue()));

                for (Map.Entry<String, Integer> entry : leaderBoardData.entrySet()) {
                    tableModel.insertRow(0, new Object[]{entry.getKey(), entry.getValue()});
                }
                JTableHeader tableHeader = table.getTableHeader();
                tableHeader.setBackground(Color.black);
                tableHeader.setForeground(Color.white);
                Font headerFont = new Font("Verdana", Font.PLAIN, 25);
                tableHeader.setFont(headerFont);
                JFrame f = new JFrame();
                f.setSize(350, 350);
                f.add(new JScrollPane(table));
                f.pack();
                f.setLocationRelativeTo(null);
                f.setVisible(true);
            } catch (IOException ex) {
                Logger.getLogger(QueriesView.class.getName()).log(Level.SEVERE, null, ex);
            }

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
    public void updateNewPatron(NewPatronView newPatron) {
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
            } else {
                logger.log(Level.INFO, "A Bowler with that name already exists.");
            }
        } catch (Exception e2) {
            logger.log(Level.INFO, "File I/O Error");
        }
    }

    /**
     * Accessor for Party
     */
    public Vector<String> getParty() {
        return party;
    }

}
