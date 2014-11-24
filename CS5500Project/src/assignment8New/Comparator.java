package assignment8New;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
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
		fillContainerWithMultiThreads(container1,files1);
		fillContainerWithMultiThreads(container2,files2);
	}
	
	// utilize moltile-core process to fill container faster
	private void fillContainerWithMultiThreads(ArrayList<Audio> container,String[] files){
	      long begTest = new java.util.Date().getTime();
	      int nrOfProcessors = Runtime.getRuntime().availableProcessors();
	      ExecutorService eservice = Executors.newFixedThreadPool(nrOfProcessors);

	      for(String file : files){
	    	  eservice.execute(new ContainerThread(container,file));
	      }
	      eservice.shutdown();
	      try {
			eservice.awaitTermination(10, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
		}
//	      eservice.shutdownNow();
	      Double secs = new Double((new java.util.Date().getTime() - begTest)*0.001);
	      System.out.println("run time " + secs + " secs");
	}
	
	// compare all the files in each container against each other
	public void compare(){
		//special case when the second container only has 1 file, indicating
		//that we need to check that file against all the files in container1
		if (container2.size() == 1 && container1.size() > 1) {
		    fillHashMap(container1);
		    Audio audioFile = container2.get(0);
		    checkSong (audioFile);
		} else { 
			//check every file in container 1 against every file in container 2
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
                String song2Name = song2[0];
                songMatchStarts[0] = song1Start;
                songMatchStarts[1] = song2Start;
                
                if(matches.containsKey(song2Name)) {
                    double storedSong1Start = matches.get(song2Name)[0];
                    double storedSong2Start = matches.get(song2Name)[1];
                    
                    if (song1Start < storedSong1Start ||
                            song2Start < storedSong2Start) {
                        matches.put(song2Name, songMatchStarts);
                    }
                } else {
                    matches.put(song2Name, songMatchStarts);
                }
            }
        }
        
        for (String matchKey : matches.keySet()) {
            String song2Name = matchKey;
            double song1Start = matches.get(matchKey)[0];
            double song2Start = matches.get(matchKey)[1];
            
            System.out.println("MATCH " + song1Name + " " 
                    + song2Name + " " 
                    + song1Start/1000 + " "
                    + song2Start/1000);
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

class ContainerThread  implements Runnable {
	ArrayList<Audio> container;
	String file;
	public ContainerThread(ArrayList<Audio> container, String file){
		this.container=container;
		this.file=file;
	}
	@Override
	public void run() {
			Audio audio=null;
			audio = Audio.getInstance(file);
			if(audio!=null){
				synchronized(this.container){
					container.add(audio);
				}
			}
	}
}
	