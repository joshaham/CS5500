package audio;

public class ComparatorAlgorithm {
	//Calculates the mean squared error for comparing
	//two files. 
	public static double calculateMSE(Audio file1, Audio file2) {
		int indexFor20Hz = (int)(Math.floor(20 * 
				file1.frequenciesData.length / file1.getSampleRate()));
		int indexFor20000Hz = (int)(Math.ceil(20000 * 
				file1.frequenciesData.length / file1.getSampleRate()));
		double squaredError = 0;
		double meanSquaredError = 0;
		for (int j = indexFor20Hz; j < indexFor20000Hz; j++) {
			double value1 = file1.frequenciesData[j];
			double value2 = file2.frequenciesData[j];
			double error = Math.pow((value1 - value2), 2);
			squaredError += error;
		}
		meanSquaredError = squaredError / file1.frequenciesData.length;
		
		return meanSquaredError;
	}
}
