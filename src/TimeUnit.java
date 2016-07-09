import java.util.HashSet;
import java.util.Set;

/**
 * Define the data in each time unit of the 60 seconds circular array
 * Each time unit can hold multiple edges (or relationships) because multiple payments happen at the same time
 */
public class TimeUnit {
    public Set<Edge> children;
    public TimeUnit() {
        children = new HashSet<>();
    }
}