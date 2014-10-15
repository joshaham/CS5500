import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;


// Audio class
public class Audio {
	String fileName;
	byte[] fileArray;
	byte[] fileLeftChannel;
	double[] fileDouble;
	double[] fileImg;
	
	public Audio(String filePath){
		String[] strs = filePath.split("/");
		this.fileName=strs[strs.length-1];
		File file = new File(filePath);
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
	}
	//Extract left channel bytes
	private  byte[] extractLeftChannels() {
		fileLeftChannel = new byte[fileArray.length - 44 / 2];
		for (int i = 44; i < fileArray.length / 2; i+=2) {
			fileLeftChannel[i] = fileArray[2*i];
			fileLeftChannel[i] = fileArray[2*i + 1];
		}		
		return fileLeftChannel;
	}
	
	//Converts byte arrays to double arrays so that they can be
	//passed through the FFT code
	private  double[] convertToDoubles() {
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
	
	//Applies the FFT to the two double arrays
	private double[] applyFFT() {
		//create arrays to store imaginary components of frequencies
		fileImg = new double[fileDouble.length];
		
		//Convert to frequency domain using FFT
		FFT fft = new FFT((int) Math.pow(2, 15));
		fft.fft(fileDouble, fileImg);
		return fileImg;
	}
	
	//Convert the real and imaginary components of results to
	//frequencies using sqrt (real * real + img * img)
	private static void convertToFrequencies() {
		fileOneFFT = new double[fileOneDouble.length / 2];
		fileTwoFFT = new double[fileTwoDouble.length / 2];
		for (int j = 0; j < fileOneFFT.length; j++) {
			double real = fileOneDouble[j];
			double img = fileOneImg[j];
			double freq = Math.sqrt(real*real + img*img);
			fileOneFFT[j] = freq;
		}
		for (int j = 0; j < fileTwoFFT.length; j++) {
			double real = fileTwoDouble[j];
			double img = fileTwoImg[j];
			double freq = Math.sqrt(real*real + img*img);
			fileTwoFFT[j] = freq;
		}
		
	}
}
