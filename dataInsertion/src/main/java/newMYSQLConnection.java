/**
 * Created by mridul.v on 8/20/2014.
 */
/**
 * Created by mridul.v on 8/12/2014.
 */
import twitter4j.StallWarning;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterObjectFactory;
import twitter4j.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.*;
import java.sql.Date;
import java.util.*;

public class newMYSQLConnection {

    public static void main( String args[]) throws SQLException, ClassNotFoundException, IOException {
        String dbUrl = "jdbc:mysql://localhost/test";
        String dbClass = "com.mysql.jdbc.Driver";
        String username = "root";
        String password = "";

        Class.forName(dbClass);
        Connection connection = DriverManager.getConnection(dbUrl,username, password);
        Statement statement = connection.createStatement();

        //statement.executeUpdate(query);
        System.out.println("this is here");
        int i = 0;

        File folder = new File("C:\\Users\\mridul.v\\Downloads\\Twitter_Sentiment\\new_backend\\dump\\new_dump\\");
        File[] listOfFiles = folder.listFiles();

        //BufferedReader buffer = new BufferedReader(new FileReader("C:\\Users\\mridul.v\\Downloads\\Twitter_Sentiment\\backend\\FlumeData.1407786558156"));
        int total = 0;
        for (File file:listOfFiles) {
            BufferedReader buffer = new BufferedReader(new FileReader(file.getAbsolutePath()));
            int duplicates = 0;
            String line;
            String[] delimStr;
            PreparedStatement preparedStatement;
            java.util.Date startDate = new java.util.Date();
            while ((line = buffer.readLine()) != null) {

                delimStr = line.split(",");

                if (delimStr.length == 13) {
                    //System.out.println(file.getAbsoluteFile());

                    String query = "REPLACE into analysis_tweets_new(key_val,id,timestamp,user_id,followers,mention_id,hashtag,retweet,favourites,rating,lat_lng,place,lang,tweet) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

                    long reTweet = Long.valueOf(delimStr[7]);
                    double tweetRating = (reTweet + (0.88490419729401604)*reTweet + 132.30818861707201)/2;

                    preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setString(1, delimStr[0]);
                    preparedStatement.setString(2, delimStr[1]);
                    preparedStatement.setString(3, delimStr[2]);
                    preparedStatement.setString(4, delimStr[3]);
                    preparedStatement.setString(5, delimStr[4]);
                    preparedStatement.setString(6, delimStr[5]);
                    preparedStatement.setString(7, delimStr[6]);
                    preparedStatement.setString(8, delimStr[7]);
                    preparedStatement.setString(9, delimStr[8]);
                    preparedStatement.setFloat(10 , (float)tweetRating);
                    preparedStatement.setString(11, delimStr[9]);
                    preparedStatement.setString(12, delimStr[10]);
                    preparedStatement.setString(13, delimStr[11]);
                    preparedStatement.setString(14, delimStr[12]);
    //                preparedStatement.setDouble(14, 0);
    //                preparedStatement.setString(15, "");
    //                preparedStatement.setString(16, "");
    //                preparedStatement.setString(17, "");
                    preparedStatement.executeUpdate();

                    preparedStatement.close();
                }
            }
            java.util.Date endDate = new java.util.Date();
            long msElapsedTime = startDate.getTime() - endDate.getTime();
            System.out.println(msElapsedTime);

            System.out.println("file over total : " + total);
            total = total + 1;
        }
        //System.gc();
        connection.close();
    }
}
