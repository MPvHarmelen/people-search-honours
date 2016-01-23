import java.lang.Math;

import java.util.logging.Logger;

import java.util.Map;
import java.util.HashMap;
// import java.util.List;
// import java.util.ArrayList;
import java.util.Arrays;

import java.io.IOException;

import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.ArrayRealVector;

import lemurproject.lemur.Index;
import lemurproject.lemur.IndexManager;
import lemurproject.lemur.TermInfoList;
import lemurproject.lemur.TermInfo;

// import ListFileReader;
// import QueryReader;
// import DocumentVector;
// import TrecReader;

class PRF {
    private final static Logger LOGGER = Logger.getLogger(PRF.class.getName());

    // Using prf one can expand a query with a certain number of terms that is
    // independent of the number of terms in the query. This method would be
    // called "adding wordk".
    // The other option is to add wordk terms for each term in the query,
    // effectively multiplying the number of terms in the query by wordk.

    // for every query:
    //   read a batch of k documents (verify the query id's and skip until different)
    //   transform them into tf-idf vectors
    //   do some calculations
    // save everything
    public static void main(String[] args) throws Exception {
        // LOGGER.setLevel(Level.ALL);
        LOGGER.fine("Command line arguments: " + Arrays.toString(args));

        // Read arguments
        // String vocabularyPath = args[];
        String queryPath = args[0];
        int documentK = Integer.parseInt(args[1]);
        int termK = Integer.parseInt(args[2]);
        boolean multiplyByWordk = Boolean.parseBoolean(args[3]);
        String resultsPath = args[4];
        String indexPath = args[5];
        String outputPath = args[6];

        // Get vocabulary
        // String[] vocabularyArray = ListFileReader.read(vocabularyPath);
        // Map<String, Integer> vocabulary = reverseVocabulary(vocabularyArray);

        // Open index
        Index index = openIndex(indexPath);

        // Get queries
        QueryReader queryReader = new QueryReader(queryPath, index);

        // Get retrieval results
        Map<String, QueryResult[]> queryResults = TrecReader.read(resultsPath);

        // Open output file
        LDFWriter outputWriter = new LDFWriter(outputPath, index);

        // // Expand queries
        // LOGGER.info("Expanding queries");
        // List<DocumentVector> expandedQueries = new ArrayList<DocumentVector>();
        // for (QueryRequest query : queries) {
        //     expandedQueries.add(expandQuery(index, query));
        // }

        RealVector idfVector = calculateIDFVector(index);

        LOGGER.info("Expanding queries");
        QueryWithID query;
        while ((query = queryReader.readQueryVector()) != null) {
            // if ("1481".equals(query.getID())) {
            outputWriter.writeTerms(
                expandQuery(
                    index,
                    query,
                    documentK,
                    termK,
                    multiplyByWordk,
                    queryResults,
                    idfVector
                )
            );
            // break;
            // }
        }

        // Save expanded queries (preferably in ldf format)
        LOGGER.info("Saving output file");
        outputWriter.close();
    }

    private static Index openIndex(String path) throws Exception {
        LOGGER.info("Opening index");
        Index index = IndexManager.openIndex(path);
        return index;
    }

    //   run the query
    //   for the top k documents:
    //     create tf-idf vector according to the book
    //   <s>tilt the vector according to the book paper</s>
    //   add termK best words to the query terms
    private static QueryWithID expandQuery(
                Index index,
                QueryWithID query,
                int documentK,
                int termK,
                boolean multiplyByWordk,
                Map<String, QueryResult[]> queryResults,
                RealVector idfVector
            ) throws Exception {
        LOGGER.finer("Expanding " + query.getID());
        QueryResult[] topDocuments = queryResults.get(query.getID());

        if (topDocuments == null) {
            // throw new Exception("No results were found for query " + query.getID());
            LOGGER.warning("No results were found for query " + query.getID());
            return query;
        } else if (topDocuments.length < documentK) {
            // throw new Exception(
            LOGGER.warning(
                "Query " + query.getID() + " can't be expanded using " +
                documentK + " documents because it only has " +
                topDocuments.length + " result(s)."
            );
            return query;
        }

        // tf sum
        RealVector vector = createDocumentVector(topDocuments[0].getDocumentID(), index);
        for (int i=1; i < documentK; i++) {
            vector = vector.add(createDocumentVector(topDocuments[i].getDocumentID(), index));
        }

        // idf
        vector = vector.ebeMultiply(idfVector);
        // Sort terms and choose top termK that aren't in the query
        if (multiplyByWordk) {
            termK *= query.getTerms().length;
            LOGGER.finer("Expanding with " + termK + " terms.");
        }
        String[] extra_words = getTopWords(query, vector, index, termK);
        query.addTerms(extra_words);
        return query;
    }

    /**
     * Get the top words that weren't already in the query
     *
     * @param  query     the original query
     * @param  vector    the document vector
     * @param  index     the indri index
     * @param  termK     the number of terms to be returned
     *
     * @return           the top K terms
     *
     * @throws Exception
     */
    private static String[] getTopWords(QueryWithID query, RealVector vector,
            Index index, int termK) throws Exception {
        // Sort: we want the highest weights to be in firstTerm;
        LinkedWeightedTerm currentTerm;
        LinkedWeightedTerm previouslyComparedTerm;
        LinkedWeightedTerm comparingTerm;
        double currentWeight;
        int linkIndex;
        int numberOfTermsToSort = termK + query.getTerms().length;
        LOGGER.finer("Number of terms to sort: " + numberOfTermsToSort);
        LinkedWeightedTerm firstTerm = new LinkedWeightedTerm(index.term(1), vector.getEntry(0));
        int debugCount = 1;
        for (int tIndex=1; tIndex < index.termCountUnique(); tIndex++) {
            currentWeight = vector.getEntry(tIndex);
            // There are many terms that don't exist in a document, so it's
            // worth checking this for performance.
            if (currentWeight == 0)
                continue;
            debugCount ++;
            currentTerm = new LinkedWeightedTerm(index.term(tIndex + 1), currentWeight);

            // If this term has a higher weight, it should be added _before_
            // the first term. This should be handled separately because there
            // is no "insertBefore" in a single linked list.
            if (currentWeight > firstTerm.getWeight()) {
                currentTerm.setNext(firstTerm);
                firstTerm = currentTerm;
            } else {
                previouslyComparedTerm = firstTerm;
                comparingTerm = firstTerm.getNext();
                // Link index makes it mutch faster:
                // if the weight isn't higher than the top K terms, just don't
                // bother figuring out where this term should go.
                linkIndex = 1;
                while (comparingTerm != null &&
                       linkIndex < numberOfTermsToSort &&
                       currentWeight < comparingTerm.getWeight()) {
                    previouslyComparedTerm = comparingTerm;
                    comparingTerm = comparingTerm.getNext();
                    linkIndex ++;
                }
                // if linkIndex == numberOfTermsToSort, this term should be
                // ignored because we already have K terms with a higher weight
                // than this term
                if (linkIndex != numberOfTermsToSort) {
                    // now currentTerm.getWeight() => comparingTerm.getWeight()
                    // and currentTerm.getWeight() < previouslyComparedTerm.getWeight()
                    // so current should be placed in between previouslyComparedTerm
                    // and comparingTerm.
                    previouslyComparedTerm.insertAfter(currentTerm);
                }
            }
        }
        LOGGER.finer("Number of terms with non zero score: " + debugCount);
        debugCount = 1;
        currentTerm = firstTerm;
        while ((currentTerm = currentTerm.getNext()) != null) {
            debugCount++;
        }
        LOGGER.finer("LinkedWeightedTerm count: " + debugCount);

        String[] topTerms =  new String[termK];
        String[] queryTerms = query.getTerms();
        int iTop=0;
        currentTerm = firstTerm;
        topLoop: while (iTop < topTerms.length) {
            LOGGER.finest("Current top terms: " + Arrays.toString(topTerms));
            if (currentTerm == null) {
                // Apparently there weren't enough terms with non zero
                // score in the top docK documents :(
                LOGGER.warning("Stopped expanding " + query.getID() +
                               " because there weren't enough terms (" + iTop
                               + " < " + termK + ")");
                String[] fewerTopTerms = new String[iTop];
                System.arraycopy(topTerms, 0, fewerTopTerms, 0, iTop);
                topTerms = fewerTopTerms;
                break;
            }
            LOGGER.finest("Current term: " + currentTerm.getTerm());
            topTerms[iTop] = currentTerm.getTerm();
            currentTerm = currentTerm.getNext();
            for (int i=0; i < queryTerms.length; i++) {
                if (queryTerms[i].equals(topTerms[iTop])) {
                    // This top term was already in the query, so we want to
                    // replace it during the next iteration.
                    LOGGER.finest("Rejected!");
                    continue topLoop;
                }
            }
            iTop ++;
        }
        LOGGER.finer("Top K terms: " + Arrays.toString(topTerms));
        return topTerms;
    }


    /**
     * Create a tf vector for a document (no idf is done!)
     *
     * A more general note: watch out using index.term(String) as an index:
     * 0 means this was an invalid term!
     *
     * @param  docName    document name to create a vector from
     * @param  index index to use for vector dimensions
     *
     * @return            the normalised tf vector of a document
     */
    private static RealVector createDocumentVector(
                String docName,
                Index index
            ) throws Exception {
        int docID = index.document(docName);
        if (docID == 0)
            throw new Exception("Document with name " + docName + " doesn't exist");

        // tf
        int termIndex;
        double[] vectorArray = new double[index.termCountUnique()];
        TermInfo termInfo;
        TermInfoList termList = index.termInfoList(docID);
        termList.startIteration();
        while (termList.hasMore()) {
            termInfo = termList.nextEntry();
            // Try and break this to check this is correct!
            termIndex = termInfo.termID() - 1;

            if (vectorArray[termIndex] != 0)
                throw new Exception("Couting term twice!");
            vectorArray[termIndex] = termInfo.count();
        }
        termList.delete();

        // LOGGER.finer("Vector length: " + vectorArray.length);
        // LOGGER.finer("Max term index: " + maxTermIndex);
        return (new ArrayRealVector(vectorArray)).unitVector();
    }

    private static RealVector calculateIDFVector(Index index) throws Exception {
        // idf = log (|D| / #D with t in D)
        // double idf;
        double docCount = index.docCount();
        double[] vectorArray = new double[index.termCountUnique()];
        for (int i=0; i < index.termCountUnique(); i++) {
            vectorArray[i] = Math.log(
                docCount / index.docCount(i + 1)
            );
            // idf = - Math.log((double) index.docCount(i + 1));
            // idf = 1.0 / index.docCount(i + 1);
            // LOGGER.finest("IDF for " + index.term(i + 1) + " is " + idf);
        }
        return new ArrayRealVector(vectorArray);
    }

    // private static Map<String, Integer> reverseVocabulary(String[] vocabularyArray) {
    //     Map<String, Integer> vocabulary = new HashMap<>(vocabularyArray.length);
    //     for (int i=0; i < vocabularyArray.length; i++)
    //         vocabulary.put(vocabularyArray[i], i);
    //     return vocabulary;
    // }
}
