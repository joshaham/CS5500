/**
 * 
 * @author Zhuoli Liang
 * @Email liang.zhu@husky.neu.edu
 * @category black-box test for SearchableStrings.java
 * @version 1.0
 * 
 */

public class TestProgram {
	int falied_count=0;
	boolean errFlag=false;
	SearchableStrings x1=null;
	SearchableStrings x2=null;
	SearchableStrings x3=null;
	SearchableStrings x4=null;

	public TestProgram(){
	    x1 = SearchableStrings.make("telegraph");
	    x2 = SearchableStrings.make("telegram");
	    x3 = SearchableStrings.make("elegant");
	    x4 = SearchableStrings.make("graph");
	}
	
	private void test_staticFactory(){
		SearchableStrings obj1=SearchableStrings.make("hello");
		// test return value type
		if(obj1==null || !(obj1 instanceof SearchableString)){
			this.errRecord("make method returned non-SearchableStrings object");
		}
		SearchableStrings obj2=SearchableStrings.make("");
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
		SearchableStrings obj = SearchableStrings.make("hello world");
		if(obj.toString() == null){
			this.errRecord("to_string method returned null");
		}
		if(!"hello world".equals(obj.toString())){
			this.errRecord("to_string method returned wrong string");
		}
		return false;
	}
	private void test_equals(){
		SearchableStrings obj1=SearchableStrings.make("hello world");
		SearchableStrings obj2=SearchableStrings.make("hello world");
		SearchableStrings obj3=SearchableStrings.make("good night");
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
		SearchableStrings obj1=SearchableStrings.make("hello world");
		SearchableStrings obj2=SearchableStrings.make("hello world");
		SearchableStrings obj3=SearchableStrings.make("binggo");
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
		System.err.println("ERROR: "+message);
	}
	private void testSummary(){
		if(this.errFlag==false){
			System.err.println("Passed all tests.");
		}else{
			System.err.println("Failed " + this.falied_count+" tests.");
		}
	}
	/* black-box testing of the SearchableString ADT */
	public static void main(String[] args){
		TestProgram testObj=null;
		try{
			testObj=new TestProgram();
			testObj.test_staticFactory();
			testObj.test_toString();
			testObj.test_equals();
			testObj.test_hashCode();
			testObj.test_distance();
			testObj.test_bestMatch();
		}catch(Exception e){
			testObj.errRecord("run-time exception");
		}finally{
			testObj.testSummary();
			if(testObj.errFlag){
				System.exit(-1);
			}else{
				System.exit(0);
			}
		}
	}
}
