package test;

import java.util.Arrays;

import drawer.JmathplotLineGraph;
import assignment7.AudioHeader;
import assignment7.FFT;

public class FFTTest {

	//Applies the FFT to the double arrays
	public static  int[] calculateFrequencyArray(double[] data) {
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
	
	public static int getAmplitudeOfFrequency(int frequency,int[] frequencyAmplitudes,AudioHeader header){
		int idx = (int)(Math.floor(frequency * 
				((frequencyAmplitudes.length+0.0) / header.getSampleRate())));
		return frequencyAmplitudes[idx];
	}
	
	
	// Test the FFT to make sure it's working
	  public static void main(String[] args) {
		  int N=(int) Math.pow(2, 10);
		 double[] re=new double[N];

		  // Single sin
		 for(int i=0; i<N; i++) {
		   re[i] = Math.cos(2*Math.PI*i*0.01);
		   re[i]+= Math.cos(2*Math.PI*i*0.02+3)*2;
		 }
		 int[] amplitudes = calculateFrequencyArray(re);
		 for(int i=0;i<amplitudes.length;i++){
			 
			 if(i%10==0){
				 System.out.println();
			 }
			 System.out.print(amplitudes[i]+"  ");
		 }
		 JmathplotLineGraph.plot2d("sin", re);
	  }


}
