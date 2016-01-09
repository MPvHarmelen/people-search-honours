import java.lang.Exception;

// import java.util.ArrayList;
// import java.util.List;
import java.util.Map;
// import java.util.HashMap;

import java.util.logging.Logger;
// import java.util.logging.Level;

import javax.xml.parsers.DocumentBuilderFactory;

import javax.xml.parsers.ParserConfigurationException;

import java.io.IOException;

import org.xml.sax.SAXException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.ArrayRealVector;

import lemurproject.lemur.Index;


// import lemurproject.indri.QueryRequest;

// import QueryWithID

class QueryReader {
    private final static Logger LOGGER = Logger.getLogger(QueryReader.class.getName());
    private static final String QUERY_TAG = "query";
    private static final String ID_TAG = "number";
    private static final String TEXT_TAG = "text";

    private int entrycnt;
    private NodeList nodeList;
    private int nodeListLength;
    private Index index;

    public QueryReader(String path, Index index)
            throws Exception {
        this.entrycnt = 0;
        this.index = index;
        Document dom = parseXmlFile(path);
        // get the root elememt
        // get a node list of <query> elements
        this.nodeList = dom.getDocumentElement().getElementsByTagName(this.QUERY_TAG);
        if (nodeList == null || nodeList.getLength() == 0)
            throw new Exception("No queries");
        this.nodeListLength = this.nodeList.getLength();
    }

    public QueryWithID readQueryVector() throws Exception {
        if (this.entrycnt < this.nodeListLength) {
            Element query = (Element) this.nodeList.item(this.entrycnt);
            String id = getTextValue(query, this.ID_TAG);
            String[] terms = getTextValue(query, this.TEXT_TAG).split("\n");
            if (terms.length == 1 && terms[0].equals(""))
                terms = new String[0];
            this.entrycnt++;
            return new QueryWithID(id, terms);//, createUnitVector(terms));
        }
        return null;
    }


    /**
     * from http://www.java-samples.com/showtutorial.php?tutorialid=152
     *
     * @param  path                         path to xml file
     *
     * @return                              dom of xml structure
     *
     * @throws SAXException
     * @throws IOException
     * @throws ParserConfigurationException
     */
    private static Document parseXmlFile(String path) throws SAXException, IOException,
            ParserConfigurationException {
        LOGGER.info("Parsing query file " + path);
        // get the factory
        // Using factory get an instance of document builder
        // parse using builder to get DOM representation of the XML file
        return DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(path);
    }


    /**
     * Edited from http://www.java-samples.com/showtutorial.php?tutorialid=152
     * I take a xml element and the tag name, look for the tag and get
     * the text content
     * i.e for <employee><name>John</name></employee> xml snippet if
     * the Element points to employee node and tagName is name I will return John
     *
     * @param  ele
     * @param  tagName
     *
     * @return
     */
    private static String getTextValue(Element ele, String tagName) {
        NodeList nl = ele.getElementsByTagName(tagName);
        if (nl != null && nl.getLength() > 0) {
            String textValue = "";
            Node el = nl.item(0).getFirstChild();
            if (el != null) textValue = el.getNodeValue();
            LOGGER.finer("Text value: " + textValue);
            return textValue;
        }
        LOGGER.finer("No text value");
        return null;
    }

    /**
     * Create a unit query vector with the given index. Pay attention!
     * This method is case sensitive.
     *
     * @param  query      the query to create a vector for
     * @param  index      the index to use as dimensions
     *
     * @return            a unit query vector
     */
    private RealVector createUnitVector(String[] query) throws Exception {
        boolean zero_vector = true;
        int termID;
        double[] queryValues = new double[this.index.termCountUnique()];
        for (int i=0; i < query.length; i++) {
            if ((termID = this.index.term(query[i])) != 0) {
                LOGGER.finer(Integer.toString(termID));
                queryValues[termID - 1] = 1;
                zero_vector = false;
            }
        }
        RealVector vector = new ArrayRealVector(queryValues);
        if (!zero_vector)
            vector.unitize();
        return vector;
    }
}
