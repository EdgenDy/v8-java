package org.jsengine.v8.base;

public class RandomNumberGenerator {
	private int initial_seed_;
	private int state0_;
	private int state1_;
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
}