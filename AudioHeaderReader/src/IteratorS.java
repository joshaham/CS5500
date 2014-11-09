import java.util.ArrayList;
import java.util.Iterator;



public class IteratorS {

	public static void main(String[] args){
		ArrayList<Integer> a = new ArrayList<Integer>();
		a.add(0);
		a.add(1);
		Iterator<Integer> it=a.iterator();
		a.clear();
		System.out.println(it.next());
		System.out.println(it.next());
		System.out.println(it.next());
	}
}
