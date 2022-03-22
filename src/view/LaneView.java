package view;

import static constants.Constants.HAPPY_FACE_PATH;
import static constants.Constants.OKAY_FACE_PATH;
import static constants.Constants.SAD_FACE_PATH;
import event.LaneEvent;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import lanepackage.Lane;
import observer.LaneObserver;
import pojo.Bowler;
import pojo.Party;

public class LaneView implements LaneObserver, ActionListener, ChangeListener {

    private int roll;
    private boolean initDone = true;
//    public final String HAPPY_FACE_PATH = "E:\\Sem2\\SE\\Project\\BowlingAlleyWorkingFinalCode\\BowlingAlleyWorkingFinalCode\\src\\images\\happy.png";
//    public final String OKAY_FACE_PATH = "E:\\Sem2\\SE\\Project\\BowlingAlleyWorkingFinalCode\\BowlingAlleyWorkingFinalCode\\src\\images\\okay.png";
//    public final String SAD_FACE_PATH = "E:\\Sem2\\SE\\Project\\BowlingAlleyWorkingFinalCode\\BowlingAlleyWorkingFinalCode\\src\\images\\sad.png";
    JFrame frame;
    Container cpanel;
    ArrayList bowlers;
    int cur;
    Iterator bowlIt;

    JPanel[][] balls;
    JLabel[][] ballLabel;
    JPanel[][] scores;
    JLabel[][] scoreLabel;
    JPanel[][] ballGrid;
    JPanel[] pins;
    JLabel[] emoteLabels;
    String[] emoteLabelsCode;

    JButton maintenance, pause, resume,throwBall;
    Lane lane;

    // frame
//    JFrame sliderFrame;
    // slider
    JSlider angleSlider,velocitySlider;

    // label
    JLabel angleLabel,velocityLabel;
    JLabel angleLabelHeading,velocityLabelHeading;

    public LaneView(Lane lane, int laneNum) {

        this.lane = lane;

        initDone = true;
        frame = new JFrame("Lane " + laneNum + ":");
        cpanel = frame.getContentPane();
        cpanel.setLayout(new BorderLayout());

        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                frame.hide();
            }
        });

        cpanel.add(new JPanel());

    }

    public void show() {
        frame.show();
    }

    public void hide() {
        frame.hide();
    }

    private JPanel makeFrame(Party party) {

        initDone = false;
        bowlers = (ArrayList) party.getMembers();
        int numBowlers = bowlers.size();

        JPanel panel = new JPanel();

        panel.setLayout(new GridLayout(0, 1));

        balls = new JPanel[numBowlers][22];
        ballLabel = new JLabel[numBowlers][22];
        scores = new JPanel[numBowlers][10];
        scoreLabel = new JLabel[numBowlers][10];
        ballGrid = new JPanel[numBowlers][10];
        pins = new JPanel[numBowlers];

        emoteLabels = new JLabel[numBowlers];
        emoteLabelsCode = new String[numBowlers]; //H:Happy,S:SAD,O:Okay
        for (int i = 0; i != numBowlers; i++) {
            for (int j = 0; j != 22; j++) {
                ballLabel[i][j] = new JLabel(" ");
                balls[i][j] = new JPanel();
                balls[i][j].setBorder(
                        BorderFactory.createLineBorder(Color.BLACK));
                balls[i][j].add(ballLabel[i][j]);
            }
        }

        for (int i = 0; i != numBowlers; i++) {
            for (int j = 0; j != 10; j++) {
                ballGrid[i][j] = new JPanel();
                ballGrid[i][j].setLayout(new GridLayout(0, 3));
                ballGrid[i][j].add(new JLabel("  "), BorderLayout.EAST);
                ballGrid[i][j].add(balls[i][2 * j], BorderLayout.EAST);
                ballGrid[i][j].add(balls[i][2 * j + 1], BorderLayout.EAST);
            }
//            int j = 10;
////                        System.out.println("h1");
//
//            ballGrid[i][j] = new JPanel();
//            ballGrid[i][j].setLayout(new GridLayout(0, 3));
////                        System.out.println("h2");
//            ballGrid[i][j].add(new JLabel("  "), BorderLayout.EAST);
//            ballGrid[i][j].add(balls[i][2 * j], BorderLayout.EAST);
//            for (j = 11; j != 14; j++) {
//                ballGrid[i][j] = new JPanel();
//                ballGrid[i][j].setLayout(new GridLayout(0, 3));
//                ballGrid[i][j].add(new JLabel("  "), BorderLayout.EAST);
//                ballGrid[i][j].add(balls[i][2 * j], BorderLayout.EAST);
//                ballGrid[i][j].add(balls[i][2 * j + 1], BorderLayout.EAST);
//            }

        }

        for (int i = 0; i != numBowlers; i++) {
            pins[i] = new JPanel();
            pins[i].setBorder(
                    BorderFactory.createTitledBorder(
                            ((Bowler) bowlers.get(i)).getNickName()));
            pins[i].setLayout(new GridLayout(0, 12));
            for (int k = 0; k != 10; k++) {
                scores[i][k] = new JPanel();
                scoreLabel[i][k] = new JLabel("  ", SwingConstants.CENTER);
                scores[i][k].setBorder(
                        BorderFactory.createLineBorder(Color.BLACK));
                scores[i][k].setLayout(new GridLayout(0, 1));
                scores[i][k].add(ballGrid[i][k], BorderLayout.EAST);
                scores[i][k].add(scoreLabel[i][k], BorderLayout.SOUTH);
                pins[i].add(scores[i][k], BorderLayout.EAST);
            }
            JPanel emotePanel = new JPanel();
            try {
                BufferedImage myPicture = ImageIO.read(new File(OKAY_FACE_PATH));
                emoteLabels[i] = new JLabel(new ImageIcon(myPicture.getScaledInstance(75, 75, Image.SCALE_SMOOTH)));
                emotePanel.add(emoteLabels[i]);
                emoteLabelsCode[i] = "O";
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            pins[i].add(emotePanel);
            panel.add(pins[i]);
//			panel.add(pins[i]);
        }

        initDone = true;

        //Angle Slider
        angleLabel = new JLabel();
        angleLabelHeading = new JLabel();

        // create a panel
        JPanel anglePanel = new JPanel();

        // create a slider
        angleSlider = new JSlider(0, 180, 90);
        angleSlider.setPreferredSize(new Dimension(700, 80));

        // paint the ticks and tracks
        angleSlider.setPaintTrack(true);
        angleSlider.setPaintTicks(true);
        angleSlider.setPaintLabels(true);

        // set spacing
        angleSlider.setMajorTickSpacing(30);
        angleSlider.setMinorTickSpacing(5);

        // setChangeListener
        angleSlider.addChangeListener(this);

        // add slider to panel
        anglePanel.add(angleLabelHeading);
        anglePanel.add(angleSlider);
        anglePanel.add(angleLabel);
        // set the text of label
        angleLabelHeading.setText("Angle");
        angleLabel.setText("Value = " + angleSlider.getValue());
        
        //Velocity Slider
        velocityLabel = new JLabel();
        velocityLabelHeading = new JLabel();

        // create a panel
        JPanel velocityPanel = new JPanel();

        // create a slider
        velocitySlider = new JSlider(0, 40, 10);
        velocitySlider.setPreferredSize(new Dimension(700, 80));

        // paint the ticks and tracks
        velocitySlider.setPaintTrack(true);
        velocitySlider.setPaintTicks(true);
        velocitySlider.setPaintLabels(true);

        // set spacing
        velocitySlider.setMajorTickSpacing(5);
        velocitySlider.setMinorTickSpacing(5);

        // setChangeListener
        velocitySlider.addChangeListener(this);

        // add slider to panel
        velocityPanel.add(velocityLabelHeading);
        velocityPanel.add(velocitySlider);
        velocityPanel.add(velocityLabel);
        // set the text of label
        velocityLabelHeading.setText("Velocity");
        velocityLabel.setText("Value = " + velocitySlider.getValue());
        
               
        panel.add(anglePanel);
        panel.add(velocityPanel);


        return panel;
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        angleLabel.setText("Value = " + angleSlider.getValue());
        velocityLabel.setText("Value = " + velocitySlider.getValue());
    }

    public void receiveLaneEvent(LaneEvent le) {
        if (lane.laneVariables.isPartyAssigned()) {
//int numBowlers = le.getParty().getMembers().size();
            int numBowlers = le.getParty().getMembers().size();
            while (!initDone) {
//System.out.println("chillin' here.");
                try {
                    Thread.sleep(1);
                } catch (Exception e) {
                }
            }

            if (le.getFrameNum() == 1
                    && le.getBall() == 0
                    && le.getIndex() == 0) {
                System.out.println("Making the frame.");
                cpanel.removeAll();
                cpanel.add(makeFrame(le.getParty()), "Center");

// Button Panel
                JPanel buttonPanel = new JPanel();
                buttonPanel.setLayout(new FlowLayout());

                Insets buttonMargin = new Insets(4, 4, 4, 4);

                pause = new JButton("Pause");
                JPanel pausePanel = new JPanel();
                pausePanel.setLayout(new FlowLayout());
                pause.addActionListener(this);
                pausePanel.add(pause);

                resume = new JButton("resume");
                JPanel resumePanel = new JPanel();
                resumePanel.setLayout(new FlowLayout());
                resume.addActionListener(this);
                resumePanel.add(resume);

                maintenance = new JButton("Maintenance Call");
                JPanel maintenancePanel = new JPanel();
                maintenancePanel.setLayout(new FlowLayout());
                maintenance.addActionListener(this);
                maintenancePanel.add(maintenance);
                
                throwBall = new JButton("Throw Ball");
                JPanel throwBallPanel = new JPanel();
                throwBallPanel.setLayout(new FlowLayout());
                throwBall.addActionListener(this);
                throwBallPanel.add(throwBall);
                
                
                buttonPanel.add(throwBallPanel);
                buttonPanel.add(pausePanel);
                buttonPanel.add(resumePanel);
                buttonPanel.add(maintenancePanel);

                cpanel.add(buttonPanel, "South");

                frame.pack();

            }

            int[][] lescores = le.getCumulScore();
            for (int k = 0; k < numBowlers; k++) {
                for (int i = 0; i <= le.getFrameNum() - 1; i++) {
                    if (lescores[k][i] != 0) {
                        scoreLabel[k][i].setText(
                                (new Integer(lescores[k][i])).toString());
                    }
                }
                for (int i = 0; i < 21; i++) {
                    if (((int[]) ((HashMap) le.getScore())
                            .get(bowlers.get(k)))[i]
                            != -1) {
                        if (((int[]) ((HashMap) le.getScore())
                                .get(bowlers.get(k)))[i]
                                == 10
                                && (i % 2 == 0 || i == 19)) {
                            ballLabel[k][i].setText("X");
                            try {
                                if (emoteLabelsCode[k] != "H") {
                                    emoteLabelsCode[k] = "H";
                                    BufferedImage emoteImage;
                                    emoteImage = ImageIO.read(new File(HAPPY_FACE_PATH));
                                    emoteLabels[k].setIcon(new ImageIcon(emoteImage.getScaledInstance(75, 75, Image.SCALE_SMOOTH)));
                                }
                            } catch (IOException ex) {
                                Logger.getLogger(LaneView.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        } else if (i > 0
                                && ((int[]) ((HashMap) le.getScore())
                                .get(bowlers.get(k)))[i]
                                + ((int[]) ((HashMap) le.getScore())
                                .get(bowlers.get(k)))[i
                                - 1]
                                == 10
                                && i % 2 == 1) {
                            ballLabel[k][i].setText("/");
                            if (emoteLabelsCode[k] != "H") {
                                try {

                                    emoteLabelsCode[k] = "H";
                                    BufferedImage emoteImage;
                                    emoteImage = ImageIO.read(new File(HAPPY_FACE_PATH));
                                    emoteLabels[k].setIcon(new ImageIcon(emoteImage.getScaledInstance(75, 75, Image.SCALE_SMOOTH)));
                                } catch (IOException ex) {
                                    Logger.getLogger(LaneView.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }
                        } else if (((int[]) ((HashMap) le.getScore()).get(bowlers.get(k)))[i] == -2) {

                            ballLabel[k][i].setText("F");
                        } else {
                            int temp = (new Integer(((int[]) ((HashMap) le.getScore())
                                    .get(bowlers.get(k)))[i]));
                            ballLabel[k][i].setText(String.valueOf(temp));
// System.out.println(new Integer(((int[]) ((HashMap) le.getScore())
// .get(bowlers.get(k)))[i]));
                            if (emoteLabelsCode[k] != "O") {
                                try {

                                    emoteLabelsCode[k] = "O";
                                    BufferedImage emoteImage;
                                    if (temp == 0) {
// System.out.println("ZEROO "+temp);
                                        emoteImage = ImageIO.read(new File(SAD_FACE_PATH));
                                    } else {
                                        emoteImage = ImageIO.read(new File(OKAY_FACE_PATH));
                                    }
                                    emoteLabels[k].setIcon(new ImageIcon(emoteImage.getScaledInstance(75, 75, Image.SCALE_SMOOTH)));
// System.out.println("NO "+k+i);
                                } catch (IOException ex) {
                                    Logger.getLogger(LaneView.class.getName()).log(Level.SEVERE, null, ex);
                                  }
                            }
                        }
                    }
                }
            }
        }
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(pause)) {
            lane.pauseGame();
        }
        if (e.getSource().equals(resume)) {
            lane.unPauseGame();
        }
        if (e.getSource().equals(maintenance)) {
            lane.pauseGame();
        }
        if (e.getSource().equals(throwBall)) {
            System.out.println("ThrowBall Action Performed");
            lane.throwBall=true;
            lane.allowToThrowBall(angleSlider.getValue(),velocitySlider.getValue());
        }
        if (e.getActionCommand().equals("Throw Ball")) {
            System.out.println("ThrowBall Action Performed");
            lane.throwBall=true;
            lane.allowToThrowBall(angleSlider.getValue(),velocitySlider.getValue());
        
        }
    }

}
