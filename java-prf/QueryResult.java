import java.lang.Comparable;

public class QueryResult implements Comparable<QueryResult> {
    // A query result that looks like this:
    // qryid ?? doc id  r score   run
    // 12001 Q0 t187436 1 51.6077 indri
    public String queryID;
    public String documentID;
    public int rank;
    public float score;
    public String run;

    QueryResult(String line) {
        String[] values = line.split("\\s+");
        this.queryID = values[0];
        this.documentID = values[2];
        this.rank = Integer.parseInt(values[3]);
        this.score = Float.parseFloat(values[4]);
        this.run = values[5];
    }

    public String getQueryID() {
        return this.queryID;
    }

    public String getDocumentID() {
        return this.documentID;
    }

    public int getRank() {
        return this.rank;
    }

    public float getScore() {
        return this.score;
    }

    public String getRun() {
        return this.run;
    }

    public String toString() {
        return getQueryID() + " " + getDocumentID() + " " + getRank() + " " + getScore() + " " + getRun();
    }

    public int compareTo(QueryResult other) {
        return Integer.compare(this.rank, other.getRank());
    }
}
