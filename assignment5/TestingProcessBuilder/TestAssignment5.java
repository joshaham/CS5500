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

		linuxCommand.executeCommand(command1);
	}
}
