package audio;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import comparator.FFT;

// Audio class
public abstract class Audio {
	String fileName;
	String format;
	double sampleRate;
	int bitesPerSecond;
	int numChannels;
	
	byte[] fileArray;
	short[] leftChannelSamples;
	double[] timeZoneData;
	double[] frequenciesData;
	
	abstract int checkCDSpecs(byte[] bytes,int datasize,double[] sampleRate ,String[] format, int[] bps,int[] nc);
	abstract  byte[] extractLeftChannels(int numsOfChannel,int bps);
	abstract double[] convertToDoubles(byte[] fileLeftChannel,int bps);
	abstract short[] convertToShort(byte[] fileLeftChannel,int bps);
	
	public static Audio getInstance(String filePath){
		if(filePath.endsWith("wav")){
			return new WavAudio(filePath);
		}
		if(filePath.endsWith("mp3")){
			return new Mp3Audio(filePath);
		}
		return null;
	}
	
	protected Audio(String filePath){
		String[] strs = filePath.split("/");
		this.fileName=strs[strs.length-1];
		File file = new File(filePath);
		this.fileArray=readFile2ByteArray(file);
		
		byte[] header=Arrays.copyOfRange(fileArray, 0, 44);
		double[] sr=new double[1];
		String[] wf=new String[1];
		int[] bps  =new int[1];
		int[] nc   =new int[1];
		if(this.checkCDSpecs(header,fileArray.length-44,sr,wf,bps,nc)!=0){
				System.err.println("File one does not match CD specification");
				System.exit(1);
		}
		sampleRate=sr[0];
		format=wf[0];
		bitesPerSecond=bps[0];
		numChannels=nc[0];
		
		
		final byte[] fileLeftChannel = extractLeftChannels(this.numChannels,this.bitesPerSecond);	
		timeZoneData = convertToDoubles(fileLeftChannel,this.bitesPerSecond);
		leftChannelSamples=convertToShort(fileLeftChannel,this.bitesPerSecond);
		double[] fileImg=applyFFT(timeZoneData);
		frequenciesData=convertToFrequencies(fileImg,timeZoneData);
	}

	
	//Calculates the mean squared error for comparing
	//two files
	public static double calculateMSE(Audio file1, Audio file2) {
		double squaredError = 0;
		double meanSquaredError = 0;
		for (int j = 0; j < file1.frequenciesData.length; j++) {
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

	
	//Applies the FFT to the double arrays
	private double[] applyFFT(double[] fileDouble) {
		double[] fileImg;
		//create arrays to store imaginary components of frequencies
		fileImg = new double[fileDouble.length];
		
		//Convert to frequency domain using FFT
		FFT fft = new FFT((int) Math.pow(2, 15));
		fft.fft(fileDouble, fileImg);
		return fileImg;
	}
	
	//Convert the real and imaginary components of results to
	//frequencies using sqrt (real * real + img * img)
	private double[] convertToFrequencies(double[] fileImg,double[] fileDouble) {
		double[] frequenciesData;
		frequenciesData = new double[fileDouble.length / 2];
		for (int j = 0; j < frequenciesData.length; j++) {
			double real = fileDouble[j];
			double img = fileImg[j];
			double freq = Math.sqrt(real*real + img*img);
			frequenciesData[j] = freq;
		}
		return frequenciesData;
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

	public short[] getLeftChannelSamples() {
		return leftChannelSamples;
	}

	public double[] getTimeZoneData() {
		return timeZoneData;
	}

	public double[] getFrequenciesData() {
		return frequenciesData;
	}

}
