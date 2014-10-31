package audio;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestWavAudio {

//	@Test
//	public void test() {
//		Audio z08 = new WavAudio("examples/z08.wav");
//		System.out.println(z08.getBitesPerSecond());
//		// test 16 bites per second
//		assertEquals(16,z08.getBitesPerSecond());
//		System.out.println(z08.getFileName());
//		// test file name
//		assertEquals("z08.wav",z08.getFileName());
//		System.out.println(z08.getFormat());
//		// test format wav
//		assertEquals("wav",z08.getFormat());
//		System.out.println(z08.getNumChannels());
//		//test 2 channels
//		assertEquals(2,z08.getNumChannels());
//		System.out.println(z08.getSampleRate());
//		//test sample rate 44100
//		assertEquals(44100.0,z08.getSampleRate(),0.001);
//	}
//	
	@Test
	// test for different bites per second
	public void bpsTest(){
		Audio z01 = new WavAudio("examples/curieuse.wav");
		// test 16 bites per second
		assertEquals(16,z01.getBitesPerSecond());
	}
	@Test
	// test for different num of channels
	public void channelsTest(){
		Audio z02 = new WavAudio("examples/curieuse.wav");
		// test 2 channels
		assertEquals(2,z02.getNumChannels());
	}
	@Test
	// test for different sample rate
	public void sampleRateTest(){
		Audio z03 = new WavAudio("examples/curieuse.wav");
		// test 2 channels
		assertEquals(44100.0,z03.getSampleRate(),0.01);
	}	
	@Test
	// test for file name
	public void fileNameTest(){
		Audio z04 = new WavAudio("examples/curieuse.wav");
		// test 2 channels
		assertEquals("curieuse.wav",z04.getFileName());
	}

}