package audio;

import java.io.IOException;
import java.util.ArrayList;
/**
 * 
 * @author zhuoli
 * @purpose: comparator containers, 
 */
public class Comparator {
	private static double THRESHOLD=30;
	private static int SECOND=2;
	ArrayList<Audio> container1=null;
	ArrayList<Audio> container2=null;
	public Comparator(String[] files1,String[] files2){
		container1=new ArrayList<Audio>();
		container2=new ArrayList<Audio>();
		fillContainer(container1,files1);
		fillContainer(container2,files2);
	}
	
	private void fillContainer(ArrayList<Audio> container,String[] files){
		for(String file : files){
			Audio audio=null;
			try {
				audio = Audio.getInstance(file);
			} catch (IOException e) {
				System.out.println("File open exception: "+file);
				// TODO Auto-generated catch block
				e.printStackTrace();
				continue;
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(audio==null){
				continue;
			}
			container.add(audio);
		}
	}
	public void compare(){
//		System.out.println(container1.size());
//		System.out.println(container2.size());
		for(Audio file1:container1){
			for(Audio file2:container2){
				if (isMatch(file1,file2)) {
					String msg="MATCH "+file1.getFileName()+" "+file2.getFileName() +
							" MSE: "+ComparatorAlgorithm.calculateMSE(file1,file2);
					System.out.println(msg);
				} else {
//					System.out.println("ahaha");
				}
			}
		}
	}
	public static boolean isMatch(Audio file1, Audio file2){
		// fast method
		if(Math.abs(file1.getAudioLength()-file2.getAudioLength()) > SECOND){
			return false;
		}
		// accurate method
		else{
			double meanSquaredError = ComparatorAlgorithm.calculateMSE(file1,file2);
			return meanSquaredError < THRESHOLD;
		}
	}
}
