import java.sql.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by mridul.v on 9/6/2014.
 */
public class dayTime {
    public static void main(String args[]) throws ClassNotFoundException, SQLException {
        Class.forName(conn.dbClass);
        Connection connection = DriverManager.getConnection(conn.dbUrl, conn.username, conn.password);

        long user_id = 100101l;

        String query = "SELECT * FROM analysis_tweets_new where user_id ="+user_id;

        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        int count = 0;

        while (rs.next()){

        }
    }
}
