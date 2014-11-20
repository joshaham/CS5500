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
        String song1Name = audioFile.getFileName();
        HashMap<Long, String> fileHashMap = audioFile.getHashMap();
        
        //HashMap of songs and time segments that 
        //returned matches with the given audioFile
        //Key: name of song
        //Value: double[] where double[0] is the second where the match
        //starts in song 1, and double[1] is the second where the match
        //starts in song 2
        HashMap<String, double[]> matches = new HashMap<String, double[]>();
        
        for (long key : fileHashMap.keySet()) {
//        	System.out.println(key+"  -> "+fileHashMap.get(key));

            if (containerHM.containsKey(key)) {
                String firstSong = fileHashMap.get(key);
                String secondSong = containerHM.get(key);
                
                String[] song1 = firstSong.split(";");
                String[] song2 = secondSong.split(";");
                
                double song1Start = Double.parseDouble(song1[1]);
                double song2Start = Double.parseDouble(song2[1]);
                double[] songMatchStarts = new double[2];
                songMatchStarts[0] = song1Start;
                songMatchStarts[1] = song2Start;
                
                if(matches.containsKey(song2[0])) {
                    double storedSong1Start = matches.get(song2[0])[0];
                    double storedSong2Start = matches.get(song2[0])[1];
                    
                    if (song1Start < storedSong1Start ||
                            song2Start < storedSong2Start) {
                        matches.put(song2[0], songMatchStarts);
                    }
                } else {
                    matches.put(song2[0], songMatchStarts);
                }
            }
            
            for (String matchKey : matches.keySet()) {
                String song2Name = matchKey;
                double song1Start = matches.get(matchKey)[0];
                double song2Start = matches.get(matchKey)[1];
                
                System.out.println("MATCH " + song1Name + " " 
                        + song2Name + " " 
                        + song1Start + " "
                        + song2Start + "\n");
            }
            
        }
	}

    private void fillHashMap(ArrayList<Audio> container) {
    	if(containerHM==null){
    		containerHM=new HashMap<Long, String> ();
    	}
        for (Audio audioFile : container) {
            HashMap<Long, String> fileHashMap = audioFile.getHashMap();
            containerHM.putAll(fileHashMap);
        }   
    }

}
	