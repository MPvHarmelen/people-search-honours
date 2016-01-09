public class LinkedWeightedTerm {// implements Comparable<LinkedWeightedTerm> {
    private String term;
    private double weight;
    private LinkedWeightedTerm next;

    LinkedWeightedTerm(String term, double weight, LinkedWeightedTerm next) {
        this.term = term;
        this.weight = weight;
        setNext(next);
    }

    LinkedWeightedTerm(String term, double weight) {
        this(term, weight, null);
    }

    public String getTerm() {
        return this.term;
    }

    public double getWeight() {
        return this.weight;
    }

    public LinkedWeightedTerm getNext() {
        return this.next;
    }

    public void setNext(LinkedWeightedTerm next) {
        this.next = next;
    }

    public void insertAfter(LinkedWeightedTerm lwt) throws Exception {
        if (lwt.getNext() != null)
            throw new Exception("LWT being inserted is already part of a chain.");
        lwt.setNext(getNext());
        setNext(lwt);
    }
}
