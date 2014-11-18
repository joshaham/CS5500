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
	
	// The negative byte, i.e 10000000
	static final byte neg = (byte) 128;
	
	// Check the sign of the two's-complement byte
	static int sgn(byte b) {
		if( (b & neg ) == neg) { return -1; } // leading bit is 1
		return 1; // leading bit is 0
	}
	
	// Check two bytes for a zero crossing
	// (when one is negative and the other is positive)
	static boolean ckzc(byte b1, byte b2) {
		if ( sgn(b1) * sgn(b2) == -1 ) { return true; }
		return false;
	}

	// Fix the bin size to a power of 2
	static final int bin_size = 8;
	
	// Get the Zero Crossing Rate
	static int getzcr(byte[] B) {
		int zero_crossings = 0;
		int num_bins = (int) B.length / bin_size;
		byte last_sgn = (byte) 0; // holds the value of every 1024th byte
		int ith; // value of i * bin_size
		
		for (int i = 0; i < num_bins; i++) {
			ith = i * bin_size;
			if  (ckzc( B[ith] , last_sgn)) { 
         			zero_crossings++;
			 }
			for (int j = 1; j < bin_size; j++) {
				if ( ckzc(B[ith + j - 1], B[ith + j]) ) {
					zero_crossings++;
				}
			}
			if (i < num_bins - 1) {
				last_sgn = B[ith + bin_size - 1];
			}
		}
		
		return (int) (zero_crossings / num_bins) * 100;
	}
	
}
