/* 
 * Joshua Shaham
 * shaham.j@husky.neu.edu 
 */

// Recipe implementation of the SearchableString ADT.

public class SearchableStrings {

	/* Static methods */
	
	// The constructor, invoke by calling
	// private SearchableString x1 = new SearchableStrings.make(astring);
	public static SearchableString make(String x1) {
		return new SearchableStringBase(x1);
	}

	private static class SearchableStringBase implements SearchableString {

		// The given string
		String s;

		// The constructor
		SearchableStringBase(String s) {
			this.s = s;
		}

		/* Dynamic methods */
		
		// Determines the number of mismatched characters
		// e.g "VALUE" and "VALVE" have distance 1,
		//     "EGG" and "BEG" have distance 2.
		public int distance (SearchableString x2, int n) {
			// Initialize the lengths to improve readability
			int slength = s.length();
			int x2length = x2.toString().length();
			
			// Algebraic Specification
			if ( (n >= 0) && (n <= slength - x2length) ) {
				// f is the auxiliary function defined below
				return f(s.substring(n, n + x2length), x2.toString())
						  + slength - x2length;
			}
			else { // if ( (n > slength - x2length)  || (n < 0) )
				return slength + x2length + 1;
			}
		}

		// Auxiliary function to the distance method:
		// Designed to have both inputs of the same size;
		// The metric for counting the number of mismatched characters
		// Shared by both the inputs
		private int f(String s1, String s2) {
			// Base case
			if (s1.length() == 0) {
				return 0;
			}
			// Same first character -> move on to the next character
			else if (s1.charAt(0) == s2.charAt(0)) {
				return f(s1.substring(1, s1.length()),
						 s2.substring(1, s1.length()));
			}
			// Otherwise -> aggregate 1 and move on to the next character
			else { //  (s1.charAt(0) != s2.charAt(0)) 
				return 1 + f(s1.substring(1, s1.length()),
							 s2.substring(1, s1.length()));
			}
		}

		// Returns the first right-shift that minimizes the distance
		// Between this SearchableString and the given one.
		public int bestMatch (SearchableString x2) {
			// Initialize the lengths to improve readability
			int slength = s.length();
			int x2length = x2.toString().length();
			
			// Algebraic Specification
			if (slength < x2length) {
				return slength + x2length + 1;
			} 
			else { // if (slength >= x2length)
				return bestMatchHelper(this, x2, slength - x2length);
			}
		}
		
		// Finds the smallest distance over the all possible right-shifts
		// And returns the first shift that produces this minimum
		private int bestMatchHelper(SearchableString x1, SearchableString x2, int dif) {
			int RunningMin = x1.toString().length();
			int Location = 0;
			int CurrentDistance;
			for (int i = 0; i <= dif; i++) {
				CurrentDistance = x1.distance(x2, i);
				if (CurrentDistance < RunningMin) {
					RunningMin = CurrentDistance;
					Location = i;
				}
			}
			return Location;
		}

		// Algebraic Specification
		public String toString() {
			return s;
		}

		// Axiomatic Specification
		public boolean equals(Object x) {
			if (x instanceof SearchableString) {
				if (x.toString() == s) return true;
				else return false;
			} 
			else return false;
		}

		// Algebraic specification
		public int hashCode() {
			return s.hashCode();
		}

		// Axiomatic Specification
		public int length() {
			return s.length();
		}

	}
}
