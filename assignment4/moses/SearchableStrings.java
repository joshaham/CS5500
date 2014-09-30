
/*
 * Moses Gonzalez
 * mgonzalez1130@gmail.com
 */

public class SearchableStrings {

	public static SearchableString make (String givenString){
		return new SearchableStringsInstance (givenString);
	}
	
	private static class SearchableStringsInstance implements SearchableString {
		
		private String thisString;
		
		SearchableStringsInstance(String givenString){
			this.thisString = givenString;
		}
		
		public String toString() {
			return thisString;
		}
		
		public int hashCode() {
			return thisString.hashCode();
		}
		
		public boolean equals(Object obj) {
			if (obj instanceof SearchableStringsInstance) {
				return (thisString == obj.toString());
			} else {
				return false;
			}
		}

		public int distance(SearchableString x2, int i) {
			
			String givenString = x2.toString();
			
			if (i < 0) {
				return thisString.length() + givenString.length() + 1;
			} else if (i >= 0 && i > thisString.length() - givenString.length()) {
				return thisString.length() + givenString.length() + 1;
			} else {
				return auxiliaryFunction (thisString.substring(i, i + givenString.length()), 
						givenString)
						+ thisString.length() - givenString.length();
			}
		}

		public int bestMatch(SearchableString x2) {
			
			String givenString = x2.toString();
			
			if (thisString.length() < givenString.length()) {
				return thisString.length() + givenString.length() + 1;
			} else {
				return this.findBestMatch (x2);
			}
		}
		
		private int findBestMatch (SearchableString x2) {
			
			int bestIndexSoFar = 0;
			int bestDistanceSoFar = this.distance(x2, 0);
			String givenString = x2.toString();
			
			for (int i = 1; i <= thisString.length() - givenString.length(); i++) {
				int newIndex = i;
				int newDistance = this.distance(x2, i);
				
				if (newDistance < bestDistanceSoFar) {
					bestIndexSoFar = newIndex;
					bestDistanceSoFar = newDistance;
				}
			}	
			return bestIndexSoFar;
		}
		
		private static int auxiliaryFunction (String string1, String string2){
			if (string1.length() == 0 
					&& string1.length() == string2.length()) {
				return 0;
			} else if (string1.length() > 0 
					&& string1.length() == string2.length()
					&& string1.charAt(0) == string2.charAt(0)) {
				return auxiliaryFunction (string1.substring(1, string1.length()), 
						string2.substring(1, string2.length()));
			} else {
				return 1 + auxiliaryFunction (string1.substring(1, string1.length()), 
						string2.substring(1, string2.length()));
			}
		}
		
	}
	
}
