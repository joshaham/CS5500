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
		command1.add("pushd");
		command1.add("/course/cs5500f14/Examples/FFT");
		
		//create ArrayList for command: larceny --err5rs
		List<String> command2 = new ArrayList<String>();
		command2.add("larceny");
		command2.add("--err5rs");
		
		//create ArrayList for command: (import (rnrs) (larceny load))
		List<String> command3 = new ArrayList<String>();
		command3.add("import");
		command3.add("(rnrs)");
		command3.add("(larceny");
		command3.add("load))");
		
		//create ArrayList for command: (load "demo3.sch")
		List<String> command4 = new ArrayList<String>();
		command4.add("(load");
		command4.add("\"demo2.sch\")");
		
		//create ArrayList for command: (import (local fft) (local aux))
		List<String> command5 = new ArrayList<String>();
		command5.add("(import");
		command5.add("(local");
		command5.add("fft)");
		command5.add("(local");
		command5.add("aux))");
		
		//create ArrayList for command: (vector-map magnitude (fft v3))
		List<String> command6 = new ArrayList<String>();
		command6.add("vector-map");
		command6.add("magnitude");
		command6.add("(fft");
		command6.add("v3))");
		
		executeCommands(command1);
		executeCommands(command2);
		executeCommands(command3);
		executeCommands(command4);
		executeCommands(command5);
		executeCommands(command6);
	}

	private static void executeCommands(List<String> command) {
		//Initalize ProcessBuilder
		ProcessBuilder pb = new ProcessBuilder(command);
		
		//Start Process
		try {
			Process process = pb.start();

		
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
		     // ignore this one
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
		     // ignore this one
		   }
		 }
		 
		 return input;
	}
	
	
}
