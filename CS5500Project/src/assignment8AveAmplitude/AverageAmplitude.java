package assignment8AveAmplitude;

// average amplitudes class, line plot data array
public class AverageAmplitude {
	// overlap ratio of each sample interval
	public static double overlapRatio=0.9;
	// length of time interval to compute average amplitude
	static int AveAmplitudeWindow=1; // 1 second
	// convert samples into one channel
	int[] monoChannel=null;
	int[] PostiveAveAmplitudes=null;
	int[] NegativeAveAmplitudes=null;
	int[] postiveAngles=null;
	int[] negativeAngles=null;
	
	
	// given audio samples data and audio header
	public AverageAmplitude(int[][] data, AudioHeader header){
		// averages amplitudes per second
		int valuesPerSecond=(int) (1/(1-overlapRatio));
		// audio samples per time window
		int audioSamplesPerInterval=AveAmplitudeWindow*header.sampleRate;
		int offset=(int) (header.sampleRate*(1-overlapRatio));
		
		this.PostiveAveAmplitudes=
				new int[(header.audioLength-AveAmplitudeWindow)*valuesPerSecond];
		this.NegativeAveAmplitudes=
				new int[(header.audioLength-AveAmplitudeWindow)*valuesPerSecond];
		this.monoChannel=convert2monochannel(data);
		for(int i=0;i<PostiveAveAmplitudes.length;i++){
			long posAccumul=0, negAccumul=0;
			int start=i*offset;
			for(int k=start;k<start+audioSamplesPerInterval;k++){
				if(this.monoChannel[k]>0){
					posAccumul+=this.monoChannel[k];
				}else{
					negAccumul+=Math.abs(this.monoChannel[k]);
				}
			}
			PostiveAveAmplitudes[i]=(int) (posAccumul/audioSamplesPerInterval);
			NegativeAveAmplitudes[i]=(int) (negAccumul/audioSamplesPerInterval);
		}
		postiveAngles=calculateAngles(PostiveAveAmplitudes,0);
		negativeAngles=calculateAngles(NegativeAveAmplitudes,0);
	}
	// calculate angles of this data array.
	private int[] calculateAngles(int[] array, int start) {
		int deltX=(int)(500*(1-AverageAmplitude.overlapRatio));
		int[] angles=new int[array.length-1];
		for(int i=start;i<array.length-1;i++){
			int deltY=array[i+1]-array[i];
			angles[i]=getAngelInDegree(Math.atan(deltY/(0.0+deltX)));
		}
		return angles;
	}
	
	//convert from angle in radian to to angle in degree
	public  int getAngelInDegree(double angle){
		return (int) (angle*180/Math.PI);
	}
	
	// convert canonical channels to monochannel
	private int[] convert2monochannel(int[][] data){
		int numChannel=data.length;
		int[] monoChannel=new int[data[0].length];
		for(int i=0;i<data[0].length;i++){
			int ave=0;
			for(int ch=0;ch<numChannel;ch++){
				ave+=data[ch][i];
			}
			monoChannel[i]=ave/numChannel;
		}
		return monoChannel;
	}
	
	

	public int[] getPostiveAngles() {
		return postiveAngles;
	}
	public int[] getNegativeAngles(){
		return negativeAngles;
	}

	public int[] getPostiveHashValuePerInterval() {
		return PostiveAveAmplitudes;
	}

	public int[] getNegativeHashValuePerInterval() {
		return NegativeAveAmplitudes;
	}
}
