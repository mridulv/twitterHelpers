import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by mridul.v on 9/9/2014.
 */
public class setTime {
    public static void main(String args[]) throws ParseException, ClassNotFoundException, SQLException {

        Map<String, Integer> hashMap = new HashMap<String, Integer>();
        hashMap.put("Jan", 1);
        hashMap.put("Feb", 2);
        hashMap.put("Mar", 3);
        hashMap.put("Apr", 4);
        hashMap.put("May", 5);
        hashMap.put("Jun", 6);
        hashMap.put("Jul", 7);
        hashMap.put("Aug", 8);
        hashMap.put("Sep", 9);
        hashMap.put("Oct", 10);
        hashMap.put("Nov", 11);
        hashMap.put("Dec", 12);

        Class.forName(conn.dbClass);
        Connection connection = DriverManager.getConnection(conn.dbUrl, conn.username, conn.password);

        String query = "SELECT * from analysis_tweets_new";
        Statement stmt = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
        ResultSet rs = stmt.executeQuery(query);

        int count = 0;

        while (rs.next()) {
            String s = rs.getString("timestamp");
            String arr[] = s.split(" ");
            String construct = arr[5] + "-" + hashMap.get(arr[1]) + "-" + arr[2] + " " + arr[3];
            Timestamp t = Timestamp.valueOf(construct);

            rs.updateLong("seconds", t.getTime() / 1000);
            rs.updateRow();

            if (count %1000 == 0)
                System.out.println(count);
            count++;
        }
    }
}
