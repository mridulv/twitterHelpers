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
public class regressionModel {
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
        String query = "SELECT retweet,favourites FROM analysis_tweets_new";

        Statement stmt = connection.createStatement();
        stmt.setFetchSize(Integer.MIN_VALUE);
        ResultSet rs = stmt.executeQuery(query);

        PreparedStatement preparedStatement;

        int count = 0 ;
        while(rs.next()){
            //String text = rs.getString("final_tweet").toLowerCase().replaceAll(" http.*?\\s", " ").replaceAll("[^\\w\\s\\,]","").replaceAll(","," ").replaceAll("http\\s*(\\w+)","");
            long t1 = rs.getLong("retweet");
            long t2 = rs.getLong("favourites");

            String insert = "INSERT into regression VALUES (?,?)";
            preparedStatement = connection2.prepareStatement(insert);
            preparedStatement.setLong(1,t1);
            preparedStatement.setLong(2,t2);
            preparedStatement.executeUpdate();

        }
        System.out.println(count);

    }
}
