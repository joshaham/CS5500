package drawer;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

public class TimeDomainDrawer {
	public TimeDomainDrawer(){
	}
	
	public void Drawer(File file)throws Exception{
		AudioInputStream audioInputStream = 
				AudioSystem.getAudioInputStream(new BufferedInputStream(new FileInputStream(file)));
		byte[] bytes = new byte[(int) (audioInputStream.getFrameLength()) * (audioInputStream.getFormat().getFrameSize())];
		audioInputStream.read(bytes);
		// Get amplitude values for each audio channel in an array.
		 int[][]  graphData = this.getUnscaledAmplitude(bytes, 1);
		
	}


	public int[][] getUnscaledAmplitude(byte[] eightBitByteArray, int nbChannels)
	{
	    int[][] toReturn = new int[nbChannels][eightBitByteArray.length / (2 * nbChannels)];
	    int index = 0;

	    for (int audioByte = 0; audioByte < eightBitByteArray.length;)
	    {
	        for (int channel = 0; channel < nbChannels; channel++)
	        {
	            // Do the byte to sample conversion.
	            int low = (int) eightBitByteArray[audioByte];
	            audioByte++;
	            int high = (int) eightBitByteArray[audioByte];
	            audioByte++;
	            int sample = (high << 8) + (low & 0x00ff);

	            toReturn[channel][index] = sample;
	        }
	        index++;
	    }

	    return toReturn;
	}
}
