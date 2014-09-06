import java.sql.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by mridul.v on 9/6/2014.
 */
public class ranking {
    public static void main(String args[]) throws ClassNotFoundException, SQLException {
        Class.forName(conn.dbClass);
        Connection connection = DriverManager.getConnection(conn.dbUrl, conn.username, conn.password);

        long user_id = 12534;

        String query = "SELECT mention_id,avg(rating) as total FROM analysis_tweets_new where key_val LIKE '1%' GROUP BY mention_id ORDER BY total DESC";
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        int count = 0;
        int rank = 0;

        String user = "MLStadium";

        System.out.println(count);
        while (rs.next()){
            count++;
            if (rs.getString("mention_id").equals(user)){
                rank = count;
            }
        }
        System.out.println(rank);
        System.out.println(count);
    }
}
