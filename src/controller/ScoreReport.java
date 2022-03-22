package controller;


/**
 *
 * SMTP implementation based on code by Ral Gagnon mailto:real@rgagnon.com
 *
 */
import model.ScoreHistoryFile;
import pojo.Score;
import pojo.Bowler;
import java.io.*;
import java.util.Iterator;
import java.net.*;
import java.awt.print.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ScoreReport {

    private String content;
    private static final String LOGGERMSG = "logging: Exception in ScoreReport. ";
    Logger logger
            = Logger.getLogger(
                    ScoreReport.class.getName());

    public ScoreReport(Bowler bowler, int[] scores, int games) {
        String nick = bowler.getNickName();
        String full = bowler.getFullName();
        List<Score> v = null;
        try {
            v = ScoreHistoryFile.getScores(nick);
            Iterator<Score> scoreIt = v.iterator();

            StringBuilder bld = new StringBuilder();
            bld.append("--Lucky Strike Bowling Alley Score Report--\n");
            bld.append("\n");
            bld.append("Report for " + full + ", aka \"" + nick + "\":\n");
            bld.append("\n");
            bld.append("Final scores for this session: ");
            bld.append(scores[0]);
            for (int i = 1; i < games; i++) {
                bld.append(", " + scores[i]);
            }
            bld.append(".\n");
            bld.append("\n");
            bld.append("\n");
            bld.append("Previous scores by date: \n");
            while (scoreIt.hasNext()) {
                Score score = scoreIt.next();
                bld.append("  " + score.getDate() + " - " + score.getScore());
                bld.append("\n");
            }
            bld.append("\n\n");
            bld.append("Thank you for your continuing patronage.");
            content = bld.toString();
        } catch (Exception e) {
            logger.log(Level.SEVERE, LOGGERMSG, e);
        }

    }

    public void sendEmail(String recipient) {
        try (Socket s = new Socket("osfmail.rit.edu", 25);) {

            BufferedReader in
                    = new BufferedReader(
                            new InputStreamReader(s.getInputStream(), StandardCharsets.UTF_8));
            BufferedWriter out
                    = new BufferedWriter(
                            new OutputStreamWriter(s.getOutputStream(), StandardCharsets.UTF_8));

            // here you are supposed to send your username
            sendln(in, out, "HELO world");
            sendln(in, out, "MAIL FROM: <mda2376@rit.edu>");
            sendln(in, out, "RCPT TO: <" + recipient + ">");
            sendln(in, out, "DATA");
            sendln(out, "Subject: Bowling Score Report ");
            sendln(out, "From: <Lucky Strikes Bowling Club>");

            sendln(out, "Content-Type: text/plain; charset=\"us-ascii\"\r\n");
            sendln(out, content + "\n\n");
            sendln(out, "\r\n");

            sendln(in, out, ".");
            sendln(in, out, "QUIT");
        } catch (Exception e) {
            logger.log(Level.SEVERE, LOGGERMSG, e);
        }
    }

    public void sendPrintout() {
        PrinterJob job = PrinterJob.getPrinterJob();

        PrintableText printobj = new PrintableText(content);

        job.setPrintable(printobj);

        if (job.printDialog()) {
            try {
                job.print();
            } catch (PrinterException e) {
                logger.log(Level.SEVERE, LOGGERMSG, e);
            }
        }

    }

    public void sendln(BufferedReader in, BufferedWriter out, String s) {
        try {
            out.write(s + "\r\n");
            out.flush();
            s = in.readLine();
            logger.log(Level.INFO, s);

        } catch (Exception e) {
            logger.log(Level.SEVERE, LOGGERMSG, e);
        }
    }

    public void sendln(BufferedWriter out, String s) {
        try {
            out.write(s + "\r\n");
            out.flush();
            logger.log(Level.INFO, s);
        } catch (Exception e) {
            logger.log(Level.SEVERE, LOGGERMSG, e);
        }
    }

}
