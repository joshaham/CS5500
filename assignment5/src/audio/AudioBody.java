package audio;

public class AudioBody {
	
	String fileName;
	AudioHeader header;
	static int INTERVAL=5;
	static int INCREASE = 1;
	TimeSegment[] segments=null;
	
	/*
	 * GIVEN:
	 * fileName: String, audio's filename
	 * timeZOne: time zone int array of audio file
	 * fftTimeZoneData: time zone double array of audio where value in range[-1,+1]
	 * header: header object of AudioHeader
	 */
	
	public AudioBody(String fileName,int[] timeZone,double[] fftTimeZoneData,AudioHeader header){

		this.fileName=fileName;
		this.header=header;
		// pre-calculate zcr for per second
		int[] zcrEachSecond=fillZCRArray(timeZone,header.audioLength,header.getSampleRate());
		int numSegments=header.audioLength-INTERVAL;
		segments=new TimeSegment[numSegments];
		for(int startSecond=0;startSecond<header.audioLength-INTERVAL;startSecond=startSecond+INCREASE){
			int zcr=this.getZCR(zcrEachSecond, startSecond, startSecond+INTERVAL);
			segments[startSecond]=new TimeSegment(startSecond,header,zcr);
		}
	}
	
	private int getZCR(int[] zcrEachSecond,int startSecond,int endSecond){
		int zcr=0;
		for(int i=startSecond;i<endSecond;i++){
			zcr+=zcrEachSecond[i];
		}
		return zcr;
		
	}
	// RETURN: a new int array representing zcr of each second
	private int[] fillZCRArray(int[] timeZone,int audioLength,int sampleRate){
		int[] zcrEachSecond=new int[audioLength];
		for(int second=0;second<audioLength;second++){
			int zcr=0;
			for(int i=sampleRate*second;i<(second+1)*sampleRate;i++){
				// found zero crossing
				 if(i>0 && (timeZone[i-1]>0 ^ timeZone[i]>0)){
					zcr++;
				}
			}
			zcrEachSecond[second]=zcr;
		}
		return zcrEachSecond;
	}
}

class TimeSegment{

	int segmentId;
	AudioHeader header;
	int zcr=0;
	protected TimeSegment(int segmentId,AudioHeader header,int zcr){
		this.segmentId=segmentId;
		this.header=header;
		this.zcr=zcr;
//		System.out.println(this);
	}
	public int getSegmentId() {
		return segmentId;
	}
	
	@Override
	public String toString(){
		StringBuilder str=new StringBuilder();
		str.append("Segment id: "+segmentId+'\n'+header.fileName+'\n');
		str.append("ZCR: "+zcr+'\n');
		return str.toString();
	}

}
