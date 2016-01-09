import java.util.logging.Logger;
import java.util.logging.Level;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ListFileReader {
    private final static Logger LOGGER = Logger.getLogger(ListFileReader.class.getName());
    /**
     * Read a newline separated list from a file
     *
     * @param  path        path to file to read from
     *
     * @return             array with lines of the file
     *
     * @throws IOException
     */
    public static String[] read(String path) throws IOException {
        LOGGER.setLevel(Level.FINE);
        LOGGER.info("Reading list from " + path);
        BufferedReader bufferedReader = new BufferedReader(new FileReader(path));
        List<String> lines = new ArrayList<String>();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            LOGGER.finest(line);
            lines.add(line);
        }
        bufferedReader.close();
        LOGGER.fine("Read " + lines.size() + " lines");
        return lines.toArray(new String[lines.size()]);
    }
}
