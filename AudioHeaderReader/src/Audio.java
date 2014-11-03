
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;



// Audio class
public abstract class Audio {
	String fileName;
	String format;
	int sampleRate;
	int bitesPerSecond;
	int numChannels;
	int audioLength;
	
	byte[] fileArray;
	short[] dualChannelSamples;
	double[] timeZoneData;
	double[] frequenciesData;
	
	public static void main(String[] args){
		String path="curieuse.wav";
		Audio audio= Audio.getInstance(path);
		System.out.println(audio);
	}
	
	abstract int checkCDSpecs(byte[] bytes,int datasize,int[] sampleRate ,
			String[] format, int[] bps,int[] nc);
	abstract  byte[] extractLeftChannels(int numsOfChannel,int bps);
	abstract byte[] convertToDualChannels(int bps);
	abstract double[] convertToDoubles(byte[] fileLeftChannel,int bps);
	abstract short[] convertToShort(byte[] fileLeftChannel,int bps);
	
	public static Audio getInstance(String filePath){
		if(filePath.endsWith("wav")){
			return  WavAudio.getInstance(filePath);
		}
		if(filePath.endsWith("mp3")){
			return  Mp3Audio.getInstance(filePath);
		}
		return null;
	}
	
	protected Audio(String filePath){
		String[] strs = filePath.split("/");
		this.fileName=strs[strs.length-1];
		File file = new File(filePath);
		this.fileArray=readFile2ByteArray(file);
		
		byte[] header=Arrays.copyOfRange(fileArray, 0, 44);
		int[] sr=new int[1];
		String[] wf=new String[1];
		int[] bps  =new int[1];
		int[] nc   =new int[1];
		if(this.checkCDSpecs(header,fileArray.length-44,sr,wf,bps,nc)!=0){
				System.err.println("File does not match CD specification: " + filePath);
				System.exit(1);
		}
		sampleRate=sr[0];
		format=wf[0];
		bitesPerSecond=bps[0];
		numChannels=nc[0];
		
		final byte[] fileDualChannels;
		if (this.numChannels == 1) {
		    fileDualChannels = convertToDualChannels(this.bitesPerSecond);
		} else {
		    fileDualChannels = this.fileArray;
		}

		timeZoneData = convertToDoubles(fileDualChannels,this.bitesPerSecond);
		dualChannelSamples=convertToShort(fileDualChannels,this.bitesPerSecond);
		this.audioLength=(this.fileArray.length-44)/(this.sampleRate*this.numChannels*(this.bitesPerSecond/8));
	}

	
	//Calculates the mean squared error for comparing
	//two files. 
	public static double calculateMSE(Audio file1, Audio file2) {
		int indexFor20Hz = (int)(Math.floor(20 * 
				file1.frequenciesData.length / file1.getSampleRate()));
		int indexFor20000Hz = (int)(Math.ceil(20000 * 
				file1.frequenciesData.length / file1.getSampleRate()));
		double squaredError = 0;
		double meanSquaredError = 0;
		for (int j = indexFor20Hz; j < indexFor20000Hz; j++) {
			double value1 = file1.frequenciesData[j];
			double value2 = file2.frequenciesData[j];
			double error = Math.pow((value1 - value2), 2);
			squaredError += error;
		}
		meanSquaredError = squaredError / file1.frequenciesData.length;
		
		return meanSquaredError;
	}
	//@overwrite
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append("Filename: "+this.fileName+"\n");
		sb.append("Audio Format: "+this.format+"\n");
		sb.append("Sample Rate: "+this.sampleRate+"\n");
		sb.append("Bites per Second: "+this.bitesPerSecond+"\n");
		sb.append("Number of Channels: "+this.numChannels+'\n');
		sb.append("Audio length: "+this.audioLength+" seconds");
		sb.append('\n');
		return sb.toString();
	}

	// read file to array
	private byte[] readFile2ByteArray(File file){
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
		return fileName;
	}

	public double getSampleRate() {
		return sampleRate;
	}
	public String getFormat() {
		return format;
	}

	public int getBitesPerSecond() {
		return bitesPerSecond;
	}

	public int getNumChannels() {
		return numChannels;
	}

	public byte[] getFileArray() {
		return fileArray;
	}

	public short[] getdualChannelSamples() {
		return dualChannelSamples;
	}

	public double[] getTimeZoneData() {
		return timeZoneData;
	}

	public double[] getFrequenciesData() {
		return frequenciesData;
	}
	public int getAudioLength(){
		return this.audioLength;
	}

}
