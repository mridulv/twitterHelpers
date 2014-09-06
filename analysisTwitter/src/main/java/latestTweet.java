import org.omg.CORBA.PUBLIC_MEMBER;
import twitter4j.*;
import weka.core.Stopwords;

import java.sql.*;
import java.util.Date;
import java.util.List;

/**
 * Created by mridul.v on 9/3/2014.
 */
public class latestTweet {
    public static long callFunc(long id) throws TwitterException {
        TwitterFactory factory = new TwitterFactory();
        Twitter twitter = factory.getInstance();
        List<Status> statuses = null;
//        statuses = twitter.getUserTimeline(id);
//        //System.out.println(statuses.get(0));
//        return statuses.get(0).getCreatedAt().getTime();
        try {
            statuses = twitter.getUserTimeline(id);
            //System.out.println(statuses);
            return statuses.get(0).getCreatedAt().getTime();
        } catch (TwitterException e) {
            return Long.MIN_VALUE;
        }
    }

    public static void main(String args[]) throws ClassNotFoundException, SQLException, TwitterException {

        String dbUrl = "jdbc:mysql://localhost/test";
        String dbClass = "com.mysql.jdbc.Driver";
        String username = "root";
        String password = "";

        Stopwords stopwords = new Stopwords();

        Class.forName(dbClass);
        Connection connection = DriverManager.getConnection(dbUrl, username, password);
        Connection connection2 = DriverManager.getConnection(dbUrl, username, password);

        // missing functionality:
        String query = "SELECT * from user_vector where difftime = 0";

        Statement stmt = connection.createStatement();
        stmt.setFetchSize(Integer.MIN_VALUE);
        ResultSet rs = stmt.executeQuery(query);

        Date d = new Date();

        while(rs.next()){
            long id = rs.getLong("user_id");
            String json = rs.getString("keyword_vector");
            long rating  = rs.getLong("rating");
            long time = callFunc(id);

            long diffTime = d.getTime() - time;
            if (diffTime < 0)
                diffTime = 0;
            System.out.println(diffTime);

            PreparedStatement preparedStatement;
            String insert = "REPLACE into user_vector VALUES (?,?,?,?)";
            preparedStatement = connection2.prepareStatement(insert);
            preparedStatement.setLong(1,id);
            preparedStatement.setString(2, json);
            preparedStatement.setLong(3, rating);
            preparedStatement.setLong(4, diffTime);
            preparedStatement.executeUpdate();
        }
    }
}
