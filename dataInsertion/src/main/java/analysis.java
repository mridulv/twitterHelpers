import java.sql.*;
import java.util.Date;

/**
 * Created by mridul.v on 8/13/2014.
 */
public class analysis {
    public static void main(String args[]) throws SQLException, ClassNotFoundException, InterruptedException {

        String dbUrl = "jdbc:mysql://localhost/test";
        String dbClass = "com.mysql.jdbc.Driver";
        String username = "root";
        String password = "";

        Class.forName(dbClass);
        Connection connection = DriverManager.getConnection(dbUrl, username, password);

        Date startDate = new Date();
        String query = "SELECT * FROM tweets WHERE key_val LIKE '783214%' ";
        //String query = "SELECT COUNT(1) FROM tweets";
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        System.out.println(rs.getRow());
        Date endDate = new Date();
        long msElapsedTime = startDate.getTime() - endDate.getTime();

        int count=0;

        while(rs.next()){
            String text = rs.getString("tweet");
            System.out.println(text);
            count++;
        }

        System.out.println(count + " " + msElapsedTime);

    }
}
