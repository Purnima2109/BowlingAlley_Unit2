package view;

/* ControlDeskView.java
 *
 *  Version:
 *			$Id$
 * 
 *  Revisions:
 * 		$Log$
 * 
 */
/**
 * Class for representation of the control desk
 *
 */
import controller.ControlDesk;
import event.ControlDeskEvent;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

import observer.ControlDeskObserver;

public class ControlDeskView implements ActionListener, ControlDeskObserver {

    //private JButton addParty, finished, assign;
    private JFrame win;
    private JList partyList;

    /**
     * The maximum number of members in a party
     */
    private int maxMembers;

    private ControlDesk controlDesk;

    /**
     * Displays a GUI representation of the ControlDesk
     *
     */
    private CreateControlDeskGUI controlDeskGUI;

    public ControlDeskView(ControlDesk controlDesk, int maxMembers) {

        this.controlDesk = controlDesk;
        this.maxMembers = maxMembers;
        int numLanes = controlDesk.getNumLanes();
        controlDeskGUI = new CreateControlDeskGUI();

        win = new JFrame("Control Desk");
        win.getContentPane().setLayout(new BorderLayout());
        ((JPanel) win.getContentPane()).setOpaque(false);

        JPanel colPanel = new JPanel();
        colPanel.setLayout(new BorderLayout());
        controlDeskGUI.createControlDeskControlsPanel(this);
        JPanel controlsPanel = controlDeskGUI.getControlsPanel();

        controlDeskGUI.createControlDeskLaneStatusPanel(controlDesk);
        JPanel laneStatusPanel = controlDeskGUI.getControlDeskLaneStatusPanel();

        controlDeskGUI.createControlDeskPartyQueuePanel(controlDesk);
        JPanel partyList = controlDeskGUI.getPartyPanel();

        // Clean up main panel
        colPanel.add(controlsPanel, "East");
        colPanel.add(laneStatusPanel, "Center");
        colPanel.add(partyList, "West");

        win.getContentPane().add("Center", colPanel);

        win.pack();

        /* Close program when this window closes */
        win.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        // Center Window on Screen
        Dimension screenSize = (Toolkit.getDefaultToolkit()).getScreenSize();
        win.setLocation(
                ((screenSize.width) / 2) - ((win.getSize().width) / 2),
                ((screenSize.height) / 2) - ((win.getSize().height) / 2));
        win.show();

    }

    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Add Party")) {
            AddPartyView addPartyWin = new AddPartyView(this, maxMembers);
        } else if (e.getActionCommand().equals("Records")) {
            QueriesView queriesWin = new QueriesView(this, maxMembers);
        } else if (e.getActionCommand().equals("Finished")) {
            win.hide();
            System.exit(0);
        }
    }

    /**
     * Receive a new party from andPartyView.
     *
     * @param addPartyView	the AddPartyView that is providing a new party
     *
     */
    public void updateAddParty(AddPartyView addPartyView) {
        Vector<String> partyView = addPartyView.getParty();
        ArrayList<String> partyArr = new ArrayList<>();
        for (String party : partyView) {
            partyArr.add(party);
        }
        controlDesk.addPartyQueue(partyArr);
    }

    /**
     * Receive a broadcast from a ControlDesk
     *
     * @param ce	the ControlDeskEvent that triggered the handler
     *
     */
    @Override
    public void receiveControlDeskEvent(ControlDeskEvent ce) {
        ArrayList<String> partyQueue = ce.getPartyQueue();
        int size = partyQueue.size();
        String[] party_str = new String[size];
        int i = 0;
        for (String party : partyQueue) {
            party_str[i++] = party;
        }
        partyList.setListData((String[]) (party_str));
    }

}
