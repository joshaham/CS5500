package assignment9;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


/**
 * 
 * @purpose: comparator containers, 
 */


public class Comparator {

   static double THRESHOLD=3;
   ArrayList<Audio> container1=null;
   ArrayList<Audio> container2=null;

   // contain files from args1 and files from args3
   public Comparator(String[] files1,String[] files2){
      container1 = new ArrayList<Audio>();
      container2 = new ArrayList<Audio>();
      fillContainerWithMultiThreads(container1, files1);
      fillContainerWithMultiThreads(container2, files2);
   }

   // utilize moltile-core process to fill container faster
   private void fillContainerWithMultiThreads(ArrayList<Audio> container,
                                                   String[] files){
      // get numbers of processors of this PC
      int nrOfProcessors = Runtime.getRuntime().availableProcessors();
      ExecutorService service = 
         Executors.newFixedThreadPool(nrOfProcessors);
      List<Future<Runnable>> futures = new ArrayList<Future<Runnable>>();

      for (String file : files) {
         // new thread
    	  Future f =service.submit(new ContainerThread(container,file));
    	  futures.add(f);
      }
      // wait for all tasks to complete before continuing
      for (Future<Runnable> f : futures)
      {
         try {
			f.get();
		} catch (Exception e){
	    	 if(Assignment9.DEBUG){
	    		 e.printStackTrace();
	    	 }
		}
      }
      service.shutdownNow();
   }

   // compare all the files in each container against each other
   public void compare() {
      // the numbers of average amplitudes per second is judged by 
      //  the overlap ratio of time window 
	  List<String> matches=new ArrayList<String>();
      int valueSamplesPerSecond = (int) (1/(1-AverageAmplitude.overlapRatio));
      int valuesPerZone=(Assignment9.SongSampleSize*valueSamplesPerSecond);
      for(Audio audio1 : container1){
         for(Audio audio2 : container2){
            if(isMatch(matches,audio1,audio2,valueSamplesPerSecond,valuesPerZone)){
               continue;
            }
         }
      }
      Collections.sort(matches);
      for(String match: matches){
    	  System.out.println(match);
      }
   }

   //compare if two files have one or more segments sounds alike
   private boolean isMatch(List<String> matches,Audio audio1, Audio audio2, 
                              int valueSamplesPerSecond, int valuesPerZone) {
      // these two arrays are used for visualization in DEBUG model
      int[] amplitudes1=audio1.getNegativeAmplitudeValuePerSecondWithOverlap();
      int[] amplitudes2=audio2.getNegativeAmplitudeValuePerSecondWithOverlap();
      for (int array1Start=0; array1Start < amplitudes1.length - valuesPerZone;
            array1Start++) {
      for (int array2Start=0; array2Start < amplitudes2.length - valuesPerZone;
            array2Start++) {
      if (doesMatchStartHere(1, 0, audio1, array1Start, 
      	                           audio2, array2Start, valuesPerZone)) {
         String msg = "MATCH " + audio1.filename + " " + audio2.filename + " "
               + (array1Start + 0.0) / valueSamplesPerSecond + " "
               + (array2Start + 0.0) / valueSamplesPerSecond;
         matches.add(msg);
         return true;
         }
      }
     }
   return false;
   }

   // compare algorithm3: compare the angles of 2d line plot, 
   //if the angles of 2 arrays hve the same trends then they the two 
   //arrays are similar
   private boolean doesMatchStartHere(int termination,int acculError,
         Audio audio1,int start1,Audio audio2,int start2, int valuesPerZone){

      int threshold = 5;
      int DIFF_ANGLE = 30;
      int count = 6;

      int[] array1Angles=termination==1?audio1.getPositiveValuesAngle()
            :audio1.getNegativeValuesAngle();
      int[] array2Angles=termination==1?audio2.getPositiveValuesAngle()
            :audio2.getNegativeValuesAngle();
      long sum = 0;
      int n = Math.min(array1Angles.length-start1, array2Angles.length-start2);
      n = Math.min(n, valuesPerZone);
      if (n < valuesPerZone){
         return false;
      }
      for (int i = 0; i < n; i++){
         if (Math.abs(array1Angles[start1 + i] - array2Angles[start2 + i])
         	   > DIFF_ANGLE && --count <= 0){
            return false;
         }
         sum += array1Angles[start1 + i] - array2Angles[start2 + i];
      }
      acculError += (int) (sum / n);

      // termination condition
      if (termination == 0){
         return acculError < threshold;
      }
      if (acculError < threshold){
         int offset = (int) (1 / (1 - AverageAmplitude.overlapRatio));
         for(int i = start1 - offset; i < start1 + offset; i++){
            for(int k = start2 - offset; k < start2 + offset; k++){
               //error index case
               if (i < 0 || k < 0){
                  continue;
               }
               if (doesMatchStartHere(0, acculError, audio1, i,
               	                           audio2, k, valuesPerZone)){
                  return true;
               }
            }
         }
      }
      return false;
   }

}


// helper class
class ContainerThread  implements Runnable {

   ArrayList<Audio> container;
   String file;

   public ContainerThread(ArrayList<Audio> container, String file){
      this.container = container;
      this.file = file;
   }

   @Override
   public void run() {
      Audio audio = null;
      audio = Audio.getInstance(file);
      if (audio != null){
         synchronized(this.container){
            container.add(audio);
         }
      }
   }

}