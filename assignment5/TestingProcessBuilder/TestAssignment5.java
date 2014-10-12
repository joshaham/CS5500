
import java.io.*;
import java.util.*;


public class TestAssignment5 {

	static byte[] fileOneArray;
	static byte[] fileTwoArray;

	public static void main(String[] args) {


		//check file path is ok, if not return 
		//ERROR: incorrect command line

		//check file extension, if not .wav return
		//ERROR: fileName is not a supported format

		//check file is in WAVE format, if not return
		//ERROR: File not in WAVE format
		
		//check file lengths, NO MATCH if files are
		//of different lengths

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
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		//Create imaginary byte arrays of same lengths
		//Convert to frequency domain using Columbia fft file
		//Create frequency array
		//Convert the real and imaginary components of results to
		//frequencies using sqrt (real * real + img * img)
		//Compare frequencies using mean squared error
		//Return MATCH if error < %10, otherwise return NO MATCH
	}
}

