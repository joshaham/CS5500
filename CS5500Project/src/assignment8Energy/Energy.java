package assignment8Energy;

import java.util.HashMap;

public class Energy {
	static double overlapRatio=0.9;
	long[] energyArray=null;
	AudioHeader header=null;
	public Energy(byte[] array, AudioHeader header){
		this.header=header;
		int base=44;
		int binsPerSecond=(int) (1/(1-overlapRatio));
		energyArray=new long[header.audioLength*binsPerSecond];
		for(int i=0;i<energyArray.length-binsPerSecond;i++){
			long sum=0;
			for(int idx=0;idx<header.sampleRate;idx++){
				int start=(int) (base+i*(1-overlapRatio)*header.sampleRate+idx);
				int l=array[start]+array[start+1]<<8;
				int r=array[start+2]+array[start+3]<<8;
				sum+=l/255+r/255;
				if(idx%30==0){
					System.out.println();
				}
				System.out.format("%4d",l/255);
				System.out.format("%4d",r/255);

			}
			energyArray[i]=sum;
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
				hashKey+=energyArray[i+k];
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
