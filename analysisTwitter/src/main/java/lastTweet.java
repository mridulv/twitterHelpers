import twitter4j.*;

import java.util.List;

/**
 * Created by mridul.v on 9/17/2014.
 */
public class lastTweet {
    public static void main(String args[]) throws TwitterException {
        long id = 27260086;
        Twitter twitter = new TwitterFactory().getInstance();
        System.out.println(id);
        List<Status> statuses = twitter.getUserTimeline(id);
        double sum = 0;
        for (Status tweet : statuses){
            System.out.println(tweet.getRetweetCount());
            sum = sum + tweet.getRetweetCount();
        }
        System.out.println(sum/statuses.size());
    }
}
