import java.io.*;
import java.util.List;

//This code is based off of code provided by Alvin Alexander at:
//http://alvinalexander.com/java/java-exec-processbuilder-process-1

public class linuxCommand {

	public static void executeCommand(List<String> command) {
		
		try {
			
			//initialize processBuilder and start process
			ProcessBuilder pb = new ProcessBuilder(command);
			Process process = pb.start();

			//initialize bufferedReaders for input and error streams
			BufferedReader stdInput = new BufferedReader(new
					InputStreamReader(process.getInputStream()));

			BufferedReader stdError = new BufferedReader(new
					InputStreamReader(process.getErrorStream()));
			
			// print input stream
			String line = null;
			StringBuilder input = new StringBuilder();
			System.out.println("Output:\n");
			while ((line = stdInput.readLine()) != null) {
				input.append(line + "\n");
			}
			System.out.println(input);

			// print error stream
			line = null;
			StringBuilder error = new StringBuilder();
			System.out.println("Error:\n");
			while ((line = stdError.readLine()) != null) {
				error.append(line + "\n");
			}
			System.out.println(error);
			
		}
		
		catch (IOException e) {
			e.printStackTrace();
		}
	}
}
