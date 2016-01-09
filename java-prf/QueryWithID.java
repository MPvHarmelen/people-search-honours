import org.apache.commons.math3.linear.RealVector;

public class QueryWithID {
    private String id;
    private String[] terms;
    // private RealVector vector;

    public QueryWithID(String id, String[] terms) { //, RealVector vector) {
        this.id = id;
        setTerms(terms);
        // this.vector = vector;
    }

    public String getID() {
        return this.id;
    }

    // public RealVector getVector() {
    //     return this.vector;
    // }

    public String[] getTerms() {
        return this.terms;
    }

    public String toString() {
        return getID() + " " + getTerms();
    }

    public void addTerms(String[] terms) {
        String[] newTerms = new String[this.terms.length + terms.length];
        System.arraycopy(this.terms, 0, newTerms, 0, this.terms.length);
        System.arraycopy(terms, 0, newTerms, this.terms.length, terms.length);
        setTerms(newTerms);
    }

    public void setTerms(String [] terms) {
        this.terms = terms;
    }
}
