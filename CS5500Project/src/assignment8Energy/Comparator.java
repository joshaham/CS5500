package assignment8Energy;

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
	static double THRESHOLD=1;
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
	}
	
	// compare all the files in each container against each other
	public void compare(){
		//special case when the second container only has 1 file, indicating
		//that we need to check that file against all the files in container1
		for(Audio audio1 : container1){
			for(Audio audio2 : container2){
				if(isMatch(audio1,audio2)){
					continue;
				}
			}
		}

	}

	private boolean isMatch(Audio audio1, Audio audio2) {
		int valueSamplesPerSecond = (int) (1/(1-Energy.overlapRatio));
		int valuesPerZone=(TEST_VERSION.SongSampleSize*valueSamplesPerSecond);
		int[] hashvalue1=audio1.getHashValuePerSecondWithOverlap();
		int[] hashvalue2=audio2.getHashValuePerSecondWithOverlap();

		for(int array1Start=0;array1Start<hashvalue1.length-valuesPerZone;array1Start++){
			for(int array2Start=0;array2Start<hashvalue2.length-valuesPerZone;array2Start++){
				if(isMatchStartHere(hashvalue1,array1Start,hashvalue2,array2Start,valuesPerZone)){
						System.out.println("MATCH "+audio1.filename+" "+audio2.filename+" "+(array1Start+0.0)/valueSamplesPerSecond+" "+(array2Start+0.0)/valueSamplesPerSecond);
						return true;
				}
			}
		}
		return false;
	}
	
	private boolean isMatchStartHere(int[] array1,int array1Start,int[] array2,int array2Start,int valuesPerZone){
		double[] rateArray=new double[valuesPerZone];
		for(int i=0;i<valuesPerZone;i++){
			int idx1=array1Start+i,idx2=array2Start+i;
			rateArray[i]=getRate(array1[idx1],array2[idx2]);
		}
		if(calculateMeanError(rateArray)<THRESHOLD){
//			System.out.println("array1 length: "+array1.length+" array1Start: "+array1Start+" array2 length: "+array2.length+" array2Start: " +array2Start);
			int restSamples=Math.min(array1.length-array1Start, array2.length-array2Start);
//			System.out.println("Rest Samples: "+restSamples);
			rateArray=new double[restSamples];
			for(int i=0;i<valuesPerZone;i++){
				rateArray[i]=getRate(array1[array1Start+i],array2[array2Start+i]);
			}
		  return calculateMeanError(rateArray)<THRESHOLD;
		}
		return false;
	}
	private double getRate(int a, int b){
		a=Math.abs(a);
		b=Math.abs(b);
		if(a>b){
			return a/(0.1+b);
		}else{
			return b/(0.1+a);
		}
//		return a/(0.1+b);
	}
	
	private double calculateMeanError(double[] array){
		double ave=0;
		for(double d : array){
			ave+=d;
		}
		ave=ave/array.length;
		double errorAccumulation=0;
		for(double d : array){
			errorAccumulation+=Math.abs(d-ave);
		}
		double mean= errorAccumulation/array.length;
		if(Audio.DEBUG ){
			System.out.println("AVE value: " + ave+"MEAN ERROR: "+mean);
		}
		return mean;
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

	