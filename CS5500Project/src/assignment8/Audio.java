package assignment8;
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
	AudioSpectrogram spectrogram;
	byte[] fileArray;
	double[] dualChannelSamples;
	long hashValue=0;
	// bin size
	int songSampleSize = 5;
	HashMap<Long,String> hm=null;
	
	// return HashValue array, 
	// the index of array represent the corresponding time slot of each bin
	public HashMap<Long,String> getHashMap(){
//		if(hm==null){
//			hm = spectrogram.getBinHashMap(getFileName(),songSampleSize);
//		}
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

		double[] datas=getScaledDataForFFTCalculate(fileArray,header);
		this.dualChannelSamples=getChannelSamples(fileArray,header);
		spectrogram=new AudioSpectrogram(datas,this.header);
		hm = spectrogram.getBinHashMap(getFileName(),songSampleSize);
	}
	
	// return scaled data for fft transform [-1,+1]
	public double[] getScaledDataForFFTCalculate(byte[] fileArray,AudioHeader header){
		double[] datas=null;
		byte[] oneChannel;
		byte[] fileLeftChannel;
		byte[] fileRightChannel;
		if(header.getNumChannels()==1){
			oneChannel=extractMonoChannel(header.getBitesPerSecond());
			datas = monoConvert2Doubles(oneChannel,header.getBitesPerSecond());
		}else{
			fileLeftChannel= new byte[(fileArray.length - 44) / 2];
			fileRightChannel=new byte[(fileArray.length - 44) / 2];
			extractBothChannels(header.getBitesPerSecond(),this.fileArray,fileLeftChannel,fileRightChannel);
			datas = StereoConvert2Doubles(fileLeftChannel,fileRightChannel,
							header.getBitesPerSecond());
		}
		return datas;
	}
	// return unscaled data for fft transform
	public double[] getChannelSamples(byte[] fileArray,AudioHeader header){
		double[] channelSamples=null;
		byte[] oneChannel;
		byte[] fileLeftChannel;
		byte[] fileRightChannel;
		if(header.getNumChannels()==1){
			oneChannel=extractMonoChannel(header.getBitesPerSecond());
			channelSamples=
					convertToDouble(oneChannel,header.getBitesPerSecond());
		}else{
			fileLeftChannel= new byte[(fileArray.length - 44) / 2];
			fileRightChannel=new byte[(fileArray.length - 44) / 2];
			extractBothChannels(header.getBitesPerSecond(),this.fileArray,fileLeftChannel,fileRightChannel);

			double[] leftShort=
					convertToDouble(fileLeftChannel,
							header.getBitesPerSecond());
			double[] rightShort=
					convertToDouble(fileRightChannel,
							header.getBitesPerSecond());
			channelSamples=new double[leftShort.length];
			for(int i=0;i<leftShort.length;i++){
				channelSamples[i]=leftShort[i]/2+rightShort[i]/2;
			}
		}
		return channelSamples;
	}

	
	void extractBothChannels(int bps,byte[]fileArray, byte[]fileLeftChannel, byte[] fileRightChannel){
		int base=44;
		if(bps==16){
			for(int i=0;i<(fileArray.length-44)/4;i++){
				fileLeftChannel[i*2]=fileArray[base+i*4+0];
				fileLeftChannel[i*2+1]=fileArray[base+i*4+1];
				fileRightChannel[i*2]=fileArray[base+i*4+2];
				fileRightChannel[i*2+1]=fileArray[base+i*4+3];
			}
		}else if(bps==8){
			for(int i=0;i<(fileArray.length-44)/2;i++){
				fileLeftChannel[i]=fileArray[base+i*2+0];
				fileRightChannel[i]=fileArray[base+i*2+1];
			}
		}else{
			if(DEBUG){
				System.err.println(
						"ERROR: incorrect bps in extractLeftChannels "+bps);}
			System.exit(1);
		}
	}
	 byte[] extractMonoChannel(int bps){
		 return Arrays.copyOfRange(fileArray, 44, fileArray.length);
	 }
	//Converts byte arrays to double arrays so that they can be
	//passed through the FFT code
	 // RETURN: double array in range [-1,+1];
	 double[] monoConvert2Doubles(byte[] fileLeftChannel,int bps) {
		double[] fileDouble=null;
//		ByteBuffer byteBuffer = ByteBuffer.wrap(fileLeftChannel);
		if(bps==16){
			fileDouble = new double[fileLeftChannel.length / 2];
			for(int i=0;i<fileDouble.length;i++){
				short t=(short) 
						(fileLeftChannel[i*2]+fileLeftChannel[i*2+1]<<8);
				fileDouble[i]=t/32768.0;
			}
		}else if(bps==8){
			fileDouble = new double[fileLeftChannel.length];
			for(int i=0;i<fileDouble.length;i++){
				fileDouble[i]=fileLeftChannel[i]/125.0;
			}
		}else{
			if(DEBUG){System.err.println(
					"ERROR: incorrect bps in convertToDoubles "+bps);}
			return null;
		}
		return fileDouble;
	}
	//Converts byte arrays to double arrays so that they can be
	//passed through the FFT code
	 double[] StereoConvert2Doubles(byte[] fileLeftChannel, 
			 byte[] fileRightChannel,int bps) {
		double[] leftChannel=this.monoConvert2Doubles(fileLeftChannel, bps);
		double[] rightChannel=this.monoConvert2Doubles(fileRightChannel, bps);
		double[] fileDouble=new double[leftChannel.length];
		for(int i=0;i<rightChannel.length;i++){
			fileDouble[i]=leftChannel[i]/2.0+rightChannel[i]/2.0;
		}
		return fileDouble;
	}
		// converts byte arrays to double
	 // Return int array 
	 double[] convertToDouble(byte[] fileLeftChannel,int bps){
			double[] array;
			if(bps==16){
				array = new double[fileLeftChannel.length / 2];
				for(int i=0;i<array.length;i++){
					int t=(fileLeftChannel[i*2]+fileLeftChannel[i*2+1]<<8);
					array[i]=t;
				}
			}else if(bps==8){
				array = new double[fileLeftChannel.length];
				for(int i=0;i<array.length;i++){
					array[i]=fileLeftChannel[i];
				}
			}else{
				if(DEBUG){System.err.println(
						"ERROR: incorrect bps in convertToShort "+bps);}
				return null;
			}
			return array;
		}
	//@overwrite
	public String toString(){
		String str= header.toString();
		int n = this.spectrogram.getLocalPeaks().length;
		String peakSize ="Peak numbers: "+n+'\n';
		if(this.hashValue==0){
			this.hashValue=Hashfp.gethash(spectrogram.getLocalPeaks());
		}
		String hashvalue ="HashValue: "+this.hashValue+'\n';
		return str+peakSize+hashvalue ;
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
	@Override
	public int hashCode(){
		if(this.hashValue==0){
			this.hashValue=Hashfp.gethash(spectrogram.getLocalPeaks());
		}
		return (int) this.hashValue;
	}
	
	// for test
	public static void main(String[] args){
		String filePath="A5/D1/curieuse2.wav";
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
		
		filePath="A5/D2/curieuse.wav";
		paths=Assignment8.getFilePaths(filePath, "-f");
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
		

	}

}
