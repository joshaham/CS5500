
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

class Comparator{

	static boolean DEBUG=true;

	public static void main(String[] args){

		Comparator instance = new Comparator();
		int r=instance.checkInput(args);
		
		if(r != 0) { 
			if (r == 3) {
				System.err.println(String.format("ERROR: %s is not a "
						+ "supported format", args[1]));
			}
			if (r == 4) { 
				System.err.println(String.format("ERROR: %s is not a "
						+ "supported format", args[3]));
			}
			else {
				System.err.println("ERROR: incorrect command line");
			}
			// Return the value corresponding to the appropriate error
			if (DEBUG) { System.out.println(r); }
		} 
		
		else { // If inputs are of the correct form
			int cd1 = instance.checkCDSpecs(args[1]);
			if (cd1 != 0) {
				System.err.println("File one does not match CD specification");
				if (DEBUG) { System.out.println(cd1); }
				System.exit(cd1);
			}
			int cd2 = instance.checkCDSpecs(args[3]);
			if (cd2 != 0) {
				System.err.println("File two does not match CD specification");
				if (DEBUG) { System.out.println(cd2); }
				System.exit(cd2);
			}
			
			// begin code here


		}
	}

	// Checks to see if the inputs are used correctly
	private int checkInput(String[] args){
		if (args.length!=4){
			return 1;
		}
		if (!(args[0].trim()).equals("f") || !(args[2].trim()).equals("f")) {
			return 2;
		}
		String file1 = args[1].trim(), file2 = args[3].trim();
		if(!file1.endsWith(".wav")) {
			return 3;
		}
		if (!file2.endsWith(".wav")) { 
			return 4;
		}
		return 0;
	}

	private int checkCDSpecs(String input) {
		try {
			// Read the first 36 bytes of the input
			File file = new File(input);
			InputStream inputStream = new FileInputStream(file);
			byte[] bytes = new byte[36];
			inputStream.read(bytes, 0, 36);
			//Check format = wave
			if (bytes[8] == 87 && bytes[9] == 65 &&
					bytes[10] == 86 && bytes[11] == 69) {
				if (DEBUG) { System.out.println("Wave Format"); }
				//Check audio format = PCM
				if (bytes[20] == 1 && bytes[21] == 0) {
					if (DEBUG) { System.out.println("PCM"); }
					//Check channels = stereo
					if (bytes[22] == 2 && bytes[23] == 0) {
						if (DEBUG) {System.out.println("Stereo Channels"); }
						//Check sample rate is 44.1 kHz
						if (bytes[24] == 68 && bytes[25] == -84
								&& bytes[26] == 0 && bytes[27] == 0) {
							if (DEBUG) { 
								System.out.println("44100 Sample Rate "); }
							if (bytes[34] == 16 && bytes[35] == 0) {
								if (DEBUG) {
									System.out.println("16 bits per sample");
								}
								inputStream.close();
								return 0;
							}
						}
					}
				}
			}
			inputStream.close();
			return 5;
		} catch (Exception e) {
			e.printStackTrace();
			return 6;
		}
	}

}