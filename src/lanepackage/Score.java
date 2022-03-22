/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lanepackage;

import java.util.HashMap;
import pojo.Bowler;

/**
 *
 * @author Asus
 */
public class Score {

    /**
     * getScore()
     *
     * Method that calculates a bowlers score
     *
     * @param Cur	The bowler that is currently up
     * @param frame	The frame the current bowler is on
     *
     * @return	The bowlers total score
     */
    public static int[][] getFinalScore(Bowler Cur, int frame, HashMap scores, int bowlIndex, int ball, int[][] cumulScores) {
        int[] curScore;
        int strikeballs = 0;
        curScore = (int[]) scores.get(Cur);
        for (int i = 0; i != 10; i++) {
            cumulScores[bowlIndex][i] = 0;
        }
//        curScore[0]=0;
//        curScore[1]=0;
        int current = 2 * (frame - 1) + ball - 1;
        //Iterate through each ball until the current one.
        for (int i = 0; i != current + 2; i++) {
            //Spare:
            if (i % 2 == 1 && curScore[i - 1] + curScore[i] == 10 && i < current - 1 && i < 19) {
                //This ball was a the second of a spare.  
                //Also, we're not on the current ball.
                //Add the next ball to the ith one in cumul.
                cumulScores[bowlIndex][(i / 2)] += curScore[i + 1] + curScore[i];
                if (i > 1) {
                    //cumulScores[bowlIndex][i/2] += cumulScores[bowlIndex][i/2 -1];
                }
            } else if (i < current && i % 2 == 0 && curScore[i] == 10 && i < 18) {
                strikeballs = 0;
                //This ball is the first ball, and was a strike.
                //If we can get 2 balls after it, good add them to cumul.
                if (curScore[i + 2] != -1) {
                    strikeballs = 1;
                    if (curScore[i + 3] != -1) {
                        //Still got em.
                        strikeballs = 2;
                    } else if (curScore[i + 4] != -1) {
                        //Ok, got it.
                        strikeballs = 2;
                    }
                }
                if (strikeballs == 2) {
                    //Add up the strike.
                    //Add the next two balls to the current cumulscore.
                    cumulScores[bowlIndex][i / 2] += 10;
                    if (curScore[i + 1] != -1) {
                        cumulScores[bowlIndex][i / 2] += curScore[i + 1] + cumulScores[bowlIndex][(i / 2) - 1];
                        if (curScore[i + 2] != -1) {
                            if (curScore[i + 2] != -2) {
                                cumulScores[bowlIndex][(i / 2)] += curScore[i + 2];
                            }
                        } else {
                            if (curScore[i + 3] != -2) {
                                cumulScores[bowlIndex][(i / 2)] += curScore[i + 3];
                            }
                        }
                    } else {
                        if (i / 2 > 0) {
                            cumulScores[bowlIndex][i / 2] += curScore[i + 2] + cumulScores[bowlIndex][(i / 2) - 1];
                        } else {
                            cumulScores[bowlIndex][i / 2] += curScore[i + 2];
                        }
                        if (curScore[i + 3] != -1) {
                            if (curScore[i + 3] != -2) {
                                cumulScores[bowlIndex][(i / 2)] += curScore[i + 3];
                            }
                        } else {
                            cumulScores[bowlIndex][(i / 2)] += curScore[i + 4];
                        }
                    }
                } else {
                    break;
                }
            } else {
                //We're dealing with a normal throw, add it and be on our way.
                if (i % 2 == 0 && i < 18) {
                    if (i / 2 == 0) {
                        //First frame, first ball.  Set his cumul score to the first ball
                        if (curScore[i] != -2) {
                            cumulScores[bowlIndex][i / 2] += curScore[i];
                        }
                    } else if (i / 2 != 9) {
                        //add his last frame's cumul to this ball, make it this frame's cumul.
                        if (curScore[i] != -2) {
                            cumulScores[bowlIndex][i / 2] += cumulScores[bowlIndex][i / 2 - 1] + curScore[i];
                        } else {
                            cumulScores[bowlIndex][i / 2] += cumulScores[bowlIndex][i / 2 - 1];
                        }
                    }
                } else if (i < 18) {
                    if (curScore[i] != -1 && i > 2) {
                        if (curScore[i] != -2) {
                            cumulScores[bowlIndex][i / 2] += curScore[i];
                        }
                    }
                }
                if (i / 2 == 9) {
                    if (i == 18) {
                        cumulScores[bowlIndex][9] += cumulScores[bowlIndex][8];
                    }
                    if (curScore[i] != -2) {
                        cumulScores[bowlIndex][9] += curScore[i];
                    }
                } else if (i / 2 == 10) {
                    if (curScore[i] != -2) {
                        cumulScores[bowlIndex][9] += curScore[i];
                    }
                }
            }
        }
        if (cumulScores[bowlIndex][0] == 0 && ball == 2) {
            System.out.println("00" + ball + " bowlIndex " + bowlIndex);

            cumulScores[bowlIndex][1] /= 2;
            for (int i = 2; i < 10; i++) {
                cumulScores[bowlIndex][i] -= cumulScores[bowlIndex][1];
            }
        }

        if ((frame <= 9 && ball == 2)) {

            for (int i = 1; i < 10; i++) {
                if (cumulScores[bowlIndex][i] == cumulScores[bowlIndex][i - 1]) {
                    int high = cumulScores[bowlIndex][0];
                    for (int j = 1; j < i; j++) {
                        high = Math.max(high, cumulScores[bowlIndex][j] - cumulScores[bowlIndex][j - 1]);
                    }
//cumulScores[bowlIndex][i] = cumulScores[bowlIndex][i] - high/2;
                    for (int j = i; j < 10; j++) {
                        cumulScores[bowlIndex][j] = cumulScores[bowlIndex][j] - high / 2;
                    }

                }
            }
        }
        return cumulScores;
    }

}
