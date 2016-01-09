import java.util.logging.Logger;
import java.util.logging.Level;

import java.util.Collections;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class TrecReader {
    private final static Logger LOGGER = Logger.getLogger(TrecReader.class.getName());

    public static Map<String, QueryResult[]> read(String path) throws IOException {
        LOGGER.setLevel(Level.FINE);
        LOGGER.info("Reading trec results from" + path);
        BufferedReader bufferedReader = new BufferedReader(new FileReader(path));
        Map<String, List<QueryResult>> building = new HashMap<>();
        String line;
        QueryResult currentResult;
        String queryID;
        List<QueryResult> currentQueryResults;
        while ((line = bufferedReader.readLine()) != null) {
            line = line.trim();
            LOGGER.finest(line);

            // Skip comments
            if (line.charAt(0) == '#') {
                // Skip all indented lines
                while ((line = bufferedReader.readLine()) != null) {
                    if (!line.split("\\s+")[0].equals(""))
                        break;
                }
                if (line == null)
                    break;
            }

            currentResult = new QueryResult(line);
            queryID = currentResult.getQueryID();
            if ((currentQueryResults = building.get(queryID)) == null) {
                // The query didn't exist yet
                currentQueryResults = new ArrayList<>();
                building.put(queryID, currentQueryResults);
            }
            currentQueryResults.add(currentResult);
        }
        bufferedReader.close();
        LOGGER.fine("Read " + building.size() + " queries");

        // Fixate lists
        Map<String, QueryResult[]> results = new HashMap<>(building.size());
        for (Map.Entry<String, List<QueryResult>> entry : building.entrySet()) {
            currentQueryResults = entry.getValue();
            Collections.sort(currentQueryResults);
            results.put(
                entry.getKey(),
                currentQueryResults.toArray(new QueryResult[currentQueryResults.size()])
            );
        }
        return results;
    }
}
