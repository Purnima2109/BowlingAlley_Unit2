/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package lanepackage;

public class CalculateScore {
    private int[] cumulScores;
    public CalculateScore (int[] curScore,int index){
        int strikeballs = 0;
        cumulScores = new int[10];
        for (int i = 0; i != 10; i++) {
            cumulScores[i] = 0;
        }
        int current = index - 2;
        for (int i = 0; i != current + 2; i++) {
            if (spare(curScore, current, i)) break;
        }

    }

    private boolean spare(int[] curScore, int current, int i) {
        int strikeballs;//Spare:
        if (checkCondition(i,curScore,current)) {
            //This ball was the second of a spare.
            //Also, we're not on the current ball.
            //Add the next ball to the ith one in cumul.
            cumulScores[(i / 2)] += curScore[i + 1] + curScore[i];
        } else if (i % 2 == 0 && curScore[i] == 10 && i < Math.min(current,18)) {
            if (strike(curScore, i)) return true;
        } else {
            normalThrow(curScore, i);
        }
        return false;
    }

    private boolean strike(int[] curScore, int i) {
        int strikeballs;
        strikeballs = 0;
        //This ball is the first ball, and was a strike.
        //If we can get 2 balls after it, good add them to cumul.
        if (curScore[i + 2] != -1) {
            if (curScore[i + 3] != -1 || curScore[i+4] != -1) {
                //Still got em.
                strikeballs = 2;
            }
            else
                strikeballs = 1;
        }
        if (addStrike(curScore, i, strikeballs)) return true;
        return false;
    }

    private boolean addStrike(int[] curScore, int i, int strikeballs) {
        if (strikeballs == 2) {
            //Add up the strike.
            //Add the next two balls to the current cumulscore.
            cumulScores[i / 2] += 10;
            if (curScore[i + 1] != -1) {
                cumulScores[i / 2] += curScore[i + 1] + cumulScores[(i / 2) - 1];
                if (curScore[i + 2] != -1) {
                    cumulScores[(i / 2)] += curScore[i + 2];
                } else {
                    cumulScores[(i / 2)] += curScore[i + 3];
                }
            } else {
                if (i / 2 > 0) {
                    cumulScores[i / 2] += curScore[i + 2] + cumulScores[(i / 2) - 1];
                } else {
                    cumulScores[i / 2] += curScore[i + 2];
                }
                if (curScore[i + 3] != -1) {
                    cumulScores[(i / 2)] += curScore[i + 3];
                } else {
                    cumulScores[(i / 2)] += curScore[i + 4];
                }
            }
        } else {
            return true;
        }
        return false;
    }

    private void normalThrow(int[] curScore, int i) {
        //We're dealing with a normal throw, add it and be on our way.
        addThrow(curScore, i);
        if (i / 2 == 9) {
            if (i == 18) {
                cumulScores[9] += cumulScores[8];
            }
            cumulScores[9] += curScore[i];
        } else if (i / 2 == 10) {
            cumulScores[9] += curScore[i];
        }
    }

    private void addThrow(int[] curScore, int i) {
        if (i % 2 == 0 && i < 18) {
            addCumul(curScore, i);
        } else if (i < 18) {
            if (curScore[i] != -1 && i > 2) {
                cumulScores[i / 2] += curScore[i];
            }
        }
    }

    private void addCumul(int[] curScore, int i) {
        if (i / 2 == 0) {
            //First frame, first ball.  Set his cumul score to the first ball
            cumulScores[i / 2] += curScore[i];
        } else if (i / 2 != 9) {
            //add his last frame's cumul to this ball, make it this frame's cumul.
            cumulScores[i / 2] += cumulScores[i / 2 - 1] + curScore[i];
        }
    }

    private boolean checkCondition(int i,int[]curScore,int current){
        if (i % 2 == 1 && curScore[i - 1] + curScore[i] == 10 && i < Math.min(current-1,19)) {
            return true;
        }
        return false;
    }
    public int[] getCumulScores() { return cumulScores;}
}