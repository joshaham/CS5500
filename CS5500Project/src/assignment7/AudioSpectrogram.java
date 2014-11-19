package assignment7;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AudioSpectrogram {
	// spectrograms
	// col: time
	// row: frequencis
	// value: amplitude
	int[][] spectrogramMatrix = null;
	public int[][] getArray2D() {
		return spectrogramMatrix;
	}


	Set<String> localPeaks=null;
	private String[] peaks=null;
	public String[] getLocalPeaks() {
		if(peaks==null){
			peaks=localPeaks.toArray(new String[localPeaks.size()]);
		}
		return peaks;
	}


	// Size of the FFT window, affects frequency granularity
	static int WINDOW_SIZE=1; // seconds
	// Maximum size of peaks per time slot
	static int MAXIMUM_PEAKS_PER_BIN=10;
	/*
	 *  Ratio by which each sequential window overlaps the last and the
	 * next window. Higher overlap will allow a higher granularity of offset
     * matching, but potentially more fingerprints.
	 */
	static double OVERLAP_RATIO=0.9; // 1-OVERLAP_RATIO second
	public static double getOVERLAP_RATIO() {
		return OVERLAP_RATIO;
	}

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
		List<List<FrequencyAmplitudePair>> peaksContainer = null;
		// generate spectogram
		spectrogramMatrix=generateSpectrogram(datas,header,windowSize,overlapRatio);
		// extract peaks
		int neighborSize=NEIGHBORHOOD_SIZE;
		int maxPeaksNum=MAXIMUM_PEAKS_PER_BIN;
		peaksContainer = get2DPeaks(spectrogramMatrix,neighborSize,ampMin,maxPeaksNum,overlapRatio,header.audioLength,header.getSampleRate());
//		for(int row=0;row<array2D.length;row++){
//			System.out.println();
//			for(int col=0;col<array2D[0].length;col++){
//				System.out.print(array2D[row][col]+"  ");
//			}
//		}
		
		// generate (time,frequency) peak pair
		localPeaks=generatePeakPairs(peaksContainer);
		
	}
	

	private Set<String> generatePeakPairs(
			List<List<FrequencyAmplitudePair>> peaksContainer) {
		Set<String> peaks= new HashSet<String>();
		
		for(int i=0;i<peaksContainer.size();i++){
			List<FrequencyAmplitudePair> pairs=peaksContainer.get(i);
			for(FrequencyAmplitudePair p : pairs){
				peaks.add(""+p.millisecond+":"+p.getFrequency());
			}
		}
		
		return peaks;
	}


	// calculate peaks for each second
	private ArrayList<List<FrequencyAmplitudePair> > get2DPeaks(int[][] spectrogramMatrix,int neighborSize,int ampMin, 
			int maxPeaksNum,double overlapRatio, int audioLength,int sampleRate){
//		System.out.println("get2DPeaks: height: "+spectrogramMatrix.length +"  width:" +spectrogramMatrix[0].length);
		ArrayList<List<FrequencyAmplitudePair> > timePeaks=new ArrayList<List<FrequencyAmplitudePair> >();
//		System.out.println("Max time idx: "+audioLength/(1-overlapRatio));
		for(int t=0;t<(int)(audioLength/(1-overlapRatio));t++){
			List<FrequencyAmplitudePair> peaks=searchPeaks(spectrogramMatrix,neighborSize,ampMin,maxPeaksNum,t,sampleRate,overlapRatio);
			timePeaks.add(peaks);
		}
		return timePeaks;
	}
	// return a list of frequencies representing peaks at this timeIdx
	private List<FrequencyAmplitudePair> searchPeaks(int[][] spectrogramMatrix,int neighborSize, int ampMin,
			int maxPeaksNum, int timeIdx, int sampleRate, double overlapRatio) {
		ArrayList<FrequencyAmplitudePair> frequencyAmplitudePairs=
				new ArrayList<FrequencyAmplitudePair>();
			for(int frequency=0;frequency<spectrogramMatrix.length;frequency++){
				int amplitude=spectrogramMatrix[frequency][timeIdx];
				if(isPeak(spectrogramMatrix,amplitude,frequency,timeIdx,neighborSize,ampMin)){
					int milSecond = (int) (timeIdx*1000*(1-overlapRatio));
					frequencyAmplitudePairs.add(new FrequencyAmplitudePair(frequency,amplitude,milSecond));
				}
			}
		Collections.sort(frequencyAmplitudePairs,Collections.reverseOrder());
		return frequencyAmplitudePairs.subList(0, Math.min(frequencyAmplitudePairs.size(), maxPeaksNum));
	}
	// judge if this amplitude is a local peak
	private boolean isPeak(int[][] array2D, int amplitude,int row, int col, int neighborSize, int ampMin){
		if(amplitude<=ampMin){
			return false;
		}
		Set<Pair> previousLevel = new HashSet<Pair>();
		Set<Pair> currentLevel = new HashSet<Pair>();
		currentLevel.add(new Pair(row,col));
		boolean isPeak=true;
		while(previousLevel.size()<neighborSize && isPeak && currentLevel.size()!=0){
			Set<Pair> nextRound = new HashSet<Pair>();
			for(Pair p : currentLevel){
				previousLevel.add(p);
				int r=p.x;
				int c=p.y;
				isPeak&=checkAndAdd(r+1,c,array2D,amplitude,nextRound,previousLevel);
				isPeak&=checkAndAdd(r-1,c,array2D,amplitude,nextRound,previousLevel);
				isPeak&=checkAndAdd(r,c+1,array2D,amplitude,nextRound,previousLevel);
				isPeak&=checkAndAdd(r,c-1,array2D,amplitude,nextRound,previousLevel);
			}
			currentLevel=nextRound;
		}
		return isPeak;
	}
	// help function of isPeak method
	private boolean checkAndAdd(int r, int c, int[][] array2D, int amplitude,
			Set<Pair> nextRound, Set<Pair> previousLevel) {
		if(r<0 || r>=array2D.length || c<0 || c>=array2D[0].length){
			return true;
		}
		if(array2D[r][c]>amplitude){
			return false;
		}
		Pair p = new Pair(r,c);
		if(!previousLevel.contains(p)){
			nextRound.add(p);
		}
		return true;
	}

	// calculate without overlap of bins
	private int[][] generateSpectrogram(double[] datas,AudioHeader header,
			int windowSize, double overlapRatio){
		int[][] spectrogram=null;
		int width=(int)(header.audioLength/(1-overlapRatio));
		double[] bin=null;
		spectrogram=new int[20000-20+1][width];
		//windowSize for FFT, not bin size
		int sampleSize=windowSize*header.sampleRate;
		int offset=(int)(header.sampleRate*(1-overlapRatio));
//		for(int time=0;time<width;time++){
//			bin=Arrays.copyOfRange(datas, offset*time, offset*time+sampleSize);
//			int[] frequencyAmplitudes=calculateFrequencyArray(bin);
//			for(int hz=20;hz<=20000;hz++){
//				int amplitude=getAmplitudeOfFrequency(hz, frequencyAmplitudes, header);
//				int frequency=20000-hz;
//				spectrogram[frequency][time]=amplitude;
//			}
//		}
		int start=0;
		while(start+sampleSize<datas.length){
			bin=Arrays.copyOfRange(datas, start, start+sampleSize);
			int[] frequencyAmplitudes=calculateFrequencyArray(bin);
			double d=(start+0.0)/header.sampleRate;
			int timeIdx=(int) (d/(1-overlapRatio));
			for(int hz=20;hz<=20000;hz++){
				int amplitude=getAmplitudeOfFrequency(hz, frequencyAmplitudes, header);
				int frequency=20000-hz;
				spectrogram[frequency][timeIdx]=amplitude;
			}
			start+=offset;
		}
		return spectrogram;
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

class FrequencyAmplitudePair implements Comparable<FrequencyAmplitudePair>{
	int frequency;
	int amplitude;
	long millisecond;
	
	public int getFrequency() {
		return frequency;
	}

	public int getAmplitude() {
		return amplitude;
	}

	public FrequencyAmplitudePair(int frequency,int amplitude, long time){
		this.frequency=frequency;
		this.amplitude=amplitude;
		this.millisecond=time;
	}

	@Override
	public int compareTo(FrequencyAmplitudePair o) {
		return this.amplitude-o.amplitude;
	}
}

class Pair{
	int x;
	int y;
	public Pair(int x,int y){
		this.x=x;
		this.y=y;
	}
	@Override
	public int hashCode(){
		return (this.x+this.y);
	}
	@Override
	public boolean equals(Object p){
		if(p instanceof Pair){
			Pair o=(Pair)p;
			if(this.x==o.x && this.y==o.y){
				return true;
			}
		}
		return false;
	}
}
