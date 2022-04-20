package org.jsengine.v8.base;

public class RandomNumberGenerator {
	private int initial_seed_;
	private int state0_ = 51; // this is a random value of a uninitialized uint64_t
	private int state1_ = 16; // this is a random value of a uninitialized uint64_t
	
	public void setSeed(int seed) {
		initial_seed_ = seed;
		state0_ = murmurHash3(seed);
		state1_ = murmurHash3(~state0_);
	}
	
	public int murmurHash3(int h) {
		h ^= h >> 33;
		h *= 0xFF51AFD; //7ED558CCD; TODO: convert this to BigInt
		h ^= h >> 33;
		h *= 0xC4CEB9F; //E1A85EC53; TODO: convert this to BigInt
		h ^= h >> 33;
		return h;
	}
	
	public static void xorShift128(int state0, int state1, int[] states) {
		int s1 = state0;
		int s0 = state1;
		states[0] = s0;
		s1 ^= s1 << 23;
		s1 ^= s1 >> 17;
		s1 ^= s0;
		s1 ^= s0 >> 26;
		states[1] = s1;
	}
	
	public int next(int bits) {
		int states[] = new int[2];
		xorShift128(state0_, state1_, states);
		state0_ = states[0];
		state1_ = states[1];
		return ((state0_ + state1_) >> (64 - bits));
	}
	
	public void nextBytes(byte buffer[], int buflen) {
		for (int n = 0; n < buflen; ++n) {
			buffer[n] = (byte) ((char) next(8));
		}
	}
}