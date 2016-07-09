import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Maintained a size 60 circular array to record the information in the latest 60 seconds
 * The graph (or circular array) and the median calculations are done on the fly with the data stream
 * Kept a count map to record the frequency of the each relationship (represented by Edge class here).
 * Only update the Circular Array and the median and graph when there is a new relationship or all Edges are all out of time 60s
 */
public class MedianDegree {
    // store the graph using a circular array in the last 60 seconds
    TimeUnit[] graph;

    // store all the edges in the 60s sliding window and also kept their count.
    // median degrees only update when the key (edge) was add and removed
    Map<Edge, Integer> countMap;

    // tail's position
    private long prevTime;

    // self defined data structure for the median calculation
    Calculator calculator;

    PaymentReader dataStream; // read in the data stream from the JSON text file
    public final String path;

    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");

    public MedianDegree(String inPath, String outPath) {
        graph = new TimeUnit[60];
        countMap = new HashMap<>();
        prevTime = 0;
        calculator = new Calculator();
        dataStream = new PaymentReader(inPath);
        this.path = outPath;
    }

    // Read in a new record and update the graph and the median degree if necessary
    public void record(List<String> input) {
        String actor = input.get(0);
        String target = input.get(1);
        String createdTime = input.get(2);
        Date date = null;
        try {
            date = simpleDateFormat.parse(createdTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long curTime = date.getTime() / 1000;
        Edge edge = new Edge(actor, target);
        if (curTime > prevTime) {
            updateCircularArray(curTime); // if curTime moves forward, need remove all old edges which are time out
        }
        if (curTime > prevTime || curTime - prevTime > -60) {  // need also deal with the record happened before current time but within 60 seconds
            int index = (int) (curTime % 60);
            if (graph[index] == null) {
                graph[index] = new TimeUnit();
            }
            graph[index].children.add(edge);
            updateCountMap(edge);
        }
        prevTime = Math.max(prevTime, curTime);
    }

    // Update the count map
    private void updateCountMap(Edge edge) {
        Integer count = countMap.get(edge);
        if (count == null) {
            count = 0;
            calculator.add(edge); // update the median degree if new edge comes
        }
        countMap.put(edge, count + 1);
    }

    public void updateCircularArray(long curTime) {
        if (curTime - prevTime >= 60) { // Optimization for case when all the graph is out of time
            Arrays.fill(graph, null);
            calculator.clear();
            countMap.clear();
            prevTime = curTime;
        } else {
            for (long t = prevTime + 1; t <= curTime; t++) {
                int index = (int) (t % 60);
                if (graph[index] == null) {
                    continue;
                }
                for (Edge edge : graph[index].children) {
                    Integer count = countMap.get(edge);
                    if (count != null && count > 1) {
                        countMap.put(edge, count - 1);
                    } else {
                        countMap.remove(edge);
                        calculator.subtract(edge); /// update the median degree if no edge is left
                    }
                }
                graph[index] = null;
            }
        }
    }

    // Simulate the stream operations and update the graph and median degree on the fly
    public void simulator() {
        try {
            // Set up the system out to the file
            PrintStream out = new PrintStream(new FileOutputStream(path));
            System.setOut(out);
            while (dataStream.hasNext()) {
                record(dataStream.next());
                System.out.println(String.format("%.2f", calculator.getMedian()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        String inPath = "venmo_input/venmo-trans.txt";
        String outPath = "venmo_output/output.txt";
        if (args.length == 2) {
            inPath = args[0];
            outPath = args[1];
            System.out.println("Input path: " + args[0] + " Output path: " + args[1]);
        }
        MedianDegree sol = new MedianDegree(inPath, outPath);
        sol.simulator();
    }
}
