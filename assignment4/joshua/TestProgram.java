
/* 
 * Joshua Shaham
 * shaham.j@husky.neu.edu
 * This program is designed to be a black-box test
 * for implementations of the ADT SearchableString
*/

public class TestProgram {

	public static void main(String[] args) {
		TestProgram test = new TestProgram();
		test.creation();
		test.accessors();
		test.summarize();
	}
	
	private void summarize () {
    	if (totalErrors == 0) System.err.println("Passed all tests.");
    	else System.err.println("Failed " + totalErrors + " tests.");
	}
	
	public TestProgram () {}

	// Declare some SearchableString objects
    private SearchableString x1;
    private SearchableString x2;
    private SearchableString x3;
    private SearchableString x4;
    
    // Store the SearchableString implementation in the objects
    private void creation () {
    	try {
    		x1 = SearchableStrings.make("telegraph");
    		x2 = SearchableStrings.make("telegram");
    		x3 = SearchableStrings.make("elegant");
    		x4 = SearchableStrings.make("graph");
    	}
    	catch (Throwable e) {
    		assertTrue ("creation", false);
    	}
    }
    
    // Test methods
    private void accessors () {
    	try {
    		
    		// Default tests
    		
    	   assertTrue("distance: x1 x1, negative i", x1.distance(x1, -1) == 19);
    	   assertTrue("distance: x1 x1, zero i", x1.distance(x1, 0) == 0);
    	   assertTrue("distance: x1 x1, positive i", x1.distance(x1, 1) == 19);

    	   assertTrue("distance: x1 x2, zero i", x1.distance(x2, 0) == 2);
    	   assertTrue("distance: x1 x2, positive i", x1.distance(x2, 1) == 9);

    	   assertTrue("distance: x1 x3, zero i", x1.distance(x3, 0) == 9);
    	   assertTrue("distance: x1 x3, positive i", x1.distance(x3, 1) == 5);

    	   assertTrue("distance: x1 x4, zero i", x1.distance(x4, 0) == 9);
    	   assertTrue("distance: x1 x4, positive i", x1.distance(x4, 4) == 4);

    	   assertTrue("bestMatch: x1 x1", x1.bestMatch(x1) == 0);
    	   assertTrue("bestMatch: x1 x2", x1.bestMatch(x2) == 0);
    	   assertTrue("bestMatch: x1 x3", x1.bestMatch(x3) == 1);
    	   assertTrue("bestMatch: x1 x4", x1.bestMatch(x4) == 4);
	
    	   // Idiosyncratic tests
    	   
    	   assertTrue("toString: x1", x1.toString() == "telegraph");
    	   assertTrue("toString: x2", x2.toString() == "telegram");
    	   assertTrue("toString: x3", x3.toString() == "elegant");
    	   assertTrue("toString: x4", x4.toString() == "graph");
    	   
    	   assertTrue("equals: x1 null", x1.equals(null) == false);
    	   assertTrue("equals: x1 x1", x1.equals(x1) == "telegraph".equals("telegraph"));
    	   assertTrue("equals: x1 x2", x1.equals(x2) == "telegraph".equals("telegram"));
    	   assertTrue("equals: x1 x3", x1.equals(x3) == "telegraph".equals("elegant"));
    	   assertTrue("equals: x1 x4", x1.equals(x4) == "telegraph".equals("graph"));
    	   
    	   assertTrue("hashCode: x1", x1.hashCode() == "telegraph".hashCode());
    	   assertTrue("hashCode: x2", x2.hashCode() == "telegram".hashCode());
    	   assertTrue("hashCode: x3", x3.hashCode() == "elegant".hashCode());
    	   assertTrue("hashCode: x4", x4.hashCode() == "graph".hashCode());
    	}
    	
    	catch (Throwable t) {
    		assertTrue("accessors", false);
    	}
    }

    private int totalErrors = 0;      // Number of errors
	
    // Prints failure report if the result is not true.
    private void assertTrue (String name, boolean result) {
        if (! result) {
            System.err.println ("ERROR: " + name);
            totalErrors++;
        }
    }

}
