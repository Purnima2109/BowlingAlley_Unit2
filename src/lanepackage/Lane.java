package lanepackage;


/* $Id$
 *
 * Revisions:
 *   $Log: Lane.java,v $
 *   Revision 1.52  2003/02/20 20:27:45  ???
 *   Fouls disables.
 *
 *   Revision 1.51  2003/02/20 20:01:32  ???
 *   Added things.
 *
 *   Revision 1.50  2003/02/20 19:53:52  ???
 *   Added foul support.  Still need to update laneview and test this.
 *
 *   Revision 1.49  2003/02/20 11:18:22  ???
 *   Works beautifully.
 *
 *   Revision 1.48  2003/02/20 04:10:58  ???
 *   Score reporting code should be good.
 *
 *   Revision 1.47  2003/02/17 00:25:28  ???
 *   Added disbale controls for View objects.
 *
 *   Revision 1.46  2003/02/17 00:20:47  ???
 *   fix for event when game ends
 *
 *   Revision 1.43  2003/02/17 00:09:42  ???
 *   fix for event when game ends
 *
 *   Revision 1.42  2003/02/17 00:03:34  ???
 *   Bug fixed
 *
 *   Revision 1.41  2003/02/16 23:59:49  ???
 *   Reporting of sorts.
 *
 *   Revision 1.40  2003/02/16 23:44:33  ???
 *   added mechnanical problem flag
 *
 *   Revision 1.39  2003/02/16 23:43:08  ???
 *   added mechnanical problem flag
 *
 *   Revision 1.38  2003/02/16 23:41:05  ???
 *   added mechnanical problem flag
 *
 *   Revision 1.37  2003/02/16 23:00:26  ???
 *   added mechnanical problem flag
 *
 *   Revision 1.36  2003/02/16 21:31:04  ???
 *   Score logging.
 *
 *   Revision 1.35  2003/02/09 21:38:00  ???
 *   Added lots of comments
 *
 *   Revision 1.34  2003/02/06 00:27:46  ???
 *   Fixed a race condition
 *
 *   Revision 1.33  2003/02/05 11:16:34  ???
 *   Boom-Shacka-Lacka!!!
 *
 *   Revision 1.32  2003/02/05 01:15:19  ???
 *   Real close now.  Honest.
 *
 *   Revision 1.31  2003/02/04 22:02:04  ???
 *   Still not quite working...
 *
 *   Revision 1.30  2003/02/04 13:33:04  ???
 *   Lane may very well work now.
 *
 *   Revision 1.29  2003/02/02 23:57:27  ???
 *   fix on pinsetter hack
 *
 *   Revision 1.28  2003/02/02 23:49:48  ???
 *   Pinsetter generates an event when all pins are reset
 *
 *   Revision 1.27  2003/02/02 23:26:32  ???
 *   ControlDesk now runs its own thread and polls for free lanes to assign queue members to
 *
 *   Revision 1.26  2003/02/02 23:11:42  ???
 *   parties can now play more than 1 game on a lane, and lanes are properly released after games
 *
 *   Revision 1.25  2003/02/02 22:52:19  ???
 *   Lane compiles
 *
 *   Revision 1.24  2003/02/02 22:50:10  ???
 *   Lane compiles
 *
 *   Revision 1.23  2003/02/02 22:47:34  ???
 *   More observering.
 *
 *   Revision 1.22  2003/02/02 22:15:40  ???
 *   Add accessor for pinsetter.
 *
 *   Revision 1.21  2003/02/02 21:59:20  ???
 *   added conditions for the party choosing to play another game
 *
 *   Revision 1.20  2003/02/02 21:51:54  ???
 *   LaneEvent may very well be observer method.
 *
 *   Revision 1.19  2003/02/02 20:28:59  ???
 *   fixed sleep thread bug in lane
 *
 *   Revision 1.18  2003/02/02 18:18:51  ???
 *   more changes. just need to fix scoring.
 *
 *   Revision 1.17  2003/02/02 17:47:02  ???
 *   Things are pretty close to working now...
 *
 *   Revision 1.16  2003/01/30 22:09:32  ???
 *   Worked on scoring.
 *
 *   Revision 1.15  2003/01/30 21:45:08  ???
 *   Fixed speling of received in Lane.
 *
 *   Revision 1.14  2003/01/30 21:29:30  ???
 *   Fixed some MVC stuff
 *
 *   Revision 1.13  2003/01/30 03:45:26  ???
 *   *** empty log message ***
 *
 *   Revision 1.12  2003/01/26 23:16:10  ???
 *   Improved thread handeling in lane/controldesk
 *
 *   Revision 1.11  2003/01/26 22:34:44  ???
 *   Total rewrite of lane and pinsetter for R2's observer model
 *   Added Lane/Pinsetter Observer
 *   Rewrite of scoring algorythm in lane
 *
 *   Revision 1.10  2003/01/26 20:44:05  ???
 *   small changes
 *
 * 
 */
import controller.EndGamePrompt;
import event.LaneEvent;
import event.PinsetterEvent;
import java.util.Calendar;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.ScoreHistoryFile;
import observer.LaneObserver;
import observer.PinsetterObserver;
import pojo.Bowler;
import pojo.Party;
import view.TieBreaker;

public class Lane extends Thread implements PinsetterObserver {

    private Pinsetter setter;
    private Iterator bowlerIterator;
    private int ball;
    private int bowlIndex;
    private boolean tenthFrameStrike;
    private boolean canThrowAgain;
    private Bowler currentThrower;			// = the thrower who just took a throw
    private LaneSubscribe laneSubscribe;
    private final Logger logger;
    public LaneVariables laneVariables;
    private int velocity, angle;
    public boolean throwBall;
    public String secondHighest;
    public String firstHighest;
    public int firstScore;
    public int secondScore;
    public Boolean closeTieBreaker;

    public HashMap<String, Integer> gameResult;

    /**
     * Lane()
     *
     * Constructs a new lane and starts its thread
     *
     * @pre none
     * @post a new lane has been created and its thered is executing
     */
    public Lane() {
        setter = new Pinsetter();
        laneVariables = new LaneVariables();
//        scores = new HashMap();
//        gameIsHalted = false;
//        partyAssigned = false;
//        gameNumber = 0;
        setter.subscribe(this);
        logger = Logger.getLogger(Lane.class.getName());
        laneSubscribe = new LaneSubscribe();
        throwBall = true;
        gameResult = new HashMap<>();
        closeTieBreaker = false;
        this.start();
    }

    /**
     * run()
     *
     * entry point for execution of this lane
     */
    public void run() {

        while (true) {
            if (laneVariables.isPartyAssigned() && !laneVariables.isGameFinished()) {	// we have a party on this lane, 
                // so next bower can take a throw

                while (laneVariables.isGameIsHalted()) {
                    try {
                        sleep(10);
                    } catch (InterruptedException e) {
                        logger.log(Level.SEVERE, "logging:", e);
                        Thread.currentThread().interrupt();
                    }
                }

                if (bowlerIterator.hasNext()) {
                    currentThrower = (Bowler) bowlerIterator.next();

                    canThrowAgain = true;
                    tenthFrameStrike = false;
                    ball = 0;
                    while (canThrowAgain) {
                        System.out.println("Can I throw Now? " + throwBall);
                        if (throwBall) {
                            System.out.println(throwBall);
                            setter.ballThrown(angle, velocity);		// simulate the thrower's ball hiting
                            ball++;
                            throwBall = false;
                        }
                    }

                    if (laneVariables.getFrameNumber() == 9) {
                        laneVariables.setFinalScores(bowlIndex);
                        try {
                            String dateString = "" + Calendar.HOUR_OF_DAY + ":" + Calendar.MINUTE + " " + Calendar.MONTH + "/" + Calendar.DAY_OF_MONTH + "/" + (Calendar.YEAR + 1900);
                            //gameResult.put(bowlIndex, laneVariables.getCumulScores()[bowlIndex][9]);
                            ScoreHistoryFile.addScore(currentThrower.getNickName(), dateString, Integer.toString(laneVariables.getCumulScores()[bowlIndex][9]));
                            gameResult.put(currentThrower.getNickName(), laneVariables.getCumulScores()[bowlIndex][9]);

                        } catch (Exception e) {
                            logger.log(Level.SEVERE, "logging: Exception in addScore. ", e);
                        }
                    }

                    setter.reset();
                    bowlIndex++;

                } else {
                    laneVariables.setFrameNumber();
//                    frameNumber++;
                    bowlerIterator = GameStart.resetBowlerIterator(laneVariables.getParty());
                    bowlIndex = 0;
                    if (laneVariables.getFrameNumber() > 9) {
                        laneVariables.setGameFinished(true);
//                        gameFinished = true;
                        laneVariables.setGameNumber();
//                        gameNumber++;
                    }
                }
            } else if (laneVariables.isPartyAssigned() && laneVariables.isGameFinished()) {
                //Shikha

                //gameResult.put(currentThrower.getNickName(), laneVariables.getCumulScores()[bowlIndex][9]);
                LinkedHashMap<String, Integer> gameResultSorted = new LinkedHashMap<String, Integer>();
                gameResult.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())).forEachOrdered(x -> gameResultSorted.put(x.getKey(), x.getValue()));
                int count = 1;
                Iterator<Map.Entry<String, Integer>> itr = gameResultSorted.entrySet().iterator();
                while (itr.hasNext() && count < 3) {
                    Map.Entry<String, Integer> entry = itr.next();
                    System.out.println("Key = " + entry.getKey()
                            + ", Value = " + entry.getValue());
                    if (count == 1) {
                        firstHighest = entry.getKey();
                        firstScore = entry.getValue();
                    }
                    if (count == 2) {
                        secondHighest = entry.getKey();
                        secondScore = entry.getValue();
                    }
                    count++;
                }
                System.out.println("First Highest: " + firstHighest);
                System.out.println("Second Highest: " + secondHighest);
                if (secondHighest != null ) {
                    try {
                        TieBreaker tb = new TieBreaker();
                        tb.runTieBreaker(firstHighest, secondHighest, firstScore, secondScore);
                        sleep(20);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Lane.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                 
                EndGamePrompt egp = new EndGamePrompt(((Bowler) laneVariables.getParty().getMembers().get(0)).getNickName() + "'s Party");
                int result = egp.getResult();
                egp.distroy();
                egp = null;

                logger.log(Level.INFO, "Result was: {0} ", result);

                // TODO: send record of scores to control desk
                if (result == 1) {					// yes, want to play again
                    laneVariables.setScores(GameStart.resetScores(laneVariables.getParty(), laneVariables.getScores()));
//                    scores = GameStart.resetScores(party, scores);
                    bowlerIterator = GameStart.resetBowlerIterator(laneVariables.getParty());

                } else if (result == 2) {// no, dont want to play another game
                    GameStart.doNotWantToPlayAgain(laneVariables.getParty(), laneVariables.getFinalScores(), laneVariables.getGameNumber());
//                    party = null;
//                    partyAssigned = false;
                    laneVariables.setParty(null);
                    laneVariables.setPartyAssigned(false);
                    laneSubscribe.publish(lanePublish());

                }
            }

            try {
                sleep(10);
            } catch (InterruptedException e) {
                logger.log(Level.SEVERE, "logging:", e);
                Thread.currentThread().interrupt();
            }
        }
    }

    /**
     * recievePinsetterEvent()
     *
     * recieves the thrown event from the pinsetter
     *
     * @pre none
     * @post the event has been acted upon if desiered
     *
     * @param pe The pinsetter event that has been received.
     */
    public void receivePinsetterEvent(PinsetterEvent pe) {

        if (pe.pinsDownOnThisThrow() >= 0) {			// this is a real throw
            markScore(currentThrower, laneVariables.getFrameNumber() + 1, pe.getThrowNumber(), pe.pinsDownOnThisThrow());

            // next logic handles the ?: what conditions dont allow them another throw?
            // handle the case of 10th frame first
            if (laneVariables.getFrameNumber() == 9) {
                if (pe.totalPinsDown() == 10) {
                    setter.resetPins();
                    if (pe.getThrowNumber() == 1) {
                        tenthFrameStrike = true;
                    }
                }

                if ((pe.totalPinsDown() != 10) && (pe.getThrowNumber() == 2 && tenthFrameStrike == false)) {
                    canThrowAgain = false;
                    //publish( lanePublish() );
                }

                if (pe.getThrowNumber() == 3) {
                    canThrowAgain = false;
                    //publish( lanePublish() );
                }
            } else { // its not the 10th frame

                if (pe.pinsDownOnThisThrow() == 10) {		// threw a strike
                    canThrowAgain = false;
                    //publish( lanePublish() );
                } else if (pe.getThrowNumber() == 2) {
                    canThrowAgain = false;
                    //publish( lanePublish() );
                } else if (pe.getThrowNumber() == 3) {
                    System.out.println("I'm here...");
                }
            }
        } else {								//  this is not a real throw, probably a reset
        }
    }

    /**
     * assignParty()
     *
     * assigns a party to this lane
     *
     * @pre none
     * @post the party has been assigned to the lane
     *
     * @param theParty	Party to be assigned
     */
    public void assignParty(Party theParty) {
        laneVariables.setParty(theParty);
//        party = theParty;
        bowlerIterator = GameStart.resetBowlerIterator(laneVariables.getParty());
        laneVariables.setPartyAssigned(true);
//        partyAssigned = true;

        laneVariables.setCurScores(new int[laneVariables.getParty().getMembers().size()]);
        laneVariables.setCumulScores(new int[laneVariables.getParty().getMembers().size()][10]);
//        finalScores = new int[laneVariables.getParty().getMembers().size()][128]; //Hardcoding a max of 128 games, bite me.
        laneVariables.resetFinalScores(new int[laneVariables.getParty().getMembers().size()][128]);
        laneVariables.resetGameNumber();

        laneVariables.setScores(GameStart.resetScores(laneVariables.getParty(), laneVariables.getScores()));
        laneVariables.setGameFinished(false);
        laneVariables.resetFrameNumber();
//        gameFinished = false;
//        frameNumber = 0;
    }

    /**
     * markScore()
     *
     * Method that marks a bowlers score on the board.
     *
     * @param Cur	The current bowler
     * @param frame	The frame that bowler is on
     * @param ball	The ball the bowler is on
     * @param score	The bowler's score
     */
    private void markScore(Bowler Cur, int frame, int ball, int score) {
        int[] curScore;
        int index = ((frame - 1) * 2 + ball);

        curScore = (int[]) laneVariables.getScores().get(Cur);

        curScore[ index - 1] = score;
        laneVariables.setSCoresPut(Cur, curScore);
//        scores.put(Cur, curScore);
        laneVariables.setCumulScores(Score.getFinalScore(Cur, frame, laneVariables.getScores(), bowlIndex, ball, laneVariables.getCumulScores()));
//        cumulScores = Score.getFinalScore(Cur, frame, scores, bowlIndex, ball, cumulScores);
        laneSubscribe.publish(lanePublish());
    }

    /**
     * lanePublish()
     *
     * Method that creates and returns a newly created laneEvent
     *
     * @return	The new lane event
     */
    private LaneEvent lanePublish() {
        Map<Object, Object> params = new HashMap<>();
        params.put("party", laneVariables.getParty());
        params.put("bowlIndex", bowlIndex);
        params.put("currentThrower", currentThrower);
        params.put("cumulScore", laneVariables.getCumulScores());
        params.put("score", laneVariables.getScores());
        params.put("curScores", laneVariables.getCurScores());
        params.put("frameNumber", laneVariables.getFrameNumber() + 1);
        params.put("ball", ball);
        params.put("gameIsHalted", laneVariables.isGameIsHalted());
        LaneEvent laneEvent = new LaneEvent(params);
        return laneEvent;
    }

//    /**
//     * isPartyAssigned()
//     *
//     * checks if a party is assigned to this lane
//     *
//     * @return true if party assigned, false otherwise
//     */
//    public boolean isPartyAssigned() {
//        return partyAssigned;
//    }
//
//    /**
//     * isGameFinished
//     *
//     * @return true if the game is done, false otherwise
//     */
//    public boolean isGameFinished() {
//        return gameFinished;
//    }
    public void subscribe(LaneObserver adding) {
        laneSubscribe.subscribe(adding);
    }

    /**
     * Accessor to get this Lane's pinsetter
     *
     * @return	A reference to this lane's pinsetter
     */
    public Pinsetter getPinsetter() {
        return setter;
    }

    /**
     * Pause the execution of this game
     */
    public void pauseGame() {
        laneVariables.setGameIsHalted(true);
//        gameIsHalted = true;
        laneSubscribe.publish(lanePublish());
    }
//    public void pauseGame1() {
//        laneVariables.setGameIsHalted(true);
//    }

    /**
     * Resume the execution of this game
     */
    public void unPauseGame() {

        laneVariables.setGameIsHalted(false);
//        gameIsHalted = false;
        laneSubscribe.publish(lanePublish());
    }

    public void allowToThrowBall(int angle, int velocity) {
        this.velocity = velocity;
        this.angle = angle;
        throwBall = true;
        System.out.println("ThorwBallSetToTrue");
    }
}
