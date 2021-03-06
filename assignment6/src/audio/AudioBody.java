package audio;

public class AudioBody {
	String fileName;
	static int interval=5;
	TimeSegment[] segments=null;
	public AudioBody(String fileName,int[] timeZone,double[] fftdata,AudioHeader header){
		this.fileName=fileName;
		int numSegments=(header.audioLength)/interval;
		int fftDataRate=header.sampleRate;
		int samplesPerSegment=interval*fftDataRate;
		segments=new TimeSegment[numSegments];
		for(int i=0;i<numSegments;i++){
			int startIdx=i*samplesPerSegment;
			double[] timeSeg=new double[samplesPerSegment];
			for(int k=0;k<samplesPerSegment;k++){
				timeSeg[k]=fftdata[startIdx+k];
			}
			segments[i]=new TimeSegment(i,timeSeg,header.getSampleRate());
		}
	}
}

class TimeSegment{

	int segmentId;
	int sampleRate;
	double[] frequencyData;
	protected TimeSegment(int segmentId, double[]timeSeg,int sampleRate){
		this.segmentId=segmentId;
		this.sampleRate=sampleRate;
		this.frequencyData = Audio.calculateFrequencyArray(timeSeg);
	}
	public int getSegmentId() {
		return segmentId;
	}
	public int getSampleRate() {
		return sampleRate;
	}
	public double[] getFrequencyData() {
		return frequencyData;
	}
}
