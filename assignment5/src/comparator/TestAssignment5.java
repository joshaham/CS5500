package comparator;

public class TestAssignment5 {
	private static double THRESHOLD=5;
	public static void main(String[] args) {		
		if(!HeaderChecker.CheckFormat(args)){
			System.exit(-1);
		};
		Audio file1 =new Audio(args[1]);
		Audio file2 =new Audio(args[3]);
		double meanSquaredError = Audio.calculateMSE(file1,file2);
		if (meanSquaredError < THRESHOLD) {
			System.out.println("MATCH "+file1.fileName+" "+file2.fileName);
		} else {
			System.out.println("NO MATCH");
		}
	}

}

