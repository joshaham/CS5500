package assignment8Energy;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;


//
// Audio class
public  class Audio {
	static boolean DEBUG=false;
	public String filename = null;
	private int[][] data = null;
	public int[][] getData() {
		return data;
	}

	private Energy energy=null;

	AudioHeader header=null;
	private BufferedInputStream bis = null;
	
//	public int[] getHashValuePerSecondWithOverlap(){
//		return energy.getHashvaluePerSecond();
//	}
	public int[] getPostiveHashValuePerSecondWithOverlap(){
		return energy.getPostiveHashValuePerInterval();
	}
	public int[] getNegativeHashValuePerInterval(){
		return energy.getNegativeHashValuePerInterval();
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
		FileInputStream fis=null;
		BufferedInputStream bis = null;
		try {
			fis = new FileInputStream(fileCanonicalPath);
			bis = new BufferedInputStream(fis);
			audio = new Audio(bis,fileCanonicalPath,fileName);
		} catch (Exception e) {
			if(Audio.DEBUG){
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			audio=null;
		} finally {
			try {
				if (bis != null)
					bis.close();
				if (fis != null)
					fis.close();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
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
	
	public Audio(BufferedInputStream bis, String fileCanonicalPath, String fileName) {
		this.bis=bis;
		this.filename = fileName;
		byte[] fileArray=readFile2ByteArray(new File(fileCanonicalPath));
		byte[] dataheader=Arrays.copyOfRange(fileArray, 0, 44);
		this.header = AudioHeader.getInstance(fileName,dataheader,fileArray.length-44);
		
		readString(WaveConstants.LENCHUNKDESCRIPTOR);
		readLong();
		readString(WaveConstants.LENWAVEFLAG);

		readString(WaveConstants.LENFMTSUBCHUNK);
		readLong();
		readInt();
		readInt();
		readLong();
		readLong();
		readInt();
		readInt();
		readString(WaveConstants.LENDATASUBCHUNK);
		readLong();


		this.data = new int[this.header.numChannels][(fileArray.length-44)/(header.numChannels*header.bitePerSample/8)];

		// read channel data
		for (int i = 0; i < this.data[0].length; ++i) {
			for (int n = 0; n < header.getNumChannels(); ++n) {
				if (header.bitePerSample == 8) {
					try {
						this.data[n][i] = bis.read();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else if (header.bitePerSample == 16) {
					this.data[n][i] = this.readInt();
				}
//				System.out.println(this.data[n][i] );
			}
		}
		energy=new Energy(this.data,header);
	}


	
	private int readInt() {
		byte[] buf = new byte[2];
		int res = 0;
		try {
			if (bis.read(buf) != 2)
				throw new IOException("no more data!!!");
			res = (buf[0] & 0x000000FF) | (((int) buf[1]) << 8);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return res;
	}

	private long readLong() {
		long res = 0;
		try {
			long[] l = new long[4];
			for (int i = 0; i < 4; ++i) {
				l[i] = bis.read();
				if (l[i] == -1) {
					throw new IOException("no more data!!!");
				}
			}
			res = l[0] | (l[1] << 8) | (l[2] << 16) | (l[3] << 24);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return res;
	}

	private String readString(int len) {
		byte[] buf = new byte[len];
		try {
			if (bis.read(buf) != len)
				throw new IOException("no more data!!!");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new String(buf);
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
		return this.header.getFileName();
	}


}
