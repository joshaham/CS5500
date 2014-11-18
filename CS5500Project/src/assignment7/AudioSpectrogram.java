package assignment7;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class AudioSpectrogram {
	// spectrograms
	// x: time
	// y: frequencis
	// value: amplitude
	int[][] array2D = null;
	public int[][] getArray2D() {
		return array2D;
	}


	ArrayList<String> localMaximas=null;
	ArrayList<ArrayList<Integer>> timeFrequencies=null;
	// Size of the FFT window, affects frequency granularity
	static int WINDOW_SIZE=1; // seconds
	/*
	 *  Ratio by which each sequential window overlaps the last and the
	 * next window. Higher overlap will allow a higher granularity of offset
     * matching, but potentially more fingerprints.
	 */
	static double OVERLAP_RATIO=0.1; // 0.1 second
	/*
	 * # Minimum amplitude in spectrogram in order to be considered a peak. 
	 * # This can be raised to reduce number of fingerprints, but can negatively
	 * # affect accuracy.
	 */
	static int AMP_MIN=10;
	/*
	 * # Number of cells around an amplitude peak in the spectrogram in order
	 * # for Dejavu to consider it a spectral peak. Higher values mean less
	 * # fingerprints and faster matching, but can potentially affect accuracy.
	 */
	static int NEIGHBORHOOD_SIZE=20;
	
	public AudioSpectrogram(double[] dualChannelSamples, double[] datas, AudioHeader header){
		int windowSize = WINDOW_SIZE;
		double overlapRatio = OVERLAP_RATIO;
		int ampMin=AMP_MIN;
		// generate spectogram
		array2D=generateSpectrogram(datas,header,windowSize,overlapRatio);

		// find peaks
		int neighborSize=NEIGHBORHOOD_SIZE;
		// extract peaks
		timeFrequencies = get2DPeaks(array2D,neighborSize,ampMin,overlapRatio,header.audioLength);
		// generate (time,frequency) peak pair
		
	}
	// calculate without overlap of bins
	private int[][] generateSpectrogram(double[] datas,AudioHeader header,
			int windowSize, double overlapRatio){
		
		int[][] spectrogram=null;
		int width=(int)(header.audioLength/overlapRatio);
		double[] bin=null;
		spectrogram=new int[20000-20+1][width];
		
		int sampleSize=windowSize*header.sampleRate;
		int offset=(int)(header.sampleRate*overlapRatio);
		for(int col=0;col<width;col++){
			bin=Arrays.copyOfRange(datas, offset*col, offset*col+sampleSize);
			int[] frequencyAmplitudes=calculateFrequencyArray(bin);
			for(int hz=20;hz<=20000;hz++){
				int amplitude=getAmplitudeOfFrequency(hz, frequencyAmplitudes, header);
				int row=20000-hz;
				spectrogram[row][col]=amplitude;
			}
		}
		
		return spectrogram;
	}
	
	// calculate peaks for each second
	private ArrayList<ArrayList<Integer>> get2DPeaks(int[][] array2D,int neighborSize,int ampMin, 
			double overlapRatio, int audioLength){
		ArrayList<ArrayList<Integer>> timePeaks=new ArrayList<ArrayList<Integer>>();
		for(int t=0;t<audioLength;t++){
			ArrayList<Integer> peaks=searchPeaks(array2D,neighborSize,ampMin,overlapRatio,t);
			timePeaks.add(peaks);
		}
		return timePeaks;
	}
	private ArrayList<Integer> searchPeaks(int[][] array2D,int neighborSize, int ampMin,
			double overlapRatio, int t) {
		int samples=(int) (1/overlapRatio);
		for(int col=samples*t;col<(t+1)*samples;col++){
			
		}
		return null;
	}
	//Applies the FFT to the double arrays
	public  int[] calculateFrequencyArray(double[] data) {
		double[] fileImg;
		int[] frequenciesData;
		double[] fileDouble=Arrays.copyOf(data, data.length);
		//create arrays to store imaginary components of frequencies
		fileImg = new double[fileDouble.length];
		
		//Convert to frequency domain using FFT
		int idx=1;
		for(idx=30;idx>0;idx--){
			if(Math.pow(2, idx)<fileDouble.length){
				break;
			}
		}
		FFT fft = new FFT((int) Math.pow(2, idx));
		fft.fft(fileDouble, fileImg);

		frequenciesData = new int[fileDouble.length / 2];
		for (int j = 0; j < frequenciesData.length; j++) {
			double real = fileDouble[j];
			double img = fileImg[j];
			double freq = Math.sqrt(real*real + img*img);
			frequenciesData[j] = (int)freq;
		}
		return frequenciesData;
	}
	
	
	public int getAmplitudeOfFrequency(int frequency,int[] frequencyAmplitudes,AudioHeader header){
		int idx = (int)(Math.floor(frequency * 
				((frequencyAmplitudes.length+0.0) / header.getSampleRate())));
		return frequencyAmplitudes[idx];
	}
	
}
