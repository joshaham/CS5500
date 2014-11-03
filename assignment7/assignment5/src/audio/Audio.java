package audio;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;



// Audio class
public  class Audio {
	static boolean DEBUG=true;
	static int count=1;
	AudioHeader header;
	byte[] fileArray;
	int[] dualChannelSamples;
	double[] timeZoneData;
	double[] frequenciesData;
	
	// for test
	public static void main(String[] args){
		String path="examples/janacek2.wav";
		Audio audio=null;
		try {
			audio = Audio.getInstance(path);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(audio);
	}
	
	public static Audio getInstance(String filePath) throws IOException{
		if(filePath.endsWith(".mp3")){
			String fileWav="/tmp/assignment7Sanguoyanyi" + count+".wav";
			String cmd="./lame --decode "+filePath+" "+fileWav;
			java.lang.Runtime.getRuntime().exec(cmd);
			Audio instance =   checkAndGetInstance(fileWav);
//			java.lang.Runtime.getRuntime().exec("rm "+fileWav);
			return instance;
		}
		else if(filePath.endsWith(".wav")){
			return   checkAndGetInstance(filePath);
		}else{
			if(DEBUG){
				System.err.println("ERROR: file not ends with audio format");
			}
		}
		return null;
	}
	
	
	private static Audio checkAndGetInstance(String filePath){

		String[] strs = filePath.split("/");
		File file = new File(filePath);
		if(!file.exists() || !file.isFile()){
			System.err.println("ERROR: File not exist, "+filePath);
			return null;
		}
		byte[] fileArray=readFile2ByteArray(file);
		byte[] dataheader=Arrays.copyOfRange(fileArray, 0, 44);
		AudioHeader header=AudioHeader.getInstance(strs[strs.length-1],dataheader,fileArray.length-44);
		if(header==null){
			System.err.println("File does not match CD specification: " + filePath);
			return null;
		}
		return new Audio(header,filePath,fileArray);
	}
	
	protected Audio(AudioHeader header,String filePath,byte[] fileArray){
		this.header=header;
		this.fileArray=fileArray;
		byte[] oneChannel;
		byte[] fileLeftChannel;
		byte[] fileRightChannel;
		if(header.getNumChannels()==1){
			oneChannel=extractMonoChannel(header.getBitesPerSecond());
			timeZoneData = monoConvert2Doubles(oneChannel,header.getBitesPerSecond());
			dualChannelSamples=convertToShort(oneChannel,header.getBitesPerSecond());
		}else{
			fileLeftChannel=extractLeftChannels(header.getBitesPerSecond());
			fileRightChannel=extractRightChannels(header.getBitesPerSecond());
			timeZoneData = StereoConvert2Doubles(fileLeftChannel,fileRightChannel,header.getBitesPerSecond());
			int[] leftShort=convertToShort(fileLeftChannel,header.getBitesPerSecond());
			int[] rightShort=convertToShort(fileRightChannel,header.getBitesPerSecond());
			dualChannelSamples=new int[leftShort.length];
			for(int i=0;i<leftShort.length;i++){
				dualChannelSamples[i]=leftShort[i]/2+rightShort[i]/2;
			}
		}
		double[] fileImg=applyFFT(timeZoneData);
		frequenciesData=convertToFrequencies(fileImg,timeZoneData);
	}

	

	//Extract left channel bytes
	// GIVEN: nums of channel, bites per sample
	 byte[] extractLeftChannels(int bps) {
		// subtract 44 head bytes, divide by numsOfChannel leaving one channel
		byte[] fileLeftChannel = new byte[(fileArray.length - 44) / 2];
		if(bps==16){
			for(int i=0;i<fileLeftChannel.length/2;i++){
				fileLeftChannel[i*2]=fileArray[44+i*2*2];
				fileLeftChannel[i*2+1]=fileArray[44+i*2*2+1];
			}
		}else if(bps==8){
			for(int i=0;i<fileLeftChannel.length;i++){
				fileLeftChannel[i]=fileArray[44+i*2];
			}
		}else{
			if(DEBUG){System.err.println("ERROR: incorrect bps in extractLeftChannels "+bps);}
			return null;
		}
		return fileLeftChannel;
	}
	byte[] extractRightChannels(int bps){
		// subtract 44 head bytes, divide by numsOfChannel leaving one channel
		byte[] fileLeftChannel = new byte[(fileArray.length - 44) / 2];
		if(bps==16){
			for(int i=0;i<fileLeftChannel.length/2;i++){
				fileLeftChannel[i*2]=fileArray[44+i*2*2+2];
				fileLeftChannel[i*2+1]=fileArray[44+i*2*2+3];
			}
		}else if(bps==8){
			for(int i=0;i<fileLeftChannel.length;i++){
				fileLeftChannel[i]=fileArray[44+i*2+1];
			}
		}else{
			if(DEBUG){System.err.println("ERROR: incorrect bps in extractLeftChannels "+bps);}
			return null;
		}
		return fileLeftChannel;
		
	}
	 byte[] extractMonoChannel(int bps){
		 return Arrays.copyOfRange(fileArray, 44, fileArray.length);
	 }
	//Converts byte arrays to double arrays so that they can be
	//passed through the FFT code
	 double[] monoConvert2Doubles(byte[] fileLeftChannel,int bps) {
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
	//Converts byte arrays to double arrays so that they can be
	//passed through the FFT code
	 double[] StereoConvert2Doubles(byte[] fileLeftChannel, byte[] fileRightChannel,int bps) {
		double[] leftChannel=this.monoConvert2Doubles(fileLeftChannel, bps);
		double[] rightChannel=this.monoConvert2Doubles(fileRightChannel, bps);
		double[] fileDouble=new double[leftChannel.length];
		for(int i=0;i<rightChannel.length;i++){
			fileDouble[i]=leftChannel[i]/2.0+rightChannel[i]/2.0;
		}
		return fileDouble;
	}
		// converts byte arrays to short int
	 int[] convertToShort(byte[] fileLeftChannel,int bps){
			int[] array;
			if(bps==16){
				array = new int[fileLeftChannel.length / 2];
				for(int i=0;i<array.length;i++){
					int t=(fileLeftChannel[i*2]+fileLeftChannel[i*2+1]<<8);
					array[i]=t;
				}
			}else if(bps==8){
				array = new int[fileLeftChannel.length];
				for(int i=0;i<array.length;i++){
					array[i]=fileLeftChannel[i];
				}
			}else{
				if(DEBUG){System.err.println("ERROR: incorrect bps in convertToShort "+bps);}
				return null;
			}
			return array;
		}
	//@overwrite
	public String toString(){
		return header.toString();
	}

	// read file to array
	private static byte[] readFile2ByteArray(File file){
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

	public int[] getdualChannelSamples() {
		return dualChannelSamples;
	}

	public double[] getTimeZoneData() {
		return timeZoneData;
	}

	public double[] getFrequenciesData() {
		return frequenciesData;
	}
	public int getAudioLength(){
		return header.getAudioLength();
	}

}
