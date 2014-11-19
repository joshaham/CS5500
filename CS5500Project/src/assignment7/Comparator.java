package assignment7;

import java.util.ArrayList;
import java.util.HashMap;
/**
 * 
 * @author zhuoli
 * @purpose: comparator containers, 
 */
public class Comparator {
	ArrayList<Audio> container1=null;
	ArrayList<Audio> container2=null;
	
	HashMap<Long, String> containerHM=null;
	
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
//			System.out.println(audio.getFileName()+"  : hashvalue  "+audio.hashValue);
			container.add(audio);
		}
	}
	
	// compare all the files in each container against each other
	public void compare(){
		//special case when the second container only has 1 file, indicating
		//that we need to check that file against all the files in container1
		if (container2.size() == 1 && container1.size() > 1) {
		    fillHashMap(container1);
		    Audio audioFile = container2.get(0);
		    checkSong (audioFile);
		} else { //check every file in container 1 against every file in container 2
		    fillHashMap(container2);
		    for (Audio audioFile : container1) {
		        checkSong(audioFile);
		    }
		}
	}
	
	private void checkSong(Audio audioFile) {
        HashMap<Long, String> fileHashMap = audioFile.getHashMap();
        
        for (long key : fileHashMap.keySet()) {
            if (containerHM.containsKey(key)) {
                String firstSong = fileHashMap.get(key);
                String secondSong = containerHM.get(key);
                
                String[] song1 = firstSong.split(";");
                String[] song2 = secondSong.split(";");
                
                System.out.println("Match " + song1[0] + " "
                        + song2[0] + " " + song1[1] + " " 
                        + song2[1]);
            }
        }
	}

    private void fillHashMap(ArrayList<Audio> container) {
        for (Audio audioFile : container) {
            HashMap<Long, String> fileHashMap = audioFile.getHashMap();
            containerHM.putAll(fileHashMap);
        }   
    }

}
	