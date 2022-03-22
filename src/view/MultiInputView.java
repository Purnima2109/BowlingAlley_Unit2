package view;
// Java program to implement
// a Simple Registration Form
// using Java Swing

import controller.ControlDesk;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MultiInputView
        extends JFrame
        implements ActionListener {

    // Components of the Form
    private JLabel title;
    private JPanel multiInputPanel;
    private Container c;
    private JLabel lanesJL;
    private JTextField lanesTF;
    private JLabel patronJL;
    private JTextField patronTF;
    private JButton sub;
    int numOfLanes = 0;
    int maxPatronsPerParty = 0;

    // constructor, to initialize the components
    // with default values.
    public MultiInputView() {
        setTitle("Input values");
        setBounds(550, 250, 600, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        c = getContentPane();
        c.setLayout(null);

        title = new JLabel("Input:");
        title.setFont(new Font("Arial", Font.PLAIN, 30));
        title.setSize(300, 30);
        title.setLocation(200, 30);
        c.add(title);

        lanesJL = new JLabel("Enter number of Lanes:");
        lanesJL.setFont(new Font("Arial", Font.PLAIN, 20));
        lanesJL.setSize(300, 20);
        lanesJL.setLocation(50, 100);
        c.add(lanesJL);

        lanesTF = new JTextField();
        lanesTF.setFont(new Font("Arial", Font.PLAIN, 15));
        lanesTF.setSize(100, 20);
        lanesTF.setLocation(460, 100);
        c.add(lanesTF);

        patronJL = new JLabel("Enter maximum number of patrons per party:");
        patronJL.setFont(new Font("Arial", Font.PLAIN, 20));
        patronJL.setSize(400, 20);
        patronJL.setLocation(50, 150);
        c.add(patronJL);

        patronTF = new JTextField();
        patronTF.setFont(new Font("Arial", Font.PLAIN, 15));
        patronTF.setSize(100, 20);
        patronTF.setLocation(460, 150);
        c.add(patronTF);

        sub = new JButton("Submit");
        sub.setFont(new Font("Arial", Font.PLAIN, 15));
        sub.setSize(100, 20);
        sub.setLocation(120, 200);
        sub.addActionListener(this);
        c.add(sub);

        setVisible(true);
    }

    public int getNumOfLanes() {
        return numOfLanes;
    }

    public int getMaxPatronsPerParty() {
        return maxPatronsPerParty;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == sub) {
            numOfLanes = Integer.parseInt(lanesTF.getText());
            maxPatronsPerParty = Integer.parseInt(patronTF.getText());
            setVisible(false);
            ControlDesk controlDesk;
            controlDesk = new ControlDesk(numOfLanes);
            controlDesk.start();
            ControlDeskView cdv = new ControlDeskView(controlDesk, maxPatronsPerParty);
        }
    }
}
