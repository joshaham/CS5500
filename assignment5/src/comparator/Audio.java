package comparator;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
/*
 */

// Audio class
public class Audio {
	private static boolean DEBUG=true;
	String fileName;
	double sampleRate;
	String format;
	int bitesPerSecond;
	int numChannels;
	
	byte[] fileArray;
	short[] leftChannelSamples;
	double[] timeZoneData;
	double[] frequenciesData;
	/** PUBLIC METHOD	**/
	public Audio(String filePath){
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
	// GET METHODs
	public String getFileName(){
		return this.fileName;
	}
	public double[] getFrequencies(){
		return this.frequenciesData;
	}
	public short[] getTimeZoneData(){
		return this.leftChannelSamples;
	}
	public double getSampleRate(){
		return this.sampleRate;
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
	// check audio file headline format
	private int checkCDSpecs(byte[] bytes,int datasize,double[] sampleRate ,String[] format, int[] bps,int[] nc) {
			//Check format = wave
			if (bytes[8] != 87 || bytes[9] != 65 ||
					bytes[10] != 86 || bytes[11] != 69) {
				if (DEBUG) { System.err.println("ERROR: NOT Wave Format"); }
				return -1;
				
			}
			format[0]="wav";
			//Check audio format = PCM
			if (bytes[20] != 1 || bytes[21] != 0) {
				if (DEBUG) { System.err.println("ERROR: NOT PCM"); }
				return -1;
			}
			//Check channels = stereo or Mono
			if (bytes[22] == 2 && bytes[23] == 0) {
				nc[0]=2;
			}else if(bytes[22]==1 && bytes[23]==0){
				nc[0]=1;
			}else{
				if(DEBUG){System.err.println("ERROR: Incorrect num of channels");}
				return -1;
			}
			//Check sample rate is 44.1 kHz
			long sr=bytes[24]+bytes[25]<<8+bytes[26]<<16+bytes[27]<<32;
			if(sr!=11025 && sr!=22050 && sr!=44100 && sr!=48000){
				if(DEBUG){System.err.println("ERROR: Incorrect sample rate "+sr);}
				return -1;
			}
			sampleRate[0]=sr;
			//check BitsPerSample is 16
			int t=bytes[34]+bytes[35]<<8;
			if (t != 16 && t != 8) {
				if (DEBUG) {System.out.println("ERROR: Incorrect bites per sample "+ t);}
				return -1;
			}
			bps[0]=t;
			//check file size
			int subChunk2Size=bytes[36]+bytes[37]<<8;
			if(subChunk2Size!=datasize){
				if(DEBUG){System.out.println("Audio File data incomplete");};
				return -1;
			}
			return 0;
	}

	
	//Extract left channel bytes
	// GIVEN: nums of channel, bites per sample
	private  byte[] extractLeftChannels(int numsOfChannel,int bps) {
		// subtract 44 head bytes, divide by numsOfChannel leaving one channel
		byte[] fileLeftChannel = new byte[(fileArray.length - 44) / numsOfChannel];
		if(bps==16){
			for(int i=0;i<fileLeftChannel.length/2;i++){
				fileLeftChannel[i*2]=fileArray[44+i*2*numsOfChannel];
				fileLeftChannel[i*2+1]=fileArray[44+i*2*numsOfChannel+1];
			}
		}else if(bps==8){
			for(int i=0;i<fileLeftChannel.length;i++){
				fileLeftChannel[i]=fileArray[44+i*numsOfChannel];
			}
		}else{
			if(DEBUG){System.err.println("ERROR: incorrect bps in extractLeftChannels "+bps);}
			return null;
		}
		return fileLeftChannel;
	}
	
	//Converts byte arrays to double arrays so that they can be
	//passed through the FFT code
	private  double[] convertToDoubles(byte[] fileLeftChannel,int bps) {
		double[] fileDouble=null;
//		ByteBuffer byteBuffer = ByteBuffer.wrap(fileLeftChannel);
		if(bps==16){
			fileDouble = new double[fileLeftChannel.length / 2];
			for(int i=0;i<fileDouble.length;i++){
				short t=(short) (fileLeftChannel[i*2]+fileLeftChannel[i*2+1]<<8);
				fileDouble[i]=t/32768.0;
			}
		}else if(bps==8){
			fileDouble = new double[fileLeftChannel.length];
			for(int i=0;i<fileDouble.length;i++){
				fileDouble[i]=fileLeftChannel[i]/125.0;
			}
		}else{
			if(DEBUG){System.err.println("ERROR: incorrect bps in convertToDoubles "+bps);}
			return null;
		}
		return fileDouble;
	}
	// converts byte arrays to short int
	private short[] convertToShort(byte[] fileLeftChannel,int bps){
		short[] array;
		if(bps==16){
			array = new short[fileLeftChannel.length / 2];
			for(int i=0;i<array.length;i++){
				short t=(short) (fileLeftChannel[i*2]+fileLeftChannel[i*2+1]<<8);
				array[i]=t;
			}
		}else if(bps==8){
			array = new short[fileLeftChannel.length];
			for(int i=0;i<array.length;i++){
				array[i]=fileLeftChannel[i];
			}
		}else{
			if(DEBUG){System.err.println("ERROR: incorrect bps in convertToShort "+bps);}
			return null;
		}
		return array;
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
