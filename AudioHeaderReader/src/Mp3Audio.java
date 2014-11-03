

import java.io.IOException;

class Mp3Audio extends Audio{

	public Mp3Audio(String filePath) {
		super(filePath);
	}
	
	public static WavAudio getInstance(String filePath){
//		// TODO Auto-generated constructor stub
		String fileWav=filePath.substring(0, filePath.indexOf(".mp3"))+".wav";
		fileWav="copy"+fileWav;
		try {
			java.lang.Runtime.getRuntime().exec("./lame" + " --decode "+filePath+ "  "+fileWav);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		WavAudio instance =  new WavAudio(fileWav);
		try {
			java.lang.Runtime.getRuntime().exec("rm  "+fileWav);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return instance;
	}

	@Override
	int checkCDSpecs(byte[] bytes, int datasize, int[] sampleRate,
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
