import com.mashape.unirest.http.exceptions.UnirestException;
import twitter4j.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.*;
import java.util.Date;

/**
 * Created by mridul.v on 8/21/2014.
 */
public class analysisMain {
    public static void main(String args[]) throws SQLException, ClassNotFoundException, IOException, URISyntaxException, UnirestException, JSONException {
        String dbUrl = "jdbc:mysql://localhost/test";
        String dbClass = "com.mysql.jdbc.Driver";
        String username = "root";
        String password = "";

        helperGender helper1 = new helperGender();
        helperLocation helper2 = new helperLocation();
        helperContent helper3 = new helperContent();
        helperSentiment helper4 = new helperSentiment();

        Twitter twitter = new TwitterFactory().getInstance();
        Class.forName(dbClass);
        Connection connection = DriverManager.getConnection(dbUrl, username, password);

        // missing functionality:
        Date startDate = new Date();
        String query = "SELECT * FROM new_tweets where key_val LIKE '120536157%' ";

        Statement stmt = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
        ResultSet rs = stmt.executeQuery(query);

        while(rs.next()) {
            String user_id = rs.getString("user_id");
            String tweet_text = rs.getString("tweet");

            System.out.println(user_id);
            if (user_id.length() > 0) {
                User user = null;
                try {
                    String valGroup;
                    user = twitter.showUser(Long.valueOf(user_id));
                    String valUsername = user.getName().replaceAll("[^\\w\\s]", "");
                    String valKeywords = helper3.getContent(tweet_text);

                    if (!valUsername.replaceAll("\\s+","").equals("")) {
                         System.out.println(valUsername);
                         valGroup = helper1.getGender(valUsername.split(" ")[0]);
                    }
                    else
                        valGroup = "unknown";

                    double valSentiment = helper4.getSentiment(tweet_text);
                    String valCountry = "WORLD";

                    if (!user.getLocation().equals("")) {
                        valCountry = helper2.getLocation(user.getLocation());
                    }

                    if (valGroup == null){
                        valGroup = "unknown";
                    }

                    System.out.println("name is  " + valUsername);
                    System.out.println("gender is " + valGroup);
                    System.out.println("country is " + valCountry);
                    System.out.println("keywords are " + valKeywords);


                    rs.updateDouble("sentiment",valSentiment);
                    rs.updateString("username",valUsername);
                    rs.updateString("groups",valGroup);
                    rs.updateString("country",valCountry);
                    rs.updateString("keywords",valKeywords);
                    rs.updateRow();
                } catch (TwitterException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
