import java.util.logging.Logger;

import java.lang.Exception;
import java.util.Arrays;

import java.io.IOException;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.FileOutputStream;

import org.apache.commons.math3.linear.RealVector;

import lemurproject.lemur.Index;

public class LDFWriter extends BufferedWriter {
    private final static Logger LOGGER = Logger.getLogger(LDFWriter.class.getName());

    private Index index;

    LDFWriter(String path, Index index) throws IOException {
        super(
            new OutputStreamWriter(
                new FileOutputStream(path),
                "utf-8"
            )
        );
        this.index = index;
    }

    // /**
    //  * Write the vector of a QueryWithID to a file in LDF format
    //  *
    //  * @param  query       QueryWithID to be written
    //  *
    //  * @throws IOException
    //  */
    // public void writeVector(QueryWithID query) throws Exception {
    //     // <DOC 12001>
    //     // 1 0.11
    //     // samuel 0.31
    //     // </DOC>
    //     LOGGER.finer("Writing " + query.getID());
    //     write("<DOC " + query.getID() + ">");
    //     newLine();

    //     RealVector vector = query.getVector();
    //     if (vector.getDimension() != 0) {
    //         double weight;
    //         for (int i=0; i < this.index.termCountUnique(); i++)
    //             if ((weight = vector.getEntry(i)) != 0) {
    //                 write(this.index.term(i + 1) + " " + Double.toString(weight));
    //                 newLine();
    //             }
    //     }
    //     write("</DOC>");
    //     newLine();
    // }

    public void writeTerms(QueryWithID query) throws Exception {
        // <DOC 12001>
        // 1
        // samuel
        // </DOC>
        LOGGER.finer("Writing " + query.getID());
        write("<DOC " + query.getID() + ">");
        newLine();

        for (String term : query.getTerms()) {
            write(term);
            newLine();
        }

        write("</DOC>");
        newLine();
    }
}
