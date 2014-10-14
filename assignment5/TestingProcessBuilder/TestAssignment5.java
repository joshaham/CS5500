
import java.io.*;
import java.nio.ByteBuffer;


public class TestAssignment5 {

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
	
	static double squaredError = 0;
	static double meanSquaredError = 0;
	
	public static void main(String[] args) {

		//check file path is ok, if not return 
		//ERROR: incorrect command line

		//check file extension, if not .wav return
		//ERROR: fileName is not a supported format

		//check file is in WAVE format, if not return
		//ERROR: File not in WAVE format
		
		//check file lengths, NO MATCH if files are
		//of different lengths
		
		//extract bytes for each file and input to byte arrays
		for (int i = 0; i < args.length; i++) {
			byte[] currentFileArray;

			try {
				String filePath = args[i];
				File file = new File(filePath);
				InputStream inputStream = new FileInputStream(file);
				currentFileArray = new byte[(int)file.length()];
				inputStream.read(currentFileArray, 0, currentFileArray.length);

				if (i == 0) {
					fileOneArray = currentFileArray;
				} else {
					fileTwoArray = currentFileArray;
				}

				inputStream.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				System.out.println("File not found");
				System.exit(1);
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(1);
			}
		}


		extractLeftChannels();
		convertToDoubles();	
		applyFFT();
		convertToFrequencies();
		calculateMSE();
		
		if (meanSquaredError < .1) {
			System.out.println("MATCH");
		} else {
			System.out.println("NO MATCH");
		}

	}

	//Calculates the mean squared error for comparing
	//two files
	private static void calculateMSE() {
		for (int j = 0; j < fileOneFFT.length; j++) {
			double value1 = fileOneFFT[j];
			double value2 = fileTwoFFT[j];
			double error = Math.pow((value1 - value2), 2);
			squaredError += error;
		}
		meanSquaredError = squaredError / fileOneFFT.length;
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

	//Applies the FFT to the two double arrays
	private static void applyFFT() {
		//create arrays to store imaginary components of frequencies
		fileOneImg = new double[fileOneDouble.length];
		fileTwoImg = new double[fileTwoDouble.length];
		
		//Convert to frequency domain using FFT
		FFT fft = new FFT((int) Math.pow(2, 15));
		fft.fft(fileOneDouble, fileOneImg);
		fft.fft(fileTwoDouble, fileTwoImg);
		
	}

	//Converts byte arrays to double arrays so that they can be
	//passed through the FFT code
	private static void convertToDoubles() {
		ByteBuffer byteBuffer = ByteBuffer.wrap(fileOneLeftChannel);
		fileOneDouble = new double[fileOneLeftChannel.length / 2];
		int i = 0;
		while (byteBuffer.remaining() > 2) {
			short t = byteBuffer.getShort();
			fileOneDouble[i] = t / 32768.0;
			i++;
		}
		
		ByteBuffer byteBuffer2 = ByteBuffer.wrap(fileTwoLeftChannel);
		fileTwoDouble = new double[fileTwoLeftChannel.length / 2];
		int j = 0;
		while (byteBuffer2.remaining() > 2) {
			short t = byteBuffer2.getShort();
			fileTwoDouble[j] = t / 32768.0;
			j++;
		}
	}
	
	//Extract left channel bytes
	private static void extractLeftChannels() {
		fileOneLeftChannel = new byte[fileOneArray.length - 44 / 2];
		fileTwoLeftChannel = new byte[fileTwoArray.length - 44 / 2];
		for (int i = 44; i < fileOneArray.length / 2; i+=2) {
			fileOneLeftChannel[i] = fileOneArray[2*i];
			fileOneLeftChannel[i] = fileOneArray[2*i + 1];
		}
		for (int i = 44; i < fileOneArray.length / 2; i+=2) {
			fileTwoLeftChannel[i] = fileTwoArray[2*i];
			fileTwoLeftChannel[i] = fileTwoArray[2*i + 1];
		}
		
	}
}

