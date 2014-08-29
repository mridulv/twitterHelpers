import com.aliasi.classify.ConditionalClassification;
import com.aliasi.classify.LMClassifier;
import com.aliasi.util.AbstractExternalizable;

import java.io.File;
import java.io.IOException;

/**
 * Created by mridul.v on 8/11/2014.
 */
public class SentimentClassifier {
    String[] categories;
    LMClassifier lmClassifier;
    public SentimentClassifier() throws IOException, ClassNotFoundException {
        lmClassifier = (LMClassifier) AbstractExternalizable.readObject(new File("C:\\Users\\mridul.v\\Downloads\\classifier.txt"));
        categories = lmClassifier.categories();
    }

    public String classify(String text) {
        ConditionalClassification classification = lmClassifier.classify(text);
        return classification.bestCategory();
    }
}
