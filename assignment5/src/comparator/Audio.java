package comparator;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;
/*
 */

// Audio class
public class Audio {
	private static boolean DEBUG=true;
	String fileName;
	double sampleRate;
	String waveFormat;
	int bitesPerSecond;
	
	byte[] fileArray;
	double[] timeZoneData;
	double[] frequenciesData;
	/** PUBLIC METHOD	**/
	public Audio(String filePath){
		String[] strs = filePath.split("/");
		this.fileName=strs[strs.length-1];
		File file = new File(filePath);
		this.fileArray=readFile2ByteArray(file);
		
		byte[] header=Arrays.copyOfRange(fileArray, 0, 36);
		if(this.checkCDSpecs(header)!=0){
				System.err.println("File one does not match CD specification");
				System.exit(1);
		}
		sampleRate=getSampleRate(header);
		waveFormat=getWaveFormat(header);
		bitesPerSecond=getBitsPerSample(header);
	
		byte[] fileLeftChannel = extractLeftChannels();	
		timeZoneData = convertToDoubles(fileLeftChannel);
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
		sb.append("Wave Format: "+this.waveFormat+"\n");
		sb.append("Sample Rate: "+this.sampleRate+"\n");
		sb.append("Bites per Second: "+this.bitesPerSecond+"\n");
		return sb.toString();
	}
	// GET METHODs
	public String getFileName(){
		return this.fileName;
	}
	public double[] getFrequencies(){
		return this.frequenciesData;
	}
	public double[] getTimeZoneData(){
		return this.timeZoneData;
	}
	
	/** Private Methods 		**/
	private double getSampleRate(byte[] bytes){
		return 44.1;
	}
	private String getWaveFormat(byte[] bytes){
		return "PCM";
	}
	private int getBitsPerSample(byte[] bytes){
		return 16;
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
	private int checkCDSpecs(byte[] bytes) {
			//Check format = wave
			if (bytes[8] == 87 && bytes[9] == 65 &&
					bytes[10] == 86 && bytes[11] == 69) {
				if (DEBUG) { System.out.println("Wave Format"); }
				//Check audio format = PCM
				if (bytes[20] == 1 && bytes[21] == 0) {
					if (DEBUG) { System.out.println("PCM"); }
					//Check channels = stereo
					if (bytes[22] == 2 && bytes[23] == 0) {
						if (DEBUG) {System.out.println("Stereo Channels"); }
						//Check sample rate is 44.1 kHz
						if (bytes[24] == 68 && bytes[25] == -84
								&& bytes[26] == 0 && bytes[27] == 0) {
							if (DEBUG) { 
								System.out.println("44100 Sample Rate "); }
							if (bytes[34] == 16 && bytes[35] == 0) {
								if (DEBUG) {
									System.out.println("16 bits per sample");
								}
								return 0;
							}
						}
					}
				}
			}
			return -1;
	}

	
	//Extract left channel bytes
	private  byte[] extractLeftChannels() {
		byte[] fileLeftChannel;
		fileLeftChannel = new byte[fileArray.length - 44 / 2];
		for (int i = 44; i < fileArray.length / 2; i+=2) {
			fileLeftChannel[i] = fileArray[2*i];
			fileLeftChannel[i] = fileArray[2*i + 1];
		}		
		return fileLeftChannel;
	}
	
	//Converts byte arrays to double arrays so that they can be
	//passed through the FFT code
	private  double[] convertToDoubles(byte[] fileLeftChannel) {
		double[] fileDouble;
		ByteBuffer byteBuffer = ByteBuffer.wrap(fileLeftChannel);
		fileDouble = new double[fileLeftChannel.length / 2];
		int i = 0;
		while (byteBuffer.remaining() > 2) {
			short t = byteBuffer.getShort();
			fileDouble[i] = t / 32768.0;
			i++;
		}
		return fileDouble;
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
	

}