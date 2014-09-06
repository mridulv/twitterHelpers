import com.google.gson.Gson;
import com.mashape.unirest.http.exceptions.UnirestException;
import twitter4j.JSONException;
import weka.core.Stopwords;

import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by mridul.v on 9/1/2014.
 */
public class tweetRating {
    public static void main(String args[]) throws SQLException, ClassNotFoundException, IOException, URISyntaxException, UnirestException, JSONException {

        String dbUrl = "jdbc:mysql://localhost/test";
        String dbClass = "com.mysql.jdbc.Driver";
        String username = "root";
        String password = "";

        Stopwords stopwords = new Stopwords();

        Class.forName(dbClass);
        Connection connection = DriverManager.getConnection(dbUrl, username, password);
        Connection connection2 = DriverManager.getConnection(dbUrl, username, password);

        // missing functionality:
        Date startDate = new Date();
        String query = "SELECT * FROM analysis_tweets";

        Statement stmt = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_UPDATABLE);
        stmt.setFetchSize(Integer.MIN_VALUE);
        ResultSet rs = stmt.executeQuery(query);

        int count = 0 ;
        while(rs.next()){
            long reTweet = rs.getLong("retweet");
            long id = rs.getLong("id");

            double tweetRating = (reTweet + (0.88490419729401604)*reTweet + 132.30818861707201)/2;

//            String queryAux = "SELECT * FROM analysis_tweets_new WHERE id = " + id;
//            Statement stmtAux = connection2.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
//                    ResultSet.CONCUR_UPDATABLE);
//            ResultSet rsAux = stmtAux.executeQuery(queryAux);
//            rsAux.next();

            rs.updateFloat("rating",(float)tweetRating);
            rs.updateRow();

            count++;
            if (count % 10000 == 0)
                System.out.println(count);
        }
    }
}
