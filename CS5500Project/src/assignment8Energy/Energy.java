package assignment8Energy;


public class Energy {
	static double overlapRatio=0.9;
	int[][] data=null;
	int[] aveChannel=null;
	long[] hashvaluePerSecond=null;
	public long[] getHashvaluePerSecond() {
		return hashvaluePerSecond;
	}
	public double[] getHashValuePerSecondForDouble(){
		double[] ret=new double[hashvaluePerSecond.length];
		for(int i=0;i<ret.length;i++){
			ret[i]=hashvaluePerSecond[i];
//			System.out.format(" %8d",hashvaluePerSecond[i]);
		}

		return ret;
	}
	double[] daveChannel=null;
	AudioHeader header=null;
	
	public double[] getEnergyDoubleArray() {
		return daveChannel;
	}
	public Energy(int[][] data, AudioHeader header){
		int samplesPerSecond=(int) (1/(1-overlapRatio));
		this.header=header;
		this.data=data;
		this.aveChannel=new int[this.data[0].length];
		this.daveChannel=new double[this.data[0].length];
		this.hashvaluePerSecond=new long[(daveChannel.length/header.sampleRate)*samplesPerSecond];
		for(int i=0;i<data[0].length;i++){
			int ave=0;
			for(int ch=0;ch<header.numChannels;ch++){
				ave+=data[ch][i];
			}
			aveChannel[i]=ave/data.length;
			daveChannel[i]=aveChannel[i];
		}
		for(int i=0;i<hashvaluePerSecond.length-samplesPerSecond;i++){
			int tmp=0;
			int start=i*header.sampleRate/samplesPerSecond;
			for(int k=start;k<start+header.sampleRate;k++){
				tmp+=this.aveChannel[k];
			}
			hashvaluePerSecond[i]=tmp;
		}
	}
	
}
