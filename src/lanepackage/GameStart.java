
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lanepackage;

import controller.EndGameReport;
import controller.ScoreReport;
import event.LaneEvent;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import pojo.Bowler;
import pojo.Party;

/**
 *
 * @author Asus
 */
public class GameStart {

    /**
     * resetScores()
     *
     * resets the scoring mechanism, must be called before scoring starts
     *
     * @pre the party has been assigned
     * @post scoring system is initialized
     */
    public static HashMap resetScores(Party party, HashMap scores) {
        Iterator bowlIt = (party.getMembers()).iterator();

        while (bowlIt.hasNext()) {
            int[] toPut = new int[25];
            for (int i = 0; i != 25; i++) {
                toPut[i] = -1;
            }
            scores.put(bowlIt.next(), toPut);
        }
        return scores;
    }

    /**
     * resetBowlerIterator()
     *
     * sets the current bower iterator back to the first bowler
     *
     * @pre the party as been assigned
     * @post the iterator points to the first bowler in the party
     */
    public static Iterator<Bowler> resetBowlerIterator(Party party) {
        return (party.getMembers()).iterator();
    }

    public static void printBowlerName(Iterator<Bowler> scoreIt, List<String> printVector, int[][] finalScores, int gameNumber) {
        int myIndex = 0;
        while (scoreIt.hasNext()) {
            Bowler thisBowler = (Bowler) scoreIt.next();
            ScoreReport sr = new ScoreReport(thisBowler, finalScores[myIndex++], gameNumber);
            sr.sendEmail(thisBowler.getEmail());
            Iterator printIt = printVector.iterator();
            while (printIt.hasNext()) {
                if (thisBowler.getNickName() == (String) printIt.next()) {
                    System.out.println("Printing " + thisBowler.getNickName());
                    sr.sendPrintout();
                }
            }

        }
    }

    static void doNotWantToPlayAgain(Party party,int[][] finalScores, int gameNumber) {
        List<String> printVector;
        EndGameReport egr = new EndGameReport(((Bowler) party.getMembers().get(0)).getNickName() + "'s Party", party);
        printVector = egr.getResult();
        Iterator scoreIt = party.getMembers().iterator();
        printBowlerName(scoreIt, printVector, finalScores, gameNumber);
    }

}
