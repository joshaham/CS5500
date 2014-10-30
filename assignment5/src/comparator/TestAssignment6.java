package comparator;

import static org.junit.Assert.*;

import org.junit.Test;

import audio.Audio;

public class TestAssignment6 {

	@Test
	public void testEqual(){
		Audio file1 = Audio.getInstance("examples/curieuse.wav");
		Audio file2 = Audio.getInstance("examples/curieuse.wav");
		assertTrue(Assignment6.isMatch(Audio.calculateMSE(file1, file2)));
		
		Audio janacek = Audio.getInstance("examples/janacek.wav");
		Audio janacek2 = Audio.getInstance("examples/janacek2.wav");
		assertTrue(Assignment6.isMatch(Audio.calculateMSE(janacek, janacek2)));
		
		Audio maynard = Audio.getInstance("examples/maynard.wav");
		Audio maynard2 = Audio.getInstance("examples/maynard2.wav");
		assertTrue("MSE: "+Audio.calculateMSE(maynard, maynard2),Assignment6.isMatch(Audio.calculateMSE(maynard, maynard2)));
		
		Audio rimsky = Audio.getInstance("examples/rimsky.wav");
		Audio rimsky2 = Audio.getInstance("examples/rimsky2.wav");
		assertTrue("MSE: "+Audio.calculateMSE(rimsky, rimsky2),Assignment6.isMatch(Audio.calculateMSE(rimsky, rimsky2)));
		
		Audio sons = Audio.getInstance("examples/sons.wav");
		Audio sons2 = Audio.getInstance("examples/sons2.wav");
		assertTrue("MSE: "+Audio.calculateMSE(sons, sons2),Assignment6.isMatch(Audio.calculateMSE(sons, sons2)));
	}

}
