package assignment7;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.lang.System;

//import drawer.SpectrogramDrawer;



// Audio class 
public  class Audio {
	static boolean DEBUG=true;
	AudioHeader header;
	AudioSpectrogram spectrogram;
	byte[] fileArray;
	double[] dualChannelSamples;
	long hashValue=0;
	double songSampleSize = 5;
	static HashMap<Long, String> hm = new HashMap();
	
	public String[] getPeaks(){
		return spectrogram.getLocalPeaks();
	}
	
	// for test
	public static void main(String[] args){
		String filePath="A5/D1";
		String[] paths=Assignment7.getFilePaths(filePath, "-d");
		for(String path : paths){
			Audio audio=null;
				audio = Audio.getInstance(path);
			if(audio==null){
				continue;
			}
//			System.out.println(audio.getFileName()+"\n"+audio.hashValue+'\n');
//			SpectrogramDrawer.drawSpectrogram(audio.getFileName(),audio.spectrogram);
		}
		

		String filePath2="A5/D2";
		String[] paths2=Assignment7.getFilePaths(filePath2, "-d");
		for(String path : paths2){
			Audio audio=null;
				audio = Audio.getInstance(path);
			if(audio==null){
				continue;
			}
//			System.out.println(audio.getFileName()+"\n"+audio.hashValue+'\n');
//			SpectrogramDrawer.drawSpectrogram(audio.getFileName(),audio.spectrogram);
		}

	}
	
	public static Audio getInstance(String filePath){
		Audio audio=null;
		// convert audio file to standard format if not,
		// return the file path of standard format file.
		// return null, if filePath is not audio file
		String fileStandardPath = 
				Convert2StandardFormat.standardFormat(filePath);
		if(fileStandardPath==null){
			return null;
		}
		audio=getInstanceHelper(fileStandardPath,filePath);
		return audio;
	}
	
	
	// change filename if in need
	private static Audio getInstanceHelper(String fileStandardPath,String filePath){
		Process p = null;
		Audio audio=checkAndGetInstance(fileStandardPath,filePath);
		// remove standardPath in tmp directory
		if(!fileStandardPath.equals(filePath)){
			try {
					p =java.lang.Runtime.getRuntime().exec("rm "+fileStandardPath);
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
		return audio;
	}

	// check file format and return audio instance if correct
	private static Audio checkAndGetInstance(String standardFilePath,String actualPath)
	{

		String[] strs = actualPath.split("/");
		String fileName=strs[strs.length-1];
		File file = new File(standardFilePath);
		if(!file.exists() || !file.isFile()){
//			System.err.println("ERROR: File not exist, "+getCurrentDir()+"/"+standardFilePath);
			System.err.println("ERROR: File does not exist, "+actualPath);
			return null;
		}
		byte[] fileArray=readFile2ByteArray(file);
		byte[] dataheader=Arrays.copyOfRange(fileArray, 0, 44);
		AudioHeader header=
				AudioHeader.getInstance(fileName,dataheader,
						fileArray.length-44);
		if(header==null){
			System.err.println("File does not match CD specification: " 
		+ strs[strs.length-1]);
			return null;
		}
		return new Audio(header,fileArray);
	}
	
	
	protected Audio(AudioHeader header,byte[] fileArray){
		this.header=header;
		this.fileArray=fileArray;
		byte[] oneChannel;
		byte[] fileLeftChannel;
		byte[] fileRightChannel;
		
		double[] datas=null;
		if(header.getNumChannels()==1){
			oneChannel=extractMonoChannel(header.getBitesPerSecond());
			datas = monoConvert2Doubles(oneChannel,header.getBitesPerSecond());
			dualChannelSamples=
					convertToDouble(oneChannel,header.getBitesPerSecond());
		}else{
			fileLeftChannel=extractLeftChannels(header.getBitesPerSecond());
			fileRightChannel=extractRightChannels(header.getBitesPerSecond());
			datas = 
					StereoConvert2Doubles(
							fileLeftChannel,fileRightChannel,
							header.getBitesPerSecond());
			double[] leftShort=
					convertToDouble(fileLeftChannel,header.getBitesPerSecond());
			double[] rightShort=
					convertToDouble(fileRightChannel,header.getBitesPerSecond());
			dualChannelSamples=new double[leftShort.length];
			for(int i=0;i<leftShort.length;i++){
				dualChannelSamples[i]=leftShort[i]/2+rightShort[i]/2;
			}
		}
//		spectrogram=new AudioSpectrogram(this.dualChannelSamples,datas,this.header);
//		hashValue=hashfp.gethash(spectrogram.getLocalPeaks());
		hashData(datas);
	}

	
	//converts every 5 second interval as well as every 1 second offset into
	//hashKeys which are then stored with the corresponding value indicating
	//the song name, beginning second, and end second of that song segment.
	void hashData(double[] datas) {
	    int arraySize = (int) (this.getSampleRate() * songSampleSize);
	    int fiveSecIntervals = (int) Math.ceil
	            (((double)datas.length) / arraySize);
	    int numOffsets = 5;
	    
	    for (int i = 0; i < numOffsets; i++) {
	        int offset = i * (int)(this.getSampleRate());
	        int begIndex = 0;
	        int iteration = 1;
	        
	        //beginning index is incremented by the arraySize every iteration
	        //length is either the pre-specified array size or the distance
	        //from the beginning index to the end of the file, whichever is smaller
	        while (begIndex < datas.length){
	            int length;
	            if (begIndex + arraySize > datas.length) {
	                length = datas.length - begIndex;
	            } else {
	                length = arraySize;
	            }
	            
	            //copies the dualChannel and datas array segments being transformed
	            //into temporary arrays
	            double[] tempDualChannels = new double[(int) Math.pow(2, 15)];
	            double[] tempDatas = new double[(int) Math.pow(2, 15)];
	            System.arraycopy(this.dualChannelSamples, begIndex,
	                    tempDualChannels, 0, length);
	            System.arraycopy(datas, begIndex,
	                    tempDatas, 0, length);
	            
	            //get the spectrogram and convert to the hashkey
	            spectrogram = new AudioSpectrogram(tempDualChannels, 
	                    tempDatas, this.header);
	            long hashKey = hashfp.gethash(spectrogram.getLocalPeaks());
	            
	            //the corresponding hashvalue is stored as a string with format
	            //SongName;BeginningSecondofChunk;EndingSecondofChunk
	            String songName = this.getFileName();
	            int begSec = (int) (begIndex / this.getSampleRate());
	            int endSec = (int) ((begIndex + length) / this.getSampleRate());
	            
	            String hashValue = (songName + ";" + begSec + ";" + endSec);
	            hm.put(hashKey, hashValue);
	            
	            begIndex = iteration * arraySize + offset;
	            iteration++;
	        }
	    }
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
			if(DEBUG){
				System.err.println(
						"ERROR: incorrect bps in extractLeftChannels "+bps);}
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
			if(DEBUG){
				System.err.println(
						"ERROR: incorrect bps in extractLeftChannels "+bps);}
			return null;
		}
		return fileLeftChannel;
		
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
		String hashvalue ="HashValue: "+this.hashValue+'\n';
		return str+peakSize+hashvalue ;
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
		return (int) this.hashValue;
	}
	
	public HashMap<Long, String> getHashMap() {
	    return this.hm;
	}

}
