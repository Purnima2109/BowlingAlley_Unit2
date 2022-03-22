package utility;

import constants.Constants;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import javafx.util.Pair;

public class Records {

    public static String getRecordForBowler(String name)
            throws IOException {
        ArrayList<Integer> score = new ArrayList<Integer>();

        String output;
        try (BufferedReader in = new BufferedReader(new FileReader(Constants.BOWLER_HIST));) {
            String data1;
            while ((data1 = in.readLine()) != null) {
// File format is nick\tfname\te-mail
//"Nick: bowler[0] Full: bowler[1] email: bowler[2]
                String[] record = data1.split("\t");
                if (record[0].equals(name)) {

                    score.add(Integer.parseInt(record[2]));
                }
//allBowlers.add(bowler);
            }

            int highest = Integer.MIN_VALUE;
            int lowest = Integer.MAX_VALUE;
            int numGames = 0;
            int totalScore = 0;
            float avgScore;
            for (Integer i : score) {
                int curScore = i.intValue();
                totalScore += curScore;
                if (curScore > highest) {
                    highest = curScore;
                }
                if (curScore < lowest) {
                    lowest = curScore;
                }
            }
            numGames = score.size();
            avgScore = totalScore / (float) numGames;
            if (numGames == 0) {
                output = "Record not available!!\n" + name + " has not played any game";
            } else {
                output = "Nick Name: " + name + "\nHighest Score: " + Integer.toString(highest) + "\nLowest Score: "
                        + Integer.toString(lowest) + "\nNumber of Games: " + Integer.toString(numGames)
                        + "\nAverage score of " + name + ": " + Float.toString(avgScore);
            }
//allBowlers.add(bowler);
        }

        return output;
    }

    public static String getHighestScore() throws IOException {
        try (BufferedReader in = new BufferedReader(new FileReader(Constants.BOWLER_HIST));) {
            String data;
            int max = Integer.MIN_VALUE;
            String name = "";
            while ((data = in.readLine()) != null) {
                String[] content = data.split("\t");
                int val = Integer.parseInt(content[2]);
                if (val > max) {
                    max = val;
                    name = content[0];
                }
            }
            return "Player having Highest score is:\nName: " + name + ", Score: " + max;
        }
    }

    public static String getLowestScore() throws IOException {
        try (BufferedReader in = new BufferedReader(new FileReader(Constants.BOWLER_HIST));) {
            String data;
            int min = Integer.MAX_VALUE;
            String name = "";
            while ((data = in.readLine()) != null) {
                String[] content = data.split("\t");
                int val = Integer.parseInt(content[2]);
                if (val < min) {
                    min = val;
                    name = content[0];
                }
            }
            return "Player having Lowest score is:\nName: " + name + ", Score: " + min;
        }
    }

    public static HashMap<String, Integer> leaderBoard() throws IOException {
        try (BufferedReader in = new BufferedReader(new FileReader(Constants.BOWLER_HIST));) {
            String data;
            HashMap<String, Integer> leaders = new HashMap<>();
            while ((data = in.readLine()) != null) {
                String[] content = data.split("\t");
                int val = Integer.parseInt(content[2]);
                if (leaders.containsKey(content[0])) {
                    int curVal = leaders.get(content[0]);
                    leaders.put(content[0], curVal + val);
                } else {
                    leaders.put(content[0], val);
                }
            }
            HashMap<String, Integer> leaders2 = new HashMap<>();
            leaders.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())).forEachOrdered(x -> leaders2.put(x.getKey(), x.getValue()));

            return leaders2;
        }
    }

    public static String getLeaderBoard() throws IOException {
        HashMap<String, Integer> leaders = leaderBoard();
        String leader = "";
        for (String name : leaders.keySet()) {
            String key = name.toString();
            String value = leaders.get(name).toString();
            leader += name + ", " + value + "\n";
        }
        return leader;
    }

    public static String getTopPlayer() throws IOException {
        HashMap<String, Integer> leaders = leaderBoard();
        String name = leaders.entrySet().stream().max((entry1, entry2) -> entry1.getValue() > entry2.getValue() ? 1 : -1).get().getKey();
        return "Top scorer is:\nName: " + name + ", Score: " + leaders.get(name);
    }

}
