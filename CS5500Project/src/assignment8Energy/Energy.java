package assignment8Energy;


public class Energy {
	static double overlapRatio=0.9;
	// time interval for hash
	static int HsashvalueInterval=1;
	int[] monoChannel=null;
	int[] PostiveHashValuePerInterval=null;
	int[] NegativeHashValuePerInterval=null;
	public int[] getPostiveHashValuePerInterval() {
		return PostiveHashValuePerInterval;
	}

	public int[] getNegativeHashValuePerInterval() {
		return NegativeHashValuePerInterval;
	}

	public Energy(int[][] data, AudioHeader header){
		int valuesPerSecond=(int) (1/(1-overlapRatio));
		int audioSamplesPerInterval=HsashvalueInterval*header.sampleRate;
		int offset=(int) (header.sampleRate*(1-overlapRatio));
		
		this.PostiveHashValuePerInterval=new int[(header.audioLength-HsashvalueInterval)*valuesPerSecond];
		this.NegativeHashValuePerInterval=new int[(header.audioLength-HsashvalueInterval)*valuesPerSecond];
		this.monoChannel=convert2monochannel(data);
		for(int i=0;i<PostiveHashValuePerInterval.length;i++){
			long posAccumul=0;
			long negAccumul=0;
			int start=i*offset;
			for(int k=start;k<start+audioSamplesPerInterval;k++){
				if(this.monoChannel[k]>0){
					posAccumul+=this.monoChannel[k];
				}else{
					negAccumul+=Math.abs(this.monoChannel[k]);
				}
			}
			PostiveHashValuePerInterval[i]=(int) (posAccumul/audioSamplesPerInterval);
			NegativeHashValuePerInterval[i]=(int) (negAccumul/audioSamplesPerInterval);
		}
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
	
}
