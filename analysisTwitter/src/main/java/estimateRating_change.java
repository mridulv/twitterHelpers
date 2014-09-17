import javafx.util.Pair;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import twitter4j.*;

import java.io.*;
import java.net.URISyntaxException;
import java.sql.*;

/**
 * Created by mridul.v on 9/6/2014.
 */
public class estimateRating_change {
    public static void main(String args[]) throws SQLException, ClassNotFoundException, TwitterException, IOException, URISyntaxException {
        Class.forName(conn.dbClass);
        Connection connection = DriverManager.getConnection(conn.dbUrl, conn.username, conn.password);

        InputStream modelIn = new FileInputStream("C:\\Users\\mridul.v\\Downloads\\twitter_Project\\en-token.bin");
        TokenizerModel model = new TokenizerModel(modelIn);
        Tokenizer tokenizer = new TokenizerME(model);

        InputStream modelIn2 = new FileInputStream("C:\\Users\\mridul.v\\Downloads\\twitter_Project\\en-pos-maxent.bin");
        POSModel model2 = new POSModel(modelIn2);
        POSTaggerME tagger = new POSTaggerME(model2);

        File file = new File("C:\\Users\\mridul.v\\Downloads\\twitter_Project\\data_estimate.csv");
        if (!file.exists()) {
            file.createNewFile();
        }
        FileWriter fw = new FileWriter(file.getAbsoluteFile());
        BufferedWriter bw = new BufferedWriter(fw);

        long user_id = 140195719;
        String query = "SELECT * FROM analysis_tweets_new where user_id = 140195719";
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(query);

        int count = 0;
        int count2 = 0;

        while (rs.next()) {

            double rating_avg = 121;
            int total = 1;
            double avg = 0;
            Twitter twitter = new TwitterFactory().getInstance();
            User user = twitter.showUser(user_id);
            Query querys = new Query("from:"+user);
            querys.setCount(20);
            try {
                QueryResult result = twitter.search(querys);
                System.out.println("Count : " + result.getTweets().size()) ;
                for (Status tweet : result.getTweets()) {
                    System.out.println("text : " + tweet.getText());
                    avg = avg + tweet.getRetweetCount();
                    total++;
                }
            } catch (TwitterException e) {
                e.printStackTrace();
            }

            System.out.println(total + "  " + avg/total);
            double followers = rs.getLong("followers");
            String text = "Thank you for getting #ShotsTo600k on here! #shotties";
            double rating = rs.getDouble("rating");

            if (count % 1000 == 0) {
                System.out.println(count + " " + count2);
            }

            Estimate estimate = new Estimate();
            Pair<Double, Pair<Double, Double>> pairPair = estimate.getRetweetCount(connection, tokenizer, tagger, text);

            double c1 = 1.27037327e-05;
            double c2 = -1.80483354e-03;
            double c3 = -1.60152027e-03;
            double c4 = -1.01518616e-02;

            double estimateRating = c1 * followers + c2 * pairPair.getKey() + c3 * pairPair.getValue().getKey() + c4 * pairPair.getValue().getValue();
            System.out.println(((estimateRating/100)+1)*rating_avg + "   " + rs.getString("rating"));

            double true_rating = rating_avg;
            if (Math.abs(true_rating - rating) > 0.2 * (rating))
                count2++;
            count++;
        }
        System.out.println(count2);
        bw.close();
    }
}
