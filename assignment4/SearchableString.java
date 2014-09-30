//  Interface type for the SearchableString ADT of assignments 2 and 3.
//
//  A SearchableString encapsulates a String and supports a limited form
//  of approximate string matching.  For details, see assignment 3.

public interface SearchableString {

    //  Returns a measure of the distance between this SearchableString
    //  and a SearchableString x2 shifted n code units to the right.

    public int distance (SearchableString x2, int n);

    //  Returns the smallest non-negative shift n that minimizes
    //  this.distance(x2, n).

    public int bestMatch (SearchableString x2);
}
