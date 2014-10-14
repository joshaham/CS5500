import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;


public class TestAssignment5v2 {

	static byte[] fileOneArray;
	static byte[] fileTwoArray;
	
	static byte[] fileOneLeftChannel;
	static byte[] fileTwoLeftChannel;
	
	static double[] fileOneDouble;
	static double[] fileTwoDouble;
	
	static double[] fileOneImg;
	static double[] fileTwoImg;
	
	static double[] fileOneFFT;
	static double[] fileTwoFFT;

	public static void main(String[] args) {


		//check file path is ok, if not return 
		//ERROR: incorrect command line

		//check file extension, if not .wav return
		//ERROR: fileName is not a supported format

		//check file is in WAVE format, if not return
		//ERROR: File not in WAVE format

		//check file lengths, NO MATCH if files are
		//of different lengths

		//extract bytes
		byte[] currentFileArray;
		try {
			File file = new File("wayfaring.wav");
			InputStream inputStream = new FileInputStream(file);
			currentFileArray = new byte[(int)file.length()];
			inputStream.read(currentFileArray, 0, currentFileArray.length);
			fileOneArray = currentFileArray;
			fileTwoArray = currentFileArray;

			inputStream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}


		//extract left channel bytes
		fileOneLeftChannel = new byte[fileOneArray.length / 2];
		fileTwoLeftChannel = new byte[fileTwoArray.length / 2];
		for (int i = 0; i < fileOneArray.length / 2; i+=2) {
			fileOneLeftChannel[i] = fileOneArray[2*i];
			fileOneLeftChannel[i] = fileTwoArray[2*i + 1];
		}
		fileTwoLeftChannel = fileOneLeftChannel;
		
		
		//convert left channel bytes to doubles
		ByteBuffer byteBuffer = ByteBuffer.wrap(fileOneLeftChannel);
		fileOneDouble = new double[fileOneLeftChannel.length / 2];
		int i = 0;
		while (byteBuffer.remaining() > 2) {
			// read shorts (16bits) and cast them to doubles
			short t = byteBuffer.getShort();
			fileOneDouble[i] = t / 32768.0;
			i++;
		}
		fileTwoDouble = fileOneDouble;
		
		//create arrays to store imaginary components of frequencies
		fileOneImg = new double[fileOneDouble.length];
		fileTwoImg = new double[fileTwoDouble.length];
		
		//Convert to frequency domain using FFT
		FFT fft = new FFT((int) Math.pow(2, 15));
		fft.fft(fileOneDouble, fileOneImg);
		fft.fft(fileTwoDouble, fileTwoImg);
		
		//Convert the real and imaginary components of results to
		//frequencies using sqrt (real * real + img * img)
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
		
		//Compare frequencies using mean squared error
		double squaredError = 0;
		double meanSquaredError = 0;
		for (int j = 0; j < fileOneFFT.length; j++) {
			double value1 = fileOneFFT[j];
			double value2 = fileTwoFFT[j];
			double error = Math.pow((value1 - value2), 2);
			squaredError += error;
		}
		meanSquaredError = squaredError / fileOneFFT.length;
		
		System.out.println(meanSquaredError);
		
		//Return MATCH if error < %10, otherwise return NO MATCH
		
		//Memory improvement: set byte arrays to null when
		//they are no longer needed?
	}
}




