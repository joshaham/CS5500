package comparator;

import audio.Audio;

public class Assignment6 {
	private static double THRESHOLD=30;
	public static void main(String[] args) {		
		if(!ParameterChecker.CheckFormat(args)){
			System.exit(-1);
		};
		Audio file1 = Audio.getInstance(args[1]);
		Audio file2 = Audio.getInstance(args[3]);
		double meanSquaredError = Audio.calculateMSE(file1,file2);
		if (isMatch(meanSquaredError)) {
			System.out.println("MATCH "+file1.getFileName()+" "+file2.getFileName());
		} else {
			System.out.println("NO MATCH");
		}
	}
	
	public static boolean isMatch(double meanSquaredError){
		return meanSquaredError < THRESHOLD;
	}

}

