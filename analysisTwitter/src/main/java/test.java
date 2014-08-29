import com.mashape.unirest.http.exceptions.UnirestException;
import twitter4j.JSONException;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;

import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.*;
import java.util.Date;

/**
 * Created by mridul.v on 8/25/2014.
 */
public class test {
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
        String query = "SELECT * FROM new_tweets where key_val LIKE '1783214%'";
        Statement stmt = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
        ResultSet rs = stmt.executeQuery(query);

        int count = 0;

        while(rs.next()) {
            System.out.println(rs.getString("group"));
            if (rs.getString("group").replaceAll("\\s+","").equals("unknown")){
                count++;
            }
        }

        System.out.println(count);
    }
}
