import javafx.util.Pair;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;

import java.io.*;
import java.net.URISyntaxException;
import java.sql.*;

/**
 * Created by mridul.v on 9/6/2014.
 */
public class estimateRatingNorm {
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

        String query = "SELECT * FROM final_tweet_analysis where lang LIKE 'en' ";
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(query);

        int count = 0;
        int count2 = 0;

        while (rs.next()) {

            String query2 = "select * from analysis_tweets_group where user_id = "+rs.getLong("user_id");
            Statement stmt2 = connection.createStatement();
            ResultSet rs2 = stmt2.executeQuery(query2);

            rs2.next();

            double rating_avg = rs2.getDouble("rating");
            if (rs2.getInt("total") > 1) {

                double followers = rs.getLong("followers");
                String text = rs.getString("tweet");
                double rating = rs.getDouble("rating");

                if (count % 1000 == 0) {
                    System.out.println(count + " " + count2);
                }

                Estimate estimate = new Estimate();
                Pair<Double, Pair<Double, Double>> pairPair = estimate.getRetweetCount(connection, tokenizer, tagger, text);

                double c1 = -3.32185595e-06;
                double c2 = -1.17079629e-03;
                double c3 =  3.08005051e-03;
                double c4 =  1.88577386e-03;

                double estimateRating = c1 * followers + c2 * pairPair.getKey() + c3 * pairPair.getValue().getKey() + c4 * pairPair.getValue().getValue();

                bw.write(String.valueOf(estimateRating));
                bw.write(",");
                bw.write(String.valueOf(rating));
                bw.write("\n");

                double true_rating = rating_avg*(1+(estimateRating/100));
                if (Math.abs(true_rating - rating) > 0.2 * (rating))
                    count2++;
                count++;
            }
        }
        System.out.println(count2);
        bw.close();
    }
}
