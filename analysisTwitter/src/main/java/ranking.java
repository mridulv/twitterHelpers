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

        String query = "SELECT * FROM analysis_tweets_new where key_val LIKE '1%'";
        Statement stmt = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
        ResultSet rs = stmt.executeQuery(query);
        int count = 0;
        while (rs.next()){
            String text = rs.getString("tweet");
            Matcher matcher = Pattern.compile("@\\s*(\\w+)").matcher(text);
            if (matcher.find()) {
                rs.updateString("mention_id",matcher.group(1));
            }
            rs.updateRow();
            if (count % 1000 == 0)
                System.out.println(count);

            count++;
        }
    }
}
