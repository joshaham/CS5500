package assignment8Energy;

import java.util.HashMap;

public class Energy {
	static double overlapRatio=0.9;
	int[][] data=null;
	int[] aveChannel=null;
	int[] hashvaluePerSecond=null;
	public int[] getHashvaluePerSecond() {
		return hashvaluePerSecond;
	}
	double[] daveChannel=null;
	AudioHeader header=null;
	
	public double[] getEnergyDoubleArray() {
		return daveChannel;
	}
	public double[] getHashValuePerSecondForDouble(){
		double[] ret=new double[hashvaluePerSecond.length];
		for(int i=0;i<ret.length;i++){
			ret[i]=hashvaluePerSecond[i];
			System.out.format(" %8d",hashvaluePerSecond[i]);
		}
		System.out.println();
		return ret;
	}
	public Energy(int[][] data, AudioHeader header){
		int samplesPerSecond=(int) (1/(1-overlapRatio));
		this.header=header;
		this.data=data;
		this.aveChannel=new int[this.data[0].length];
		this.daveChannel=new double[this.data[0].length];
		this.hashvaluePerSecond=new int[(daveChannel.length/header.sampleRate)*samplesPerSecond];
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
			for(int k=i*header.sampleRate/samplesPerSecond;k<i*header.sampleRate/samplesPerSecond+header.sampleRate;k++){
				tmp+=this.aveChannel[k];
			}
			hashvaluePerSecond[i]=tmp;
		}
	}
	
//	@Override
//	public String toString(){
//		StringBuilder sb=new StringBuilder();
//		for(int i=0;i<energyArray.length;i++){
//			sb.append((int)energyArray[i]+"   ");
//			if(i%20==0){
//				sb.append('\n');
//			}
//		}
//		return sb.toString();
//	}
	
	@Override
	public String toString(){
		HashMap<Long,String> hm = this.getBinHashMap(this.header.fileName, 5);
		StringBuilder sb=new StringBuilder();
//		for(int i=0;i<energyArray.length;i++){
//			sb.append((int)energyArray[i]+"   ");
//			if(i%20==0){
//				sb.append('\n');
//			}
//		}
		return sb.toString();
	}

	public HashMap<Long, String> getBinHashMap(String fileName,
			int songSampleSize) {
		HashMap<Long,String> hm=new HashMap<Long,String>();
		for(int i=0;i<header.audioLength-songSampleSize;i++){
			long hashKey=0;
			for(int k=0;k<songSampleSize;k++){
				hashKey+=this.hashvaluePerSecond[i+k];
			}
			hashKey=this.round(hashKey, 100);
			String hashValue=fileName+";"+i*1000+";"+(i+songSampleSize)*1000;
			hm.put(hashKey, hashValue);
//			System.out.println(hashKey);
		}
		return hm;
	}
	public long round(long number, int digits){
		long roundedNumber = (number + 5*digits) / digits * digits;
		return roundedNumber;
	}
}
