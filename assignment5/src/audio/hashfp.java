
public class hashfp {

	public static long main(String[] args) {
		// given a set of n inputs of the form (time, frequency)
		// sums the time differences in addition to the frequencies
		// then returns the hash of the value
		// using the FNV1a hash implemented below
		long sum = 0;
		int n = args.length;
		long[] times;
		for (int i = 0; i < n; i++) {
			times[i] = args[i][0]; // instead of [0], use appropriate acccesor for time
			sum += args[i][1];  // instead of [1], use appropriate accessor for frequency
		}
		sum += get_deltas_sum(times, n);
		return FNV1a(sum);
		
	}

	// 32 bit hash algorithm
	private static long FNV1a(long fp) {
		// 32 bit hash value
		final long FNV_prime = 16777619; // 2^24 + 2^8 + 0x93
		Long offset_basis_object = new Long("2166136261");
		long offset_basis = offset_basis_object.longValue();
		
		// use bitwise operator & with the following longs
		// to get the corresponding octet
		long[] octets = { 255 << 24, 255 << 16, 255 << 8, 255 };
		
		// the 32bit algorithm
		long hash = offset_basis;
		for (int i = 0; i < 4; i++) {
			hash = hash ^ (fp & octets[i]);
			hash = hash * FNV_prime;
		}
		return hash;
	}
	
	// computes the sum of the time deltas
	private static long get_deltas_sum(long[] times, int n) {
		long deltas_sum = 0;
		sort(times); // quicksort or whatever
		for (int i = 0; i < n; i++) {
			// because each term appears 2i - n + 1 times,
			deltas_sum += times[i] * (2*i - n + 1);
		}
		return deltas_sum;
	}
}
