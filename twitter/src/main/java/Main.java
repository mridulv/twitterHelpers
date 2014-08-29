import com.mongodb.DBObject;
import twitter4j.*;
import com.mongodb.util.JSON;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;

public class Main {

    public static void main(String[] args) throws IOException, ClassNotFoundException {

            final SentimentClassifier sentimentClassifier = new SentimentClassifier();
            final Sentiment sentiment = new Sentiment();
            final ContentAnalysis contentAnalysis = new ContentAnalysis();

            NLP.init();

            StatusListener listener = new StatusListener() {
                public void onStatus(Status status) {
                    //int sentiment = 0;
                    String tweet_json = TwitterObjectFactory.getRawJSON(status);
                    System.out.println(status.getGeoLocation());
                    DBObject doc = (DBObject) JSON.parse(tweet_json);
                    if(status.getText() != null) {
                        System.out.println(status.getText().replaceAll(" http.*?\\s", " ").replaceAll("[^\\w\\s\\,]","").replaceAll(","," ").replaceAll("http\\s*(\\w+)",""));
                        // These two features have been added namely : Sentiment and contentAnalysis
                        System.out.println(status);
                        System.out.println("sentiment is " + NLP.findSentiment(status.getText().replaceAll(" http.*?\\s", " ").replaceAll("[^\\w\\s\\,]", "").replaceAll(",", " ").replaceAll("http\\s*(\\w+)", "")));
                        //sentiment.getSentiment(status.getText().replaceAll(" http.*?\\s", " ").replaceAll("[^\\w\\s\\,]","").replaceAll(","," ").replaceAll("http\\s*(\\w+)",""));
                        //contentAnalysis.getContent(status.getText().replaceAll(" http.*?\\s", " ").replaceAll("[^\\w\\s\\,]","").replaceAll(","," ").replaceAll("http\\s*(\\w+)",""));
                    }
                }

                public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
                }

                public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
                }

                @Override
                public void onScrubGeo(long l, long l2) {

                }

                @Override
                public void onStallWarning(StallWarning stallWarning) {

                }

                public void onException(Exception ex) {
                    ex.printStackTrace();
                }
            };

            TwitterStream twitterStream = new TwitterStreamFactory().getInstance();
            twitterStream.addListener(listener);
            // sample() method internally creates a thread which manipulates TwitterStream and calls these adequate listener methods continuously.
            FilterQuery filter = new FilterQuery();
            String[] lang = {"en"};
            String[] keywordsArray = { "@google" };
            filter.language(lang);
            filter.track(keywordsArray);
            twitterStream.filter(filter);


    }

}
