package audio;

import java.util.ArrayList;
/**
 * 
 * @author zhuoli
 * @purpose: comparator containers, 
 */
public class Comparator {
	public static double THRESHOLD=30;
	private static int SECOND=2;
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
			container.add(audio);
		}
	}
	// compare the two files
	public void compare(){
//		System.out.println(container1.size());
//		System.out.println(container2.size());
		for(Audio file1:container1){
			for(Audio file2:container2){
				if (isMatch(file1,file2)) {
					String msg="MATCH "+file1.getFileName()+" "+file2.getFileName();
					System.out.println(msg);
				} else {
				}
			}
		}
	}
	// match method
	public static boolean isMatch(Audio file1, Audio file2){
		// fast method
		if(!fastMatch(file1,file2)){
			return false;
		}
		// time zone MSE method
		else{
			return TMSEMatch(file1,file2);
		}
		// frequency zone MSE method
//		else{
//			return MSEMatch(file1,file2);
//		}
		// Segment compare
//		else{
//			return SegmentMatch(file1,file2);
//		}
	}
	// fast match method
	private static boolean fastMatch(Audio file1, Audio file2){
		return Math.abs(file1.getAudioLength()-file2.getAudioLength()) < SECOND;
	}
	// time zone MSE compare
	private static boolean TMSEMatch(Audio file1, Audio file2){
		int meanSquaredError=ComparatorAlgorithm.calculateTMSE(file1, file2);
		System.out.println("TMSE: "+meanSquaredError);
		return meanSquaredError < THRESHOLD;
	}
	// frequency zone MSE compare
	private static boolean MSEMatch(Audio file1, Audio file2){
		double meanSquaredError = ComparatorAlgorithm.calculateMSE(file1,file2);
		return meanSquaredError < THRESHOLD;
		
	}
//	// segment MSE compare
//	private static boolean SegmentMatch(Audio file1, Audio file2){
//		int matchs=ComparatorAlgorithm.calculateMatchSegments(file1, file2);
//		return matchs>=1;
//	}
}

class AudioSegment {
	Audio audio;
	int segmentID;
	String audioName;
	int zcr;
	
	
	@Override
	public int hashCode(){ 
		return audio.hashCode()+this.segmentID;
	}
	@Override
	public String toString(){
		return this.audio.toString()+'\n'+this.segmentID+'\n'+zcr+'\n';
	}
}
