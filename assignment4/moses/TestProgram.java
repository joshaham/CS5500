
/*
 * Moses Gonzalez
 * mgonzalez1130@gmail.com
 */

public class TestProgram {
	
	//ignores its argument and performs black-box testing
	//of the SearchableString ADT
	public static void main (String[] ignored) {
		TestProgram test = new TestProgram();
		test.testToString();
		test.testEquals();
		test.testHashCode();
		test.testDistance();
		test.testBestMatch();
		test.summarize();
	}

	public TestProgram () { }
	
	private String testString1 = "telegraph";
	private String testString2 = "telegram";
	private String testString3 = "elegant";
	private String testString4 = "graph";
	private String testString5 = "zzz";
	
	//tests the toString() method
	private void testToString () {
		assertTrue ("toString()", 
				SearchableStrings.make(testString1).toString() == testString1);
		
		assertTrue ("toString()",
				SearchableStrings.make(testString1).toString() != null);
	}
	
	//tests the equals() method
	private void testEquals() {
		String s1 = testString3;
		String s2 = testString3;
		String s3 = testString3;
		String s4 = testString4;
		int int1 = 4;
		
		//test when the given value is null
		assertFalse ("testEquals() test 1", SearchableStrings.make(s1).equals(null));
		
		//test when the two strings are the same
		assertTrue ("testEquals() test 2", 
				SearchableStrings.make(s1).equals(SearchableStrings.make(s2))
				== s1.equals(s2));
		
		//test when the two strings are different
		assertTrue ("testEquals() test 3", 
				SearchableStrings.make(s3).equals(SearchableStrings.make(s4))
				== s3.equals(s4));
		
		//test when the given value is not a a SearchableString
		assertFalse ("testEquals() test 4", SearchableStrings.make(s1).equals(int1));
		
		//test when the two strings are the same but the given string is
		//not a SearchableString
		assertFalse ("testEquals() test 5", SearchableStrings.make(s1).equals(s1));
	}
	
	private void testHashCode() {
		assertTrue("testHashCode()", SearchableStrings.make(testString4).hashCode()
				== testString4.hashCode());
	}
	
	SearchableString x1 = SearchableStrings.make(testString1);
	SearchableString x2 = SearchableStrings.make(testString2);
	SearchableString x3 = SearchableStrings.make(testString3);
	SearchableString x4 = SearchableStrings.make(testString4);
	SearchableString x5 = SearchableStrings.make(testString5);
	
	private void testDistance() {
		
		int testInt1 = -1;
		int testInt2 = 3;
		int testInt3 = 5;
		
		assertTrue ("testDistance test 1",
				SearchableStrings.make(testString1).distance(SearchableStrings.make(testString4),
						testInt1)
						== testString1.length() + testString4.length() + 1);
		
		assertTrue ("testDistance test 2",
				SearchableStrings.make(testString1).distance(SearchableStrings.make(testString4),
						testInt3) 
						== testString1.length() + testString4.length() + 1);
		
		assertTrue ("testDistance test 3",
				SearchableStrings.make(testString1).distance(SearchableStrings.make(testString4), 
						testInt2)
						== (auxiliaryFunction (testString1.substring
								(testInt2, (testInt2 + testString4.length())), 
								testString4)) + testString1.length() - testString4.length());
		

		assertTrue ("testDistance test 4", x1.distance(x1, -1) == 19);
		
		assertTrue ("testDistance test 5", x1.distance(x1, 0) == 0);
			
		assertTrue ("testDistance test 6", x1.distance(x1, 1) == 19);
		
		assertTrue ("testDistance test 7", x1.distance(x2, 0) == 2);
		
		assertTrue ("testDistance test 8", x1.distance(x2, 1) == 9);
		
		assertTrue ("testDistance test 9", x1.distance(x3, 0) == 9);
		
		assertTrue ("testDistance test 10", x1.distance(x3, 1) == 5); 
		
		assertTrue ("testDistance test 11", x1.distance(x4, 0) == 9);
	}
	
	private void testBestMatch() {
		// s1.length() < s2.length()
		assertTrue ("testBestMatch test 1", x2.bestMatch(x1) == 18);
		
		assertTrue ("testBestMatch test 2", x3.bestMatch(x1) == 17);
		
		assertTrue ("testBestMatch test 3", x4.bestMatch(x1) == 15);
		
		assertTrue ("testBestMatch test 4", x5.bestMatch(x1) == 13);
		
		assertTrue ("testBestMatch test 5", x3.bestMatch(x2) == 16);
		
		assertTrue ("testBestMatch test 6", x4.bestMatch(x2) == 14);
		
		assertTrue ("testBestMatch test 7", x5.bestMatch(x2) == 12);
		
		assertTrue ("testBestMatch test 8", x4.bestMatch(x3) == 13);
		
		assertTrue ("testBestMatch test 9", x5.bestMatch(x3) == 11);
		
		assertTrue ("testBestMatch test 10", x5.bestMatch(x4) == 9);
		

		// s1.length() >= s2.length
		assertTrue ("testBestMatch test 11", x1.bestMatch(x1) == 0);
		
		assertTrue ("testBestMatch test 12", x1.bestMatch(x2) == 0);
		
		assertTrue ("testBestMatch test 13", x1.bestMatch(x3) == 1);
		
		assertTrue ("testBestMatch test 14", x1.bestMatch(x4) == 4);
		
		assertTrue ("testBestMatch test 15", x1.bestMatch(x5) == 0);
		
		assertTrue ("testBestMatch test 16", x2.bestMatch(x3) == 1);
		
		assertTrue ("testBestMatch test 17", x2.bestMatch(x4) == 0);
		
		assertTrue ("testBestMatch test 18", x2.bestMatch(x5) == 0);
		
		assertTrue ("testBestMatch test 19", x3.bestMatch(x4) == 2);
		
		assertTrue ("testBestMatch test 20", x3.bestMatch(x5) == 0);
		
		assertTrue ("testBestMatch test 21", x4.bestMatch(x5) == 0);	
	}
	
	
	private int totalErrors = 0;
	
	// Prints failure report if the result is not true
	private void assertTrue (String testName, boolean result) {
		if (! result) {
			totalErrors ++;
			System.err.println("Error: Test failed; " + testName + "; " + totalErrors
					+ " tests failed so far");
		}
	}
	
	//Prints failure report if the result is not false
	private void assertFalse (String testName, boolean result) {
		assertTrue (testName, ! result);
	}
	
	private void summarize () {
		if (totalErrors == 0){
			System.err.println("Passed all tests.");
			System.exit(0);
		} else {
			System.err.println("Failed " + totalErrors + " tests.");
			System.exit(1);
		}
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