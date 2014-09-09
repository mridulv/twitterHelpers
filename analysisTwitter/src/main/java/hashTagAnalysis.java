import java.sql.*;
import java.sql.Date;
import java.util.*;

/**
 * Created by mridul.v on 9/8/2014.
 */
public class hashTagAnalysis {
    public static void main(String args[]) throws SQLException, ClassNotFoundException {
        Class.forName(conn.dbClass);
        Connection connection = DriverManager.getConnection(conn.dbUrl, conn.username, conn.password);

        java.util.Date startDate = new java.util.Date();

        String hash = "#iphone";
        String query = "SELECT * FROM analysis_tweets_new where tweet LIKE '%"+hash+"%'";

        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(query);

        int count = 0;
        while (rs.next()) {
            System.out.println(rs.getString("tweet"));
            count++;
        }

        java.util.Date endDate = new java.util.Date();

        System.out.println(count);
        long msElapsedTime = startDate.getTime() - endDate.getTime();
        System.out.println(msElapsedTime);
    }
}
