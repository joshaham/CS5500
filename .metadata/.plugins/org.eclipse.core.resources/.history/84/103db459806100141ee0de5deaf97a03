package audio;

import java.io.IOException;

class Mp3Audio extends Audio{

	public Mp3Audio(String filePath) {
		super(filePath);
	}
	
	public static WavAudio getInstance(String filePath){
//		// TODO Auto-generated constructor stub
		String fileWav=filePath.substring(0, filePath.indexOf(".mp3"))+".wav";
		try {
			java.lang.Runtime.getRuntime().exec("lame" + " --mp3input "+filePath+ "  "+fileWav);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new WavAudio(fileWav);
	}

	@Override
	int checkCDSpecs(byte[] bytes, int datasize, double[] sampleRate,
			String[] format, int[] bps, int[] nc) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	byte[] extractLeftChannels(int numsOfChannel, int bps) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	double[] convertToDoubles(byte[] fileLeftChannel, int bps) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	short[] convertToShort(byte[] fileLeftChannel, int bps) {
		// TODO Auto-generated method stub
		return null;
	}

    @Override
    byte[] convertToDualChannels(int bps) {
        // TODO Auto-generated method stub
        return null;
    }

}
