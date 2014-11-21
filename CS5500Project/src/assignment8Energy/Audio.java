package assignment8Energy;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;

//import drawer.SpectrogramDrawer;

// Audio class
public  class Audio {
	static boolean DEBUG=true;
	AudioHeader header;
	byte[] fileArray;
	double[] dualChannelSamples;
	long hashValue=0;
	// bin size
	int songSampleSize = 5;
	HashMap<Long,String> hm=null;
	Energy energy=null;
	
	// return HashValue array, 
	// the index of array represent the corresponding time slot of each bin
	public HashMap<Long,String> getHashMap(){
		if(hm==null){
			hm = this.energy.getBinHashMap(getFileName(),songSampleSize);
		}
		return hm;
	}

	// Return instance of Audio
	public static Audio getInstance(String filePath){
		Audio audio=null;
		String fileCanonicalPath = 
				Convert2StandardFormat.Convert2CanonicalFormat(filePath);
		if(fileCanonicalPath==null){
			return null;
		}
		String fileName=getActualName(filePath);
		File file = new File(fileCanonicalPath);
		if(!file.exists() || !file.isFile()){
			return null;
		}
		byte[] fileArray=readFile2ByteArray(file);
		audio = new Audio(fileArray,fileName);
		if(!filePath.equals(fileCanonicalPath)){
			removeCanonicalPath(fileCanonicalPath);
		}
		return audio;
	}
	
	private static void removeCanonicalPath(String fileCanonicalPath) {
		Process p=null;
		try {
			String str="rm "+fileCanonicalPath;
			p =java.lang.Runtime.getRuntime().exec(str);
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
		
	}
	private static String getActualName( String filePath){
		String[] strs = filePath.split("/");
		String fileName=strs[strs.length-1];
		return fileName;
	}
	
	
	private Audio(byte[] fileArray,String fileName){
		byte[] dataheader=Arrays.copyOfRange(fileArray, 0, 44);
		AudioHeader header=
				AudioHeader.getInstance(fileName,dataheader,
						fileArray.length-44);
		this.header=header;
		this.fileArray=fileArray;
		this.energy=new Energy(fileArray,this.header);
//		System.out.println(energy);
	}
	
	//@overwrite
	public String toString(){
		String str= header.toString();
		return str+this.energy.toString()+"\n";
	}

	// read file to array
	static byte[] readFile2ByteArray(File file){
		byte[] fileArray=null;
		try{
			InputStream inputStream = new FileInputStream(file);
			fileArray = new byte[(int)file.length()];
			inputStream.read(fileArray, 0, fileArray.length);
			inputStream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.err.println("File not found");
			System.exit(1);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		} 
		

		return fileArray;
	}

	

	

	public String getFileName() {
		return header.getFileName();
	}

	public double getSampleRate() {
		return header.getSampleRate();
	}
	public String getFormat() {
		return header.getFormat();
	}

	public int getBitesPerSecond() {
		return header.getBitesPerSecond();
	}

	public int getNumChannels() {
		return header.getNumChannels();
	}

	public byte[] getFileArray() {
		return fileArray;
	}

	public double[] getdualChannelSamples() {
		return dualChannelSamples;
	}
	public int getAudioLength(){
		return header.getAudioLength();
	}

	
	// for test
	public static void main(String[] args){
		String filePath="A5/D1/sons2.wav";
		String[] paths=Assignment8.getFilePaths(filePath, "-f");
		for(String path : paths){
			Audio audio=null;
				audio = Audio.getInstance(path);
			if(audio==null){
				continue;
			}
			System.out.println(audio);
//			SpectrogramDrawer.drawSpectrogram(audio.getFileName(),
//			audio.spectrogram);
		}
		
//		filePath="A5/D2/sons.wav";
//		paths=Assignment8.getFilePaths(filePath, "-f");
//		for(String path : paths){
//			Audio audio=null;
//				audio = Audio.getInstance(path);
//			if(audio==null){
//				continue;
//			}
//			System.out.println(audio);
////			SpectrogramDrawer.drawSpectrogram(audio.getFileName(),
////			audio.spectrogram);
//		}
		

	}

}
