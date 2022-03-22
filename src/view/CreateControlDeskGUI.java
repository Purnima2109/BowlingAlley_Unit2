/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import controller.ControlDesk;
import event.ControlDeskEvent;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;
import lanepackage.Lane;
import lanepackage.Pinsetter;
import observer.ControlDeskObserver;

public class CreateControlDeskGUI implements ControlDeskObserver {

    private JPanel controlsPanel;
    private JPanel LaneStatusPanel;
    private JPanel partyPanel;
    private JList partyList;

    public void createControlDeskLaneStatusPanel(ControlDesk controlDesk) {

        LaneStatusPanel = new JPanel();
        int numLanes = controlDesk.getNumLanes();
        LaneStatusPanel.setLayout(new GridLayout(numLanes, 1));
        LaneStatusPanel.setBorder(new TitledBorder("LANE STATUS"));

        HashSet lanes = (HashSet) controlDesk.getLanes();
        Iterator it = lanes.iterator();
        int laneCount = 0;
        while (it.hasNext()) {
            Lane curLane = (Lane) it.next();
            LaneStatusView laneStat = new LaneStatusView(curLane, (laneCount + 1));
            curLane.subscribe(laneStat);
//            curLane.subscribe(laneStat);
            ((Pinsetter) curLane.getPinsetter()).subscribe(laneStat);
            JPanel lanePanel = laneStat.showLane();
            lanePanel.setBorder(new TitledBorder("Lane" + ++laneCount));
            LaneStatusPanel.add(lanePanel);
        }

    }

    public JPanel getControlDeskLaneStatusPanel() {

        return LaneStatusPanel;
    }

    public void createControlDeskControlsPanel(ControlDeskView controlDesk) {
        // Controls Panel
        controlsPanel = new JPanel();
        controlsPanel.setLayout(new GridLayout(6, 6));
        controlsPanel.setBorder(new TitledBorder("CONTROLS"));
        this.addButtonToPanel("Add Party", controlDesk);
        this.addButtonToPanel("Finished", controlDesk);
        this.addButtonToPanel("Records", controlDesk);
    }

    public void addButtonToPanel(String Button_text, ControlDeskView controlDesk) {
        System.out.println("button : " + Button_text);
        JButton button = new JButton(Button_text);
        JPanel subControlPanel = new JPanel();
        subControlPanel.setLayout(new FlowLayout());
        button.addActionListener(controlDesk);
        subControlPanel.add(button);
        controlsPanel.add(subControlPanel);
    }

    public JPanel getControlsPanel() {
        return controlsPanel;
    }

    public void createControlDeskPartyQueuePanel(ControlDesk controlDesk) {
        partyPanel = new JPanel();
        partyPanel.setLayout(new FlowLayout());
        partyPanel.setBorder(new TitledBorder("Party Queue"));

//        Vector empty = new Vector();
//        empty.add("(Empty)");
        String[] str_emp = new String[1];
        str_emp[0] = "(Empty)";
        partyList = new JList(str_emp);
        partyList.setFixedCellWidth(120);
        partyList.setVisibleRowCount(10);
        JScrollPane partyPane = new JScrollPane(partyList);
        partyPane.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        partyPanel.add(partyPane);
        controlDesk.getSubscriber().subscribe(this);
//        controlDesk.getSubscriber().subscribe(this);
    }

    public JPanel getPartyPanel() {
        return partyPanel;
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
