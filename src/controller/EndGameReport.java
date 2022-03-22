package controller;

/**
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */

import pojo.Party;
import pojo.Bowler;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import java.util.*;
public class EndGameReport implements ActionListener, ListSelectionListener {

	private JFrame win;
	private JButton printButton;
        private JButton finished;
	private JList<String> memberList;
	private java.util.List<String> retVal;

	private int result;

	private String selectedMember;
        Logger logger
            = Logger.getLogger(
                EndGameReport.class.getName());
	public EndGameReport( String partyName, Party party ) {
	
		result =0;
		retVal = new ArrayList<>();
		win = new JFrame("End Game Report for " + partyName + "?" );
		win.getContentPane().setLayout(new BorderLayout());
		((JPanel) win.getContentPane()).setOpaque(false);

		JPanel colPanel = new JPanel();
		colPanel.setLayout(new GridLayout( 1, 2 ));

		// Member Panel
		JPanel partyPanel = new JPanel();
		partyPanel.setLayout(new FlowLayout());
		partyPanel.setBorder(new TitledBorder("Party Members"));
		
		Iterator<Bowler> iter = (party.getMembers()).iterator();
                String[] myVector = new String[party.getMembers().size()];
                int i=0;
		while (iter.hasNext()){
                        myVector[i++]=((iter.next()).getNickName());
		}	
		memberList = new JList<>(myVector);
		memberList.setFixedCellWidth(120);
		memberList.setVisibleRowCount(5);
		memberList.addListSelectionListener(this);
		JScrollPane partyPane = new JScrollPane(memberList);
		partyPanel.add(partyPane);

		partyPanel.add( memberList );

		// Button Panel
		// Button Panel
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(2, 1));

		printButton = new JButton("Print Report");
		JPanel printButtonPanel = new JPanel();
		printButtonPanel.setLayout(new FlowLayout());
		printButton.addActionListener(this);
		printButtonPanel.add(printButton);

		finished = new JButton("Finished");
		JPanel finishedPanel = new JPanel();
		finishedPanel.setLayout(new FlowLayout());
		finished.addActionListener(this);
		finishedPanel.add(finished);

		buttonPanel.add(printButton);
		buttonPanel.add(finished);

		// Clean up main panel
		colPanel.add(partyPanel);
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
		if (e.getSource().equals(printButton)) {		
			//Add selected to the vector.
			retVal.add(selectedMember);
		}
		if (e.getSource().equals(finished)) {
                        win.setVisible(false);
			result = 1;
                        
		}

	}

	public void valueChanged(ListSelectionEvent e) {
		selectedMember =
			((String) ((JList) e.getSource()).getSelectedValue());
	}

	public java.util.List<String> getResult() {
		while ( result == 0 ) {
			try {
				Thread.sleep(10);
			} catch ( InterruptedException e ) {
                                Thread.currentThread().interrupt();
				logger.log(Level.INFO, "Interrupted" );
			}
		}
		return retVal;	
	}
     
        public void destroy(){
            win.setVisible(false);
        }
        
	public static void main( String[] args ) {
		ArrayList<Bowler> bowlers = new ArrayList<>();
		for ( int i=0; i<4; i++ ) {
                        String bowlerData = "aaaaa";
			bowlers.add( new Bowler( bowlerData, bowlerData, bowlerData ) );
		}
		Party party = new Party( bowlers );
		String partyName="wank";
                new EndGameReport( partyName, party );
	}
	
}

