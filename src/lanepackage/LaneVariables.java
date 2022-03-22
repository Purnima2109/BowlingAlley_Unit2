/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lanepackage;

import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Logger;
import pojo.Bowler;
import pojo.Party;

/**
 *
 * @author pc
 */
public class LaneVariables {

    private Party party;
    public boolean gameIsHalted;
    private boolean partyAssigned;
    public boolean gameFinished;
    private int[] curScores;
    private int[][] cumulScores;
    private HashMap scores;
    public int frameNumber;
    private int[][] finalScores;
    public int gameNumber;
    protected Logger logger;
    private boolean tenthFrameStrike;
    private boolean canThrowAgain;

    public LaneVariables() {
        scores = new HashMap();
        gameIsHalted = false;
        partyAssigned = false;
        gameNumber = 0;
    }

    public Party getParty() {
        return party;
    }

    public void setParty(Party party) {
        this.party = party;
    }

    public boolean isGameIsHalted() {
        return gameIsHalted;
    }

    public void setGameIsHalted(boolean gameIsHalted) {
        this.gameIsHalted = gameIsHalted;
    }

    public boolean isPartyAssigned() {
        return partyAssigned;
    }

    public void setPartyAssigned(boolean partyAssigned) {
        this.partyAssigned = partyAssigned;
    }

    public boolean isGameFinished() {
        return gameFinished;
    }

    public void setGameFinished(boolean gameFinished) {
        this.gameFinished = gameFinished;
    }

    public int[] getCurScores() {
        return curScores;
    }

    public void setCurScores(int[] curScores) {
        this.curScores = curScores;
    }

    public int[][] getCumulScores() {
        return cumulScores;
    }

    public void setCumulScores(int[][] cumulScores) {
        this.cumulScores = cumulScores;
    }

    public HashMap getScores() {
        return scores;
    }

    public void setScores(HashMap scores) {
        this.scores = scores;
    }
    
    public void setSCoresPut(Bowler Cur, int[] curScore) {
        scores.put(Cur, curScore);
    }
    

    public int getFrameNumber() {
        return frameNumber;
    }

    public void setFrameNumber() {
        this.frameNumber++;
    }
    
     public void resetFrameNumber() {
        this.frameNumber=0;
    }

      public int[][] getFinalScores() {
        return finalScores;
    }

    public void setFinalScores(int bowlIndex) {

        this.finalScores[bowlIndex][gameNumber] = cumulScores[bowlIndex][9];
    }
    
    public void resetFinalScores(int[][] finalScores) {

        this.finalScores=finalScores;
    }

    public int getGameNumber() {
        return gameNumber;
    }

    public void setGameNumber() {
        this.gameNumber ++;
    }
    
    public void resetGameNumber() {
        this.gameNumber=0;
    }

    public Logger getLogger() {
        return logger;
    }

    public void setLogger(Logger logger) {
        this.logger = logger;
    }

    public boolean isTenthFrameStrike() {
        return tenthFrameStrike;
    }

    public void setTenthFrameStrike(boolean tenthFrameStrike) {
        this.tenthFrameStrike = tenthFrameStrike;
    }

    public boolean isCanThrowAgain() {
        return canThrowAgain;
    }

    public void setCanThrowAgain(boolean canThrowAgain) {
        this.canThrowAgain = canThrowAgain;
    }

    

}
