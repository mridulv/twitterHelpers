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

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;

/**
 * Created by mridul.v on 9/6/2014.
 */
public class estimateRating {
    public static void main(String args[]) throws SQLException, ClassNotFoundException, TwitterException, IOException {
        Class.forName(conn.dbClass);
        Connection connection = DriverManager.getConnection(conn.dbUrl, conn.username, conn.password);

        InputStream modelIn = new FileInputStream("C:\\Users\\mridul.v\\Downloads\\twitter_Project\\en-token.bin");
        TokenizerModel model = new TokenizerModel(modelIn);
        Tokenizer tokenizer = new TokenizerME(model);

        InputStream modelIn2 = new FileInputStream("C:\\Users\\mridul.v\\Downloads\\twitter_Project\\en-pos-maxent.bin");
        POSModel model2 = new POSModel(modelIn2);
        POSTaggerME tagger = new POSTaggerME(model2);

        String entity = "justinbieber";
        String text = "Watch @FloydMayweather fight on Showtime PPV tomorrow night for #Mayhem.";

        Estimate estimate = new Estimate();
        Pair<Double, Pair<Double, Double>> pairPair = estimate.getRetweetCount(connection,tokenizer,tagger , text);

        Twitter twitter = new TwitterFactory().getInstance();
        User user = twitter.showUser(entity);
        long followers = user.getFollowersCount();

        double c1 = 1.02903521e-03;
        double c2 = 1.96047162e+00;
        double c3 = 2.42172591e+00;
        double c4 = -5.12693532e-01;

        double estimateRating = c1*followers + c2*pairPair.getKey() + c3*pairPair.getValue().getKey() + c4*pairPair.getValue().getValue();

        System.out.println(estimateRating);
    }
}
