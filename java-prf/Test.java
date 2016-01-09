import java.util.logging.Logger;
import java.util.logging.Level;
import lemurproject.indri.*;

public class Test {
    private final static Logger LOGGER = Logger.getLogger(Main.class.getName());
    private final static String INDEX_PATH = "../indices/tu-no-expansion-no-stop";

    public static void main(String[] args) throws Exception {
        LOGGER.setLevel(Level.ALL);
        QueryEnvironment env = new QueryEnvironment();
        env.addIndex(INDEX_PATH);
        LOGGER.fine("Opened index");

        QueryRequest req = new QueryRequest();
        req.query = "semantiek betekenisleer";
        req.resultsRequested = 3;
        req.startNum = 0; //starting number in the result set, eg 10 to get results starting from the 11th position in the result list
        req.options = req.TextSnippet; //alternative one is TextSnippet
        QueryResult[] rets = env.runQuery(req).results;
        // iterate over the results.
        for(QueryResult ret : rets)
            System.out.println(
                //ret.docid + " " + // internal ID
                ret.documentName + " " +
                ret.score + " " +
                ret.snippet //.replace("\n", "").replace("\r", "") // add this to remove the newline...
            );
    }
}
