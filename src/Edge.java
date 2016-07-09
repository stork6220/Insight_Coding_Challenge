/**
 * Define the edges in the graphs
 * Since it is undirected graph, we can swap the actor and target to make sure two fields follow lexicographical order
 * It is an optimization for payments happened in between same people but with reversed directions
 * This case will not update the graph and degree if they are both in the 60 seconds sliding window
 */
public class Edge {
    String actor;
    String target;
    private final int largePrime = 7919;
    public Edge(String actor, String target) {
            if (actor.compareTo(target) > 0) {
                this.actor = target;
                this.target = actor;
            } else {
                this.actor = actor;
                this.target = target;
            }
        }
        @Override
        public int hashCode() {  // For the HashMap implementation
            return (actor.hashCode() * 31 % largePrime + target.hashCode() + largePrime) % largePrime;
        }
        @Override
        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
        }
        if (!(obj instanceof Edge)) {
            return false;
        }
        Edge another = (Edge) obj;
        return this.actor.equals(another.actor) && this.target.equals(another.target);
    }
}