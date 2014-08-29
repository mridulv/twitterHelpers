import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by mridul.v on 8/20/2014.
 */
public class timeStamp {
    public static void main(String args[]) throws ClassNotFoundException, ParseException, SQLException {
        // This file for converting the timestamp field to seconds
        String dbUrl = "jdbc:mysql://localhost/test";
        String dbClass = "com.mysql.jdbc.Driver";
        String username = "root";
        String password = "";

        Class.forName(dbClass);
        Connection connection = DriverManager.getConnection(dbUrl, username, password);
        String query = "SELECT * FROM analysis_tweets";

        Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
        ResultSet rs = statement.executeQuery(query);

        Date startDate = new Date();
        while(rs.next()){
            String text = rs.getString("timestamp");
            int index = rs.findColumn("seconds");

            String str_date[] = text.split(" ");
            String strDate = str_date[5] + str_date[1] + str_date[2];
            String strTime = str_date[3];

            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMMdd");
            Date s = formatter.parse(strDate);

            SimpleDateFormat timeformatter = new SimpleDateFormat("HH:mm:ss");
            Date t = timeformatter.parse(strTime);

            long secs = s.getTime()/1000 + t.getTime()/1000;

            System.out.println(secs + " " + s.getTime()/1000 + " " + t.getTime()/1000);
            rs.updateLong(index,secs);
            rs.updateRow();
            System.out.println(text + " " + strDate + " " + strTime);
        }

        Date endDate = new Date();
        long msElapsedTime = startDate.getTime() - endDate.getTime();
        System.out.println(msElapsedTime);

        //statement.executeUpdate(query);

    }
}
