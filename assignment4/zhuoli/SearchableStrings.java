/**
 * 
 * @author Zhuoli Liang
 * @Email liang.zhu@husky.neu.edu
 *
 */
public class SearchableStrings implements SearchableString{
	String str=null;
	private SearchableStrings(String arg){
		this.str=arg;
	}
	// static method, 
	// RETURN a new instance
	public static SearchableStrings make(String arg){
		return new SearchableStrings(arg);
	}
	// dynamic methods
	// GIVEN: SearchableString, int
	// RETURN: int
	@Override
	public int distance(SearchableString obj, int i){
		String s1=this.str;
		String s2=obj.toString();
		int dis=-1;
		if(i<0){
			dis=s1.length()+s2.length()+1;
		}else if((0<=i) && (i<=s1.length()-s2.length())){
			dis=f(s1.substring(i, i+s2.length()),s2)
					+s1.length()-s2.length();
		}else if(0<=i && (i>s1.length()-s2.length())){
			dis=s1.length()+s2.length()+1;
		}
		
		return dis;
	}
	// GIVEN: SearchableString
	// RETURN: int
	@Override
	public int bestMatch(SearchableString obj){
		int j=0;
		String s1=this.str;
		String s2=obj.toString();
		if(s1.length()<s2.length()){
			j=s1.length()+s2.length()+1;
		}else if(s1.length()>=s2.length()){
			int min=Integer.MAX_VALUE;
			for(int i=0;i<=s1.length()-s2.length();i++){
				int val = f(s1.substring(i, i+s2.length()),s2);
				if(val<min){
					min=val;
					j=i;
				}
			}
		}
		
		return j;
	}
	// RETURN: String
	public String toString(){
		return this.str;
	}
	// RETURN: Boolean
	public boolean equals(Object obj){
		if(obj==null){
			return false;
		}else if(obj instanceof SearchableStrings){
			return this.str.equals(obj.toString());
		}else{
			return false;
		}
	}
	// RETURN int;
	public int hashCode(){
		return str.hashCode();
	}
	
	
	// private auxiliary function
	// calculate the diff between s1 and s2
	private int f(String s1,String s2){
		int n = s1.length();
		if(s1.length()!=s2.length()){
			throw new RuntimeException("input for auxiliary function not equal: s1.len!=s2.len");
		}
		if(n==0){
			return 0;
		}else if(s1.charAt(0)==s2.charAt(0)){
			return f(s1.substring(1,n),s2.substring(1,n));
		}else{
			return 1+f(s1.substring(1,n),s2.substring(1, n));
		}
	}

}
