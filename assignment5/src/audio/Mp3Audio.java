package audio;

class Mp3Audio extends Audio{

	public Mp3Audio(String filePath) {
		super(filePath);
		// TODO Auto-generated constructor stub
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

}
