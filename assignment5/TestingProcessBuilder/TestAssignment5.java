import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.*;


public class TestAssignment5 {


	public static void main(String[] args) {

		//create ArrayList for command: pushd /course/cs5500f14/Examples/FFT
		List<String> command1 = new ArrayList<String>();
		command1.add("xxd");
		command1.add("-l");
		command1.add("0x36");
		command1.add("wayfaring.wav");

		executeCommands(command1);
	}


	private static void executeCommands(List<String> command) {
		//Initialize ProcessBuilder
		ProcessBuilder pb = new ProcessBuilder(command);

		//Start Process
		try {
			Process process = pb.start();

			OutputStream stdOutput = process.getOutputStream();
			
			//Print output and error streams
			StringBuilder input = new StringBuilder();
			input = printInput(process.getInputStream());

			StringBuilder error = new StringBuilder();
			error = printError(process.getErrorStream());

			System.out.println(input);
			System.out.println(error);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}

	private static StringBuilder printError(InputStream errorStream) {
		BufferedReader bufferedReader = null;
		StringBuilder error = new StringBuilder();

		try
		{
			bufferedReader = new BufferedReader(new InputStreamReader(errorStream));
			String line = null;
			while ((line = bufferedReader.readLine()) != null)
			{
				error.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally
		{
			try
			{
				bufferedReader.close();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}

		return error;
	}

	private static StringBuilder printInput(InputStream inputStream) {
		BufferedReader bufferedReader = null;
		StringBuilder input = new StringBuilder();

		try
		{
			bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
			String line = null;
			while ((line = bufferedReader.readLine()) != null)
			{
				input.append(line + "\n");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally
		{
			try
			{
				bufferedReader.close();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}

		return input;
	}


}
