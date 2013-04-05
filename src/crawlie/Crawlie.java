/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package crawlie;

import java.util.Map;
import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

/**
 *
 * @author samuellouvan
 */
public class Crawlie {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws TwitterException {
        // TODO code application logic here
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey("")
                .setOAuthConsumerSecret("")
                .setOAuthAccessToken("")
                .setOAuthAccessTokenSecret("")
                .setHttpProxyHost("")
                .setHttpProxyPort(8080);

        TwitterFactory tf = new TwitterFactory(cb.build());
        Twitter twitter = tf.getInstance();
        
        //Paging p = new Paging(1);
        ResponseList<Status> statuses = twitter.getUserTimeline(19482988, new Paging(1, 200));
        System.out.println(statuses.size());
        for (int i = 0; i < statuses.size(); i++)
        {
            System.out.println(statuses.get(i).getUser().getScreenName());
        }
        /*IDs followers = twitter.getFollowersIDs("anakuidotcom", -1);
        if (followers.getIDs().length > 0)
        {
            boolean stop = false;
            do
            {
                long[] ids = followers.getIDs();
                for (long id : ids)
                    System.out.println(id);
                
                if (!followers.hasNext())
                    stop = true;
                else
                {
                    long cursor = followers.getNextCursor();
                    followers = twitter.getFollowersIDs("anakuidotcom", cursor);
                }
            }while (!stop);
        }*/
        /*long[] ids = followers.getIDs();
        for (int i = 0; i < ids.length; i++)
        {
            System.out.println(ids[i]);
        }*/
        
        Map<String,RateLimitStatus> limitStatus = twitter.getRateLimitStatus("followers");
        System.out.println(limitStatus.size());
        for (String key : limitStatus.keySet())
        {
            System.out.println(key);
            RateLimitStatus rateLimitStatus = limitStatus.get(key);
            System.out.println(rateLimitStatus);
        }
    }
}
