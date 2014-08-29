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
import java.util.HashMap;
import java.util.Map;

public class MYSQLConnectionTest {

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

        File folder = new File("C:\\Users\\mridul.v\\Downloads\\Twitter_Sentiment\\backend\\");
        File[] listOfFiles = folder.listFiles();

        //BufferedReader buffer = new BufferedReader(new FileReader("C:\\Users\\mridul.v\\Downloads\\Twitter_Sentiment\\backend\\FlumeData.1407786558156"));

        Map<String,Integer> k = new HashMap<String, Integer>();
        BufferedReader buffer = new BufferedReader(new FileReader("C:\\Users\\mridul.v\\Downloads\\Twitter_Sentiment\\backend\\FlumeData.1407823093387"));
        int total = 0;
        int duplicates = 0;
        String line;
        String[] delimStr;
        PreparedStatement preparedStatement;
        while ((line = buffer.readLine()) != null) {
            total = total + 1;

            delimStr = line.split(",");

            if (!k.containsKey(delimStr[0])) {
                k.put(delimStr[0], 1);
                String query = "REPLACE into tweetsaux3 VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
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
                preparedStatement.setString(10, delimStr[9]);
                preparedStatement.setString(11, delimStr[10]);
                preparedStatement.setString(12, delimStr[11]);
                preparedStatement.executeUpdate();
            } else {
                //System.out.println("got one dupli key");
                duplicates = duplicates + 1;
            }

            delimStr = null;
        }
        System.out.println(total + " " + duplicates);
        k.clear();
        //System.gc();
        connection.close();
    }
}
