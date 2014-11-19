package assignment7;


public class hashfp {
	
	public static long gethash(String[] args) {
		// given a set of n inputs of the form (time, frequency)
		// sums the time differences in addition to the frequencies
		// then returns the hash of the value
		// using the FNV1a hash implemented below
		long sum = 0;
		int split, n = args.length;
		String time, freq;
		long[] times = new long[n];
		
		for (int i = 0; i < n; i++) {
			// decompose the string time:freq into its constituents
			split = args[i].indexOf(":");
			time = args[i].substring(0, split);
			freq = args[i].substring(split+1);
			// parse the strings into longs
			times[i] = Long.parseLong(time);
			sum += Long.parseLong(freq);
		}
		
		quicksort(times);
		sum += get_deltas_sum(times, n);
		return FNV1a(sum);
		
	}

	// 32bit hash algorithm
	// NOTE: use longs because java does not have unsigned ints
	private static long FNV1a(long fp) {
		// 32bit parameters
		final long FNV_prime = 16777619; // 2^24 + 2^8 + 0x93
		Long offset_basis_object = new Long("2166136261");
		final long offset_basis = offset_basis_object.longValue();
		
		// max value available to a 32bit number
		Long max_value_object = new Long("4294967295");
		final long max_value = max_value_object.longValue();
		
		// use bitwise operator & with the following longs
		// to get the corresponding octet
		long[] octets = { (long) 255 << 24, 255 << 16, 255 << 8, 255 };
		
		// the 32bit algorithm
		long hash = offset_basis;
		for (int i = 3; i >= 0; i--) {
			hash = hash ^ (fp & octets[i]);
			hash = (hash * FNV_prime) % max_value;
		}
		return hash;
	}
	
	// computes the sum of the time deltas
	private static long get_deltas_sum(long[] times, int n) {
		long deltas_sum = 0;
		quicksort(times); // quicksort or whatever
		for (int i = 0; i < n; i++) {
			// because each term appears 2i - n + 1 times,
			deltas_sum += times[i] * (2*i - n + 1);
		}
		return deltas_sum;
	}
	
	// quicksort helper function
	private static void sort(long[] L, int low, int high) {
		// long[] above, pivot, below;
		int size = high - low;
		if (size < 2) { }
		else if (size == 2) {
			if (L[low] > L[low + 1]) {
				long temp = L[low];
				L[low] = L[low + 1];
				L[low + 1] = temp;
			}
		}
		else {
			long pivot = median(L[low], L[(int) (high + low) / 2], L[high-1]);
			long[] above = new long[size];
			long[] pivots = new long[size];
			long[] below = new long[size];
			int above_cnt = 0, pivots_cnt = 0, below_cnt = 0;
			
			int i;
			
			for (i = low; i < high; i++) {
				if (L[i] > pivot) above[above_cnt++] = L[i];
				else if (L[i] < pivot) below[below_cnt++] = L[i];
				else pivots[pivots_cnt++] = L[i];
			}
			
			int middle = below_cnt + pivots_cnt;
			if (below.length > 0) { 
				for (i = 0; i < below_cnt; i++)
					L[low + i] = below[i];
			}
			
			for (i = 0; i < pivots_cnt; i++)
				L[low + below_cnt + i] = pivots[i];
			
			if (above.length > 0) {
				for (i = 0; i < above_cnt; i++)
					L[low + below_cnt + pivots_cnt + i] = above[i];
			}
			
			sort(L, low, low + below_cnt); // sort everything below pivots
			sort(L, low + middle, high); // sort everything above pivots
		}
		
	}
	
	// quicksort: O(nlogn)
	private static void quicksort(long[] L) {
		sort(L, 0, L.length);
	}

	// returns the median value of three numbers
	private static long median(long l1, long l2, long l3) {
		// first check if l1 is a max
		if (l1 > l2 & l1 > l3) {
			if (l2 > l3) return l2;
			else return l3;
		}
		// then check if l2 is a min
		else if (l1 < l2 & l1 < l3) {
			if (l2 < l3) return l2;
			else return l3;
		}
		// if not, then l1 is the median
		else return l1;
	}
	
}
