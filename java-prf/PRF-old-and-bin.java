import java.util.logging.Logger;
// import java.util.logging.Level;
// import java.util.logging.ConsoleHandler;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import org.apache.commons.math3.linear.RealVector;
import lemurproject.indri.QueryRequest;
import lemurproject.indri.QueryEnvironment;

// import QueryReader;
// import DocumentVector;

class PRF {
    private final static Logger LOGGER = Logger.getLogger(PRF.class.getName());
    // read the queries
    // for each query:
    //   run the query
    //   for the top k documents:
    //     create tf-idf vector according to the book
    //   tilt the vector according to the book paper
    // save the output

    public static void main(String[] args) throws Exception {
        // LOGGER.setLevel(Level.ALL);
        LOGGER.info("Hello World!");
        LOGGER.fine("Command line arguments: " + Arrays.toString(args));

        // Get queries
        String queryPath = args[0];
        int documentK = 3;
        List<QueryRequest> queries = QueryReader.read(queryPath, documentK);

        // Open index
        String indexPath = args[1];
        QueryEnvironment queryEnv = openIndex(indexPath);

        // Expand queries
        LOGGER.info("Expanding queries");
        List<DocumentVector> expandedQueries = new ArrayList<DocumentVector>();
        for (QueryRequest query : queries) {
            expandedQueries.add(expandQuery(queryEnv, query));
        }

        // Save expanded queries (preferably in ldf format)
    }

    private static QueryEnvironment openIndex(String path) throws Exception {
        LOGGER.info("Opening index");
        QueryEnvironment queryEnv = new QueryEnvironment();
        queryEnv.addIndex(path);
        return queryEnv;
    }

    //   run the query
    //   for the top k documents:
    //     create tf-idf vector according to the book
    //   tilt the vector according to the book paper
    private static DocumentVector expandQuery(
                QueryEnvironment env,
                QueryRequest query
            ) {
        LOGGER.finer("Expanding \"" + query.query + "\"");
        return new DocumentVector();
    }
}

    // public static Map<String, RealVector> read(String path, String[] vocabulary)
    //         throws SAXException, IOException, ParserConfigurationException {
    //     LOGGER.info("Reading queries");
    //     Map<String, RealVector> queries = new HashMap<>();
    //     // <QueryRequest> queries = new ArrayList<QueryRequest>();
    //     // // for line in file read blabla

    //     Document dom = parseXmlFile(path);
    //     // get the root elememt
    //     // get a node list of <query> elements
    //     NodeList nodeList = dom.getDocumentElement().getElementsByTagName("query");
    //     if(nodeList != null && nodeList.getLength() > 0) {
    //         for(int i = 0 ; i < nodeList.getLength();i++) {
    //             Element query = (Element) nodeList.item(i);
    //             String id = getTextValue(query, "number");
    //             String query_text = getTextValue(query, "text");
    //             RealVector vector = createUnitVector(query_text, vocabulary);

    //             queries.put(id, vector);
    //             // //add it to list
    //             // myEmpls.add(e);
    //         }
    //     }

        // // example
        // QueryRequest query = new QueryRequest();
        // query.options = query.TextSnippet; //alternative one is TextSnippet
        // query.query = "semantiek betekenisleer";
        // query.resultsRequested = documentK;
        // query.startNum = 0; //starting number in the result set, eg 10 to get results starting from the 11th position in the result list
        // queries.add(query);

    //     LOGGER.finer("Queries returned: " + queries.toString());
    //     return queries;
    // }

    // public static List<QueryRequest> readOLD(String path, int documentK) {
    //     LOGGER.info("Reading queries");
    //     List<QueryRequest> queries = new ArrayList<QueryRequest>();
    //     // for line in file read blabla

    //     // example
    //     QueryRequest query = new QueryRequest();
    //     query.options = query.TextSnippet; //alternative one is TextSnippet
    //     query.query = "semantiek betekenisleer";
    //     query.resultsRequested = documentK;
    //     query.startNum = 0; //starting number in the result set, eg 10 to get results starting from the 11th position in the result list
    //     queries.add(query);

    //     LOGGER.finer("Queries returned: " + queries.toString());
    //     return queries;
    // }
