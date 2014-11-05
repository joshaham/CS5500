import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;


public class damm {

	public static void main(String[] args) {
		byte[] currentFileArray;

		try {
			
			String filePath = args[0];
			File file = new File(filePath);
			InputStream inputStream = new FileInputStream(file);
			currentFileArray = new byte[(int)file.length()];
			
			inputStream.read(currentFileArray, 0, currentFileArray.length);
			inputStream.close();
			
			System.out.println( getzcr(currentFileArray) );
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	// The negative byte, i.e 1000,0000
	static final byte neg = (byte) 128;
	
	// Checks the sign of the two's-complement byte.
	static int sgn(byte b) {
		if( (b & neg ) == neg) { return -1; } // leading bit is 1
		return 1; // leading bit is 0
	}
	
	// Check Zero Crossing
	static boolean ckzc(byte b1, byte b2) {
		if ( sgn(b1) * sgn(b2) == -1 ) { return true; }
		return false;
	}

	// constant bin size = 2^10
	static final int bin_size = 1024;
	
	static float getzcr(byte[] B) {
		int zero_crossings = 0;
		
		int num_bins = (int) B.length / bin_size;
		
		for (int i = 0; i < num_bins; i++) {
			for (int j = 1; j < bin_size; j++) {
				if ( ckzc(B[i + j - 1], B[i + j]) ) {
					zero_crossings++;
				}
			}
		}
		
		return zero_crossings / num_bins;
	}
	
}
