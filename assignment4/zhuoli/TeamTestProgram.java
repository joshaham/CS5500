/**
* @author:
* @Email:
*/
import java.io.PrintWriter;
public class TeamTestProgram {
	public static void main(String[] args){
		String report=null;
		PrintWriter F=null;
		String user = args[0];
		String fileName=System.getProperty("user.dir") +"/"+user+ "/log.txt";
		System.out.println(fileName);
		try{ 
			F= new PrintWriter(fileName,"UTF-8");
		}catch(Exception ex){

		}
		report=ZhuoliTestProgram.RunTest();
		F.println(report);
		report=MosesTestProgram.RunTest();
		F.println(report);
		report=JoshuaTestProgram.RunTest();
		F.println(report);
		try{
			F.close();
		}catch(Exception ex){

		}

	}
}


class JoshuaTestProgram{
	public static String RunTest() {
		JoshuaTestProgram test = new JoshuaTestProgram();
		test.creation();
		test.accessors();
		return test.summarize();
	}
	
	private String summarize () {
    	if (totalErrors == 0) return "\nJoshua\nPassed all tests.";
    	else return "\nJoshua\nFailed " + totalErrors + " tests.";
	}
	
	public JoshuaTestProgram () {}

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

class MosesTestProgram{
//ignores its argument and performs black-box testing
	//of the SearchableString ADT
	public static String RunTest () {
		MosesTestProgram test = new MosesTestProgram();
		test.testToString();
		test.testEquals();
		test.testHashCode();
		test.testDistance();
		test.testBestMatch();
		return test.summarize();
	}

	public MosesTestProgram () { }
	
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
	
	private String summarize () {
		if (totalErrors == 0){
			return "\nMoses\nPassed all tests.";
		} else {
			return "\nMoses\nFailed " + totalErrors + " tests.";
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
class ZhuoliTestProgram{
	StringBuilder records=null;
	int falied_count=0;
	boolean errFlag=false;
	SearchableString x1=null;
	SearchableString x2=null;
	SearchableString x3=null;
	SearchableString x4=null;

	public ZhuoliTestProgram(){
		records=new StringBuilder("Zhuoli Test\n");
	    x1 = SearchableStrings.make("telegraph");
	    x2 = SearchableStrings.make("telegram");
	    x3 = SearchableStrings.make("elegant");
	    x4 = SearchableStrings.make("graph");
	}
	
	private void test_staticFactory(){
		SearchableString obj1=SearchableStrings.make("hello");
		// test return value type
		if(obj1==null || !(obj1 instanceof SearchableString)){
			this.errRecord("make method returned non-SearchableStrings object");
		}
		SearchableString obj2=SearchableStrings.make("");
		// test return value address
		if(obj1==obj2){
			this.errRecord("make method created two different strings with the same objects");
		}
	}
	
	private void test_distance(){
		String s1="hello world";
		String s2="world";
		int result= SearchableStrings.make(s1).distance(SearchableStrings.make(s2), -3);
		if(result!=s1.length()+s2.length()+1){
			this.errRecord("distance method output error while i<0");
		}
		result=SearchableStrings.make(s1).distance(SearchableStrings.make(s2), 5);
		if(result!=11){
			this.errRecord("distance method output error while i=5");
		}	
		result=SearchableStrings.make(s1).distance(SearchableStrings.make(s2), 6);
		if(result!=6){
			this.errRecord("distance method expect: "+6+" returns "+result);
		}
		result=SearchableStrings.make(s1).distance(SearchableStrings.make(s2), 8);
		if(result!=17){
			this.errRecord("distance method expect: "+17+" returns "+result);
		}
		
		if(x1.distance(x1, -1)!=19){
			this.errRecord("distance method expect: "+19+" returns "+x1.distance(x1, -1));
		}
		if(x1.distance(x1, 0)!=0){
			this.errRecord("distance method expect: "+0+" returns "+x1.distance(x1, 0));
		}
		if(x1.distance(x1, 1)!=19){
			this.errRecord("distance method expect: "+19+" returns "+x1.distance(x1, 1));
		}
		
		int val=x1.distance(x2,0);
		if(val!=2){
			this.errRecord("distance method expect: "+2+" returns "+val);
		}
		val=x1.distance(x2,1);
		if(val!=9){
			this.errRecord("distance method expect: "+9+" returns "+val);
		}
		
		val=x1.distance(x3, 0);
		if(val!=9){
			this.errRecord("distance method expect: "+9+" returns "+val);
		}
		val=x1.distance(x3, 1);
		if(val!=5){
			this.errRecord("distance method expect: "+5+" returns "+val);
		}
		
		val=x1.distance(x4, 0);
		if(val!=9){
			this.errRecord("distance method expect: "+9 +" returns "+val);
		}
		val=x1.distance(x4, 4);
		if(val!=4){
			this.errRecord("distance method expect: "+4 +" returns "+val);
		}
		
	}
	private void test_bestMatch(){
		String s1="world";
		String s2="hello world";
		int result=SearchableStrings.make(s1).bestMatch(SearchableStrings.make(s2));
		if(result!=17){
			this.errRecord("bestMatch expect: "+17+" returns "+result);
		}
		s1="hello world";
		s2="world";
		result=SearchableStrings.make(s1).bestMatch(SearchableStrings.make(s2));
		if(result!=6){
			this.errRecord("bestMatch expect: "+6+" returns "+result);
		}
		int val =x1.bestMatch(x1);
		if(val!=0){
			this.errRecord("bestMatch expect: "+0 + " returns "+val);
		}
		val =x1.bestMatch(x2);
		if(val!=0){
			this.errRecord("bestMatch expect: "+0 + " returns "+val);
		}
		val =x1.bestMatch(x3);
		if(val!=1){
			this.errRecord("bestMatch expect: "+1 + " returns "+val);
		}
		val =x1.bestMatch(x4);
		if(val!=4){
			this.errRecord("bestMatch expect: "+4 + "returns "+val);
		}
	}
	private boolean test_toString(){
		SearchableString obj = SearchableStrings.make("hello world");
		if(obj.toString() == null){
			this.errRecord("to_string method returned null");
		}
		if(!"hello world".equals(obj.toString())){
			this.errRecord("to_string method returned wrong string");
		}
		return false;
	}
	private void test_equals(){
		SearchableString obj1=SearchableStrings.make("hello world");
		SearchableString obj2=SearchableStrings.make("hello world");
		SearchableString obj3=SearchableStrings.make("good night");
		if(!obj1.equals(obj2)){
			this.errRecord("equal method 'hello world' doesn't equal 'hello world'");
		}
		if(obj1.equals(obj3)){
			this.errRecord("equal method 'hello world' equal 'good night");
		}
		if(obj1.equals(null)){
			this.errRecord("equal method should not equal null");
		}
	}
	private void test_hashCode(){
		SearchableString obj1=SearchableStrings.make("hello world");
		SearchableString obj2=SearchableStrings.make("hello world");
		SearchableString obj3=SearchableStrings.make("binggo");
		if(obj1.hashCode()!="hello world".hashCode()){
			this.errRecord("hashCode method returned wrong 'hello world' not equal 'hello world'");
		}
		if(obj1.hashCode()!=obj2.hashCode()){
			this.errRecord("hashCode method returned wrong 'hello world' not equal 'hello world'");
		}
		if(obj1.hashCode()==obj3.hashCode()){
			this.errRecord("hashCode method returned wrong ");
		}
	}
	
	private void errRecord(String message){
		this.errFlag=true;
		this.falied_count+=1;
		records.append("ERROR: "+message+'\n');
	}
	private String testSummary(){
		if(this.errFlag==false){
			records.append("Passed all test");
			return records.toString();
		}else{
			records.append(this.falied_count+" Failed");
			return records.toString();
		}
	}
	/* black-box testing of the SearchableString ADT */
	public static String RunTest(){
		ZhuoliTestProgram testObj=null;
		try{
			testObj=new ZhuoliTestProgram();
			testObj.test_staticFactory();
			testObj.test_toString();
			testObj.test_equals();
			testObj.test_hashCode();
			testObj.test_distance();
			testObj.test_bestMatch();
		}catch(Exception e){
			testObj.errRecord("run-time exception");
		}finally{
			return testObj.testSummary();
		}
	}
}
