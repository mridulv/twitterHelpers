import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;

/**
 * Created by mridul.v on 9/10/2014.
 */
public class testClas {
    public static void main(String args[]) throws TwitterException {
        long id = 27260086;
        Twitter twitter = new TwitterFactory().getInstance();
        System.out.println(id);
        User user = twitter.showUser(id);
        System.out.println(user.getScreenName());
    }
}
