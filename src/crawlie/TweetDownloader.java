/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package crawlie;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

/**
 *
 * @author samuellouvan
 */
public class TweetDownloader implements Runnable {

    private int id;
    private ConfigurationBuilder cb;
    private TwitterFactory twitterFactory;
    private Twitter twitter;
    private ArrayList<Long> userID;
    private ArrayList<String> screenNames;
    private final long DELAY = 900000;
    private int startIdx;
    private int endIdx;
    private PrintWriter writer;

    public TweetDownloader(int ID, boolean proxy) throws FileNotFoundException {

        this.id = ID;
        cb = new ConfigurationBuilder();
        CrawlerConfigReader configReader = CrawlerConfigReader.getInstance();
        Configuration conf = configReader.readConfiguration(ID);

        cb.setOAuthConsumerKey(conf.getConsumerKey());
        cb.setOAuthConsumerSecret(conf.getConsumerSecret());
        cb.setOAuthAccessToken(conf.getAccessToken());
        cb.setOAuthAccessTokenSecret(conf.getAccessTokenSecret());
        if (proxy) {
            cb.setHttpProxyHost(conf.getProxyHost());
            cb.setHttpProxyPort(conf.getProxyPort());
        }
        twitterFactory = new TwitterFactory(cb.build());
        twitter = twitterFactory.getInstance();
    }

    public void addUserIDS(ArrayList<Long> ids) throws FileNotFoundException {
        userID = ids;
    }

    public void setScreenNames(ArrayList<String> screenNames) {
        this.screenNames = screenNames;
    }

    public void setEndIdx(int endIdx) {
        this.endIdx = endIdx;
    }

    public void setStartIdx(int startIdx) {
        this.startIdx = startIdx;
    }

    public int getEndIdx() {
        return endIdx;
    }

    public int getStartIdx() {
        return startIdx;
    }

    public String getCurrentTime()
    {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        return sdf.format(cal.getTime());
        
    }
    @Override
    public void run() {
        try {
            int currentIdx = startIdx;
            int fold = 1;
            int cnt = 0;
            writer = new PrintWriter("tweet-thread" + id + "-" + fold + "-"+getCurrentTime()+".txt");
            while (currentIdx <= endIdx) {
                int remaining = getRateLimitStatusUserTimeLine();
                if (remaining > 0) {
                    System.out.println("Thread :" + id + " getting the status from index :" + currentIdx + "(" + endIdx + ")" + " call remaining" + remaining);

                    ResponseList<Status> statuses = null;
                    try {
                        if (userID == null || userID.size() == 0) {
                            statuses = twitter.getUserTimeline(screenNames.get(currentIdx), new Paging(1, 200));
                            System.out.println("ScreenName : " + screenNames.get(currentIdx));
                        } else {
                            statuses = twitter.getUserTimeline(userID.get(currentIdx), new Paging(1, 200));
                            System.out.println("ID : " + userID.get(currentIdx));
                        }
                    } catch (TwitterException ex) {
                        Logger.getLogger(TweetDownloader.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    if (statuses != null) {
                        cnt++;
                        for (Status status : statuses) {
                            writer.print("<Tweet>\n");
                            writer.print("<statusID>" + status.getId() + "</statusID>\n");
                            writer.print("<username>" + status.getUser().getScreenName() + "</username>\n");
                            writer.print("<status>" + status.getText() + "</status>\n");
                            writer.print("<follower>" + status.getUser().getFollowersCount() + "</follower>\n");
                            writer.print("<following>" + status.getUser().getFriendsCount() + "</following>\n");
                            writer.print("<listcount>" + status.getUser().getListedCount() + "</listcount>\n");
                            writer.print("<replyTo>" + status.getInReplyToScreenName() + "</replyTo>\n");
                            writer.print("<RTcount>" + status.getRetweetCount() + "</RTCount>\n");
                            writer.print("<country>" + status.getPlace() + "</country>\n");
                            writer.print("<language>" + status.getUser().getLang() + "</language>\n");
                            writer.print("<time>" + status.getCreatedAt() + "</time>\n");
                            writer.print("<isRT>" + status.isRetweet() + "</isRT>\n");
                            writer.print("<Location>" + status.getGeoLocation() + "</Location>\n");
                            writer.print("<isFavorited>" + status.isFavorited() + "</isFavorited>\n");
                            writer.print("</Tweet>\n");
                            writer.flush();
                        }
                        if (cnt % 180 == 0) {
                            writer.close();
                            fold++;
                            writer = new PrintWriter("tweet-thread" + id + "-" + fold + ".txt");
                        }
                    }
                } else {
                    try {
                        System.out.println("Thread :" + id + " Limit reached, time to sleep for " + DELAY + " seconds");
                        Thread.sleep(DELAY);
                        System.out.println("Thread :" + id + " wakes up");
                    } catch (InterruptedException ex) {
                        Logger.getLogger(TweetDownloader.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                currentIdx++;
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(TweetDownloader.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
        System.out.println("Thread :" + id + " finished");
    }

    public int getRateLimitStatusUserTimeLine() {
        try {

            Map<String, RateLimitStatus> limitStatus = twitter.getRateLimitStatus("statuses");
            RateLimitStatus rateLimitStatus = null;
            System.out.println(limitStatus.size());
            for (String key : limitStatus.keySet()) {
                if (key.contains("user_timeline")) {
                    rateLimitStatus = limitStatus.get(key);
                }
            }
            if (rateLimitStatus != null) {
                return rateLimitStatus.getRemaining();
            }


        } catch (TwitterException ex) {
            Logger.getLogger(TweetDownloader.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }
}
