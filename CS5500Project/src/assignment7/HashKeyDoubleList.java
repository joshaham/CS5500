package assignment7;

import java.util.HashSet;

public class HashKeyDoubleList {
	static HashSet<HashKeyDoubleList> container=null;
	HashKeyDoubleList previous=null;
	HashKeyDoubleList next=null;
	
	public HashKeyDoubleList(long val){
		if(container==null){
			container=new HashSet<HashKeyDoubleList>();
			container.add(this);
		}else{
			
		}
	}
	
}
