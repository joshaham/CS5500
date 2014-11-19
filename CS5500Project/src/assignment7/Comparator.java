package assignment7;

import java.util.ArrayList;
/**
 * 
 * @author zhuoli
 * @purpose: comparator containers, 
 */
public class Comparator {
	ArrayList<Audio> container1=null;
	ArrayList<Audio> container2=null;
	// contain files from args1 and files from args3
	public Comparator(String[] files1,String[] files2){
		container1=new ArrayList<Audio>();
		container2=new ArrayList<Audio>();
		fillContainer(container1,files1);
		fillContainer(container2,files2);
	}
	
	private void fillContainer(ArrayList<Audio> container,String[] files){
		for(String file : files){
			Audio audio=null;
			audio = Audio.getInstance(file);
			if(audio==null){
				continue;
			}
			System.out.println(audio.getFileName()+"  : hashvalue  "+audio.hashValue);
			container.add(audio);
		}
	}
	// compare the two files
	public void compare(){
		System.out.println("To be implemented.....");
	}

	
}
	