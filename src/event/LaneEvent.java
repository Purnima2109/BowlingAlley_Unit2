package event;

/*  $Id$
 *
 *  Revisions:
 *    $Log: LaneEvent.java,v $
 *    Revision 1.6  2003/02/16 22:59:34  ???
 *    added mechnanical problem flag
 *
 *    Revision 1.5  2003/02/02 23:55:31  ???
 *    Many many changes.
 *
 *    Revision 1.4  2003/02/02 22:44:26  ???
 *    More data.
 *
 *    Revision 1.3  2003/02/02 17:49:31  ???
 *    Modified.
 *
 *    Revision 1.2  2003/01/30 21:21:07  ???
 *    *** empty log message ***
 *
 *    Revision 1.1  2003/01/19 22:12:40  ???
 *    created laneevent and laneobserver
 *
 *
 */
import java.util.HashMap;
import java.util.Map;
import pojo.Bowler;
import pojo.Party;

public class LaneEvent {

    private Party p;
    int frame;
    int ball;
    Bowler bowler;
    int[][] cumulScore;
    HashMap<Bowler, int[]> score;
    int index;
    int frameNum;
    int[] curScores;
    boolean mechProb;

    public LaneEvent(Map<Object, Object> params) {
        p = (Party) params.get("party");
        index = (int) params.get("bowlIndex");
        bowler = (Bowler) params.get("currentThrower");
        cumulScore = (int[][]) params.get("cumulScore");
        score = (HashMap<Bowler, int[]>) params.get("score");
        curScores = (int[]) params.get("curScores");
        frameNum = (int) params.get("frameNumber");
        ball = (int) params.get("ball");
        mechProb = (boolean) params.get("gameIsHalted");
    }

    public boolean isMechanicalProblem() {
        return mechProb;
    }

    public int getFrameNum() {
        return frameNum;
    }

    public HashMap<Bowler, int[]> getScore() {
        return score;
    }

    public int[] getCurScores() {
        return curScores;
    }

    public int getIndex() {
        return index;
    }

    public int getFrame() {
        return frame;
    }

    public int getBall() {
        return ball;
    }

    public int[][] getCumulScore() {
        return cumulScore;
    }

    public Party getParty() {
        return p;
    }

    public Bowler getBowler() {
        return bowler;
    }
}
