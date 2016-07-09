import java.util.*;

/**
 * Maintain two TreeSet (Red-Black Tree in Java), and make sure:
 * 1. the left TreeSet size is equal to right size (even count) or right size + 1 (odd count);
 * 2. all elements in the left TreeSet is not larger than any element in the right TreeSet
 * When calling getMedian() method, if the total number of nodes is odd, return the last of left TreeSet
 * If the total number of nodes is even, return the average of (last of left) and (first of right)
 */
public class Calculator {
    private TreeSet<CalcNode> left;
    private TreeSet<CalcNode> right;

    private Map<String, CalcNode> lookupMap; // Optimization: make the lookup time O(1). We can quickly know if the given user already exists

    public Calculator() {
        left = new TreeSet<>();
        right = new TreeSet<>();
        lookupMap = new HashMap<>();
    }

    // If all the relationships in the graph are out of time, reset the degree calculation
    public void clear() {
        left.clear();
        right.clear();
        lookupMap.clear();
    }

    // Adaptor for the MedianDegree class implementation, in which a pair of users (or Edge) were updated together
    public void add(Edge edge) {
        add(edge.actor);
        add(edge.target);
    }

    // Add a new user, if the user already exist, just update the user node's degree and let left and right TreeSet automatically update
    public void add(String user) {
        CalcNode node = lookupMap.get(user);
        if (node == null) {
            node = new CalcNode(user);
            node.setDegree(1);
            lookupMap.put(user, node);
            //make sure left.size() == right.size() or right.size() + 1
            if (left.isEmpty() || node.compareTo(left.last()) <= 0) {
                left.add(node);
            } else {
                right.add(node);
            }
            if (left.size() > right.size() + 1) {
                right.add(left.pollLast());
            } else if (left.size() < right.size()) {
                left.add(right.pollFirst());
            }
        } else {
            node.setDegree(node.getDegree() + 1);
            // Do the following operations otherwise the TreeSet in Java would not update by itself
            if (!left.isEmpty()) {
                left.add(left.pollLast());
            }
            if (!right.isEmpty()) {
                right.add(right.pollFirst());
            }
        }
        if (!left.isEmpty() && !right.isEmpty() && left.last().compareTo(right.first()) > 0) {
            right.add(left.pollLast());
            left.add(right.pollFirst());
        }
    }

    // Adaptor for the MedianDegree class implementation, in which a pair of users (or Edge) were updated together
    public void subtract(Edge edge) {
        subtract(edge.actor);
        subtract(edge.target);
    }

    // Subtract a user degree by 1, if the user degree is more than one, just decrease its count by one
    // Otherwise, the node need to be removed in both TreeSet and HashMap
    public void subtract(String user) {
        CalcNode node = lookupMap.get(user);
        if (node == null) {
            return;
        }
        if (node.getDegree() == 1) {
            //make sure left.size() == right.size() or right.size() + 1
            if (left.remove(node) && left.size() <= right.size()) {
                left.add(right.pollFirst());
            }
            if (right.remove(node) && (left.size() > right.size() + 1)) {
                right.add(left.pollLast());
            }
            lookupMap.remove(user);
        } else {
            node.setDegree(node.getDegree() - 1);
            // Do the following operations otherwise the TreeSet in Java would not update by itself
            if (!left.isEmpty()) {
                left.add(left.pollLast());
            }
            if (!right.isEmpty()) {
                right.add(right.pollFirst());
            }
        }
        if (!left.isEmpty() && !right.isEmpty() && left.last().compareTo(right.first()) > 0) {
            right.add(left.pollLast());
            left.add(right.pollFirst());
        }
    }

    public double getMedian() {
        if (left.isEmpty()) {
            return 0;
        }
        if (left.size() == right.size() + 1) {
            return (double) left.last().getDegree();
        }
        return (left.last().getDegree() + right.first().getDegree()) / 2.0;
    }
}
