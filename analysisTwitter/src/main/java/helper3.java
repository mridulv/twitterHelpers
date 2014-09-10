import java.util.Comparator;
import java.util.Map;

/**
 * Created by mridul.v on 9/10/2014.
 */
class helper3 implements Comparator<String> {

    Map<String, Double> base;
    public helper3(Map<String, Double> base) {
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