import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Read the payment from the JSON file
 * JSON library java-json.jar was used for the JSONObject
 * It was be downloaded from http://www.java2s.com/Code/JarDownload/java-json/java-json.jar.zip
 * The input file is a JSON file, in which each line is a JSONObject
 * PaymentReader return a list of JSONObject
 * Invalid data had also been discarded in the pre processing
 */
public class PaymentReader {
    public final String path;
    private List<List<String>> list;
    int index = 0;
    public PaymentReader(String path) {
        this.path = path;
        list = new ArrayList<>();
        String line = null;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(path));
            int index = 0;
            while ((line = reader.readLine()) != null) {
                JSONObject json = new JSONObject(line);
                if (json.has("actor") && json.has("target") && json.has("created_time")) {
                    String actor = (String) json.get("actor");
                    String target = (String) json.getString("target");
                    String createdTime = (String) json.get("created_time");
                    createdTime = createdTime.replace("T", " ").replace("Z", "");
                    if (actor.equals("") || target.equals("") || createdTime.equals("")) { // clean up the invalid data
                        continue;
                    }
                    List<String> output = Arrays.asList(actor, target, createdTime);
                    list.add(output);
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public boolean hasNext() {
        return index < list.size();
    }

    public List<String> next() {
        if (hasNext()) {
            return list.get(index++);
        }
        return null;
    }
}
