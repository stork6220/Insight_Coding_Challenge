/**
 * (This is a helper class for the Calculator)
 * The graph node class that includes the user name and the degrees of the use's relationship
 * The comparator for the graph node had been overrided by first comparing the degrees and then the user name
 * The reason for using user name is to deduplicate as many users can have the same degree
 */
public class CalcNode implements Comparable<CalcNode> {
    public final String user;
    private int degree;
    public CalcNode(String user) {
        this.user = user;
        degree = 0;
    }
    public int getDegree() {
        return degree;
    }
    public void setDegree(int newDegree) {
        degree = newDegree;
    }
    @Override
    public int compareTo(CalcNode another) {
        if (degree == another.degree) {
            return user.compareTo(another.user);
        }
        return this.degree < another.degree ? -1 : 1;
    }
}