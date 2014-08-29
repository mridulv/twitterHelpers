/**
 * Created by mridul.v on 8/19/2014.
 */
import java.io.IOException;
import com.mongodb.DBObject;
import java.net.URISyntaxException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import com.mongodb.util.JSON;

import twitter4j.*;

public class Producer implements Runnable{

    protected ArrayBlockingQueue<Status> queue;

    public Producer(ArrayBlockingQueue<Status> queue) {
        this.queue = queue;
    }

    public void run() {
            StatusListener listener = new StatusListener() {

                public void onStatus(Status status) {
                    queue.add(status);
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
            FilterQuery filter = new FilterQuery();
            String[] lang = {"en"};
            String[] keywordsArray = { "iphone", "samsung" };
            filter.language(lang);
            filter.track(keywordsArray);
            twitterStream.filter(filter);
    }
}
