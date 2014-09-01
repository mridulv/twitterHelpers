/**
 * Created by mridul.v on 9/1/2014.
 */
import java.util.Comparator;
import java.util.Map;

public class helper implements Comparator<String> {

    Map<String, Integer> base;
    public helper(Map<String, Integer> base) {
        this.base = base;
    }

    // Note: this comparator imposes orderings that are inconsistent with equals.
    public int compare(String a, String b) {
        if (base.get(a) >= base.get(b)) {
            return -1;
        } else
            return 1;
    }
}