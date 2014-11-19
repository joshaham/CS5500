package assignment7;

import java.io.IOException;
/**
 * Convert all the audio files to standard format:
 * : wav format
 * : 44100 sample rate
 * : 16 bit per sample
 * @author zhuoli
 *
 */
public class Convert2StandardFormat {
	
	static int count=0;
	public static String standardFormat(String filePath){
		String formatFile = filePath;
		if(filePath.endsWith(".mp3")){
			formatFile=convertFromMp3ToWav(filePath);
		}
		else if(filePath.endsWith(".wav")){
			// to be implemented
			// standard sample rate, 16 bite per sample
			String fileWav="/tmp/assignment7Sanguoyanyi" + (count++)+".wav";
			String cmd="cp "+filePath+" "+fileWav;
			Process p=null;
			try {
				p = java.lang.Runtime.getRuntime().exec(cmd);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{
			if(Audio.DEBUG){
				System.err.println("ERROR: file not ends with audio format");
			}
			formatFile=null;
		}
		return formatFile;
		
	}
	
	// convert mp3 file to wav file
	private static String convertFromMp3ToWav(String filePath){
		String fileWav="/tmp/assignment7Sanguoyanyi" + (count++)+".wav";
		String cmd="./lame --decode "+filePath+" "+fileWav;
		Process p=null;
		try {
			p = java.lang.Runtime.getRuntime().exec(cmd);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		synchronized(p){
			try {
				p.wait(3000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return fileWav;
	}
	
}
