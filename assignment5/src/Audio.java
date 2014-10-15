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
	double[] fileFFT;
	
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
		

		byte[] fileLeftChannel = extractLeftChannels();	
		double[] fileDouble = convertToDoubles(fileLeftChannel);
		double[] fileImg=applyFFT(fileDouble);
		fileFFT=convertToFrequencies(fileImg,fileDouble);
	}	
	
	//Calculates the mean squared error for comparing
	//two files
	public static double calculateMSE(Audio file1, Audio file2) {
		double squaredError = 0;
		double meanSquaredError = 0;
		for (int j = 0; j < file1.fileFFT.length; j++) {
			double value1 = file1.fileFFT[j];
			double value2 = file2.fileFFT[j];
			double error = Math.pow((value1 - value2), 2);
			squaredError += error;
		}
		meanSquaredError = squaredError / file1.fileFFT.length;
		return meanSquaredError;
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
	
	//Applies the FFT to the two double arrays
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
		double[] fileFFT;
		fileFFT = new double[fileDouble.length / 2];
		for (int j = 0; j < fileFFT.length; j++) {
			double real = fileDouble[j];
			double img = fileImg[j];
			double freq = Math.sqrt(real*real + img*img);
			fileFFT[j] = freq;
		}
		return fileFFT;
	}
	

}