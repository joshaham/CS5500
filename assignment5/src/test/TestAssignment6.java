package test;

import static org.junit.Assert.*;

import org.junit.Test;

import audio.Audio;
import audio.Comparator;

public class TestAssignment6 {

	@Test
	public void testWAVEqual(){
		Audio file1 = Audio.getInstance("A5/D1/curieuse2.wav");
		Audio file2 = Audio.getInstance("A5/D2/curieuse.wav");
		assertTrue(Comparator.isMatch(file1, file2));
		
		Audio janacek = Audio.getInstance("A5/D1/maynard.wav");
		Audio janacek2 = Audio.getInstance("A5/D2/maynard2.wav");
		assertTrue(Comparator.isMatch(janacek, janacek2));
		
		Audio maynard = Audio.getInstance("A5/D1/sons2.wav");
		Audio maynard2 = Audio.getInstance("A5/D2/sons.wav");
		assertTrue(Comparator.isMatch(maynard, maynard2));
		
		Audio rimsky = Audio.getInstance("A5/D1/z03.wav");
		System.out.println("rimsky:\n"+rimsky);
		Audio rimsky2 = Audio.getInstance("A5/D2/z04.wav");
		System.out.println("rimsky2:\n"+rimsky2);
		assertTrue(Comparator.isMatch(rimsky, rimsky2));
		
		Audio sons = Audio.getInstance("A5/D1/z07.wav");
		Audio sons2 = Audio.getInstance("A5/D2/z08.wav");
		assertTrue(Comparator.isMatch(sons, sons2));
	}
//	@Test
	public void testWAVNotEqual(){
//		System.out.println("testWAVNotEqual");
//		Audio file1 = Audio.getInstance("examples/curieuse.wav");
//		Audio file2 = Audio.getInstance("examples/janacek.wav");
//		assertFalse(Assignment6.isMatch(file1, file2));
//		
//		Audio janacek = Audio.getInstance("examples/curieuse.wav");
//		Audio janacek2 = Audio.getInstance("examples/janacek2.wav");
//		assertFalse(Assignment6.isMatch(janacek, janacek2));
//		
//		Audio maynard = Audio.getInstance("examples/maynard.wav");
//		Audio maynard2 = Audio.getInstance("examples/rimsky2.wav");
//		assertFalse("MSE: "+Audio.calculateMSE(maynard, maynard2),Assignment6.isMatch(maynard, maynard2));
//		
//		Audio rimsky = Audio.getInstance("examples/maynard.wav");
//		Audio rimsky2 = Audio.getInstance("examples/rimsky2.wav");
//		assertFalse("MSE: "+Audio.calculateMSE(rimsky, rimsky2),Assignment6.isMatch(rimsky, rimsky2));
//		
//		Audio sons = Audio.getInstance("examples/curieuse.wav");
//		Audio sons2 = Audio.getInstance("examples/sons2.wav");
//		assertFalse("MSE: "+Audio.calculateMSE(sons, sons2),Assignment6.isMatch(sons, sons2));
	}

}
