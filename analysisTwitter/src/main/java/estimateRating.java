import javafx.util.Pair;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;

import java.sql.*;

/**
 * Created by mridul.v on 9/6/2014.
 */
public class estimateRating {
    public static void main(String args[]) throws SQLException, ClassNotFoundException, TwitterException {
        Class.forName(conn.dbClass);
        Connection connection = DriverManager.getConnection(conn.dbUrl, conn.username, conn.password);

        String entity = "zaynmalik";
        String text = "Good old number 11 never fails me :)";

        Estimate estimate = new Estimate();
        Pair<Double, Pair<Double, Double>> pairPair = estimate.getRetweetCount(connection, text);

        Twitter twitter = new TwitterFactory().getInstance();
        User user = twitter.showUser(entity);
        long followers = user.getFollowersCount();

        double c1 = 8.18808697e-04;
        double c2 = 6.59106942e+00;
        double c3 = 3.80304209e-01;
        double c4 = -1.35557023e+00;

        double estimateRating = c1*followers + c2*pairPair.getKey() + c3*pairPair.getValue().getKey() + c4*pairPair.getValue().getValue();

        System.out.println(estimateRating);
    }
}
