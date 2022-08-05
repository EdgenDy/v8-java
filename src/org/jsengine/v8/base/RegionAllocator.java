package org.jsengine.v8.base;

import org.jsengine.v8.internal.Address;
import org.jsengine.v8.Base;
import org.jsengine.utils.Pointer;
import org.jsengine.Memory;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class RegionAllocator {
	private boolean is_used_ = false;
	private Region whole_region_;
	private long region_size_in_pages_ = 0;
	private double max_load_for_randomization_ = 0;
	private long free_size_ = 0;
	private long page_size_ = 0;
	
	private AllRegionsSet all_regions_ = new AllRegionsSet();
	private HashSet<Pointer<Region>> free_regions_ = new HashSet<Pointer<Region>>();
	
	public RegionAllocator(Address memory_region_begin, long memory_region_size, long page_size) {
		whole_region_ = new Region(memory_region_begin, memory_region_size, false);
		region_size_in_pages_ = size() / page_size;
		max_load_for_randomization_ = size() * Base.kMaxLoadFactorForRandomization;
		free_size_ = 0;
		page_size_ = page_size;
		
		Pointer<Region> region = Pointer.init(whole_region_);
		all_regions_.add(region);
		freeListAddRegion(region);
	}
	
	public boolean allocateRegionAt(Address requested_address, long size) {
		Address requested_end = new Address(requested_address.value() + size);
		Pointer<Region> region = null;
    
		Pointer<Region> region_iter = findRegion(requested_address);
		if (region_iter == null) {
			return false;
		}

		region = region_iter;
		Region region_ = region.dereference();
		
		if (region_.is_used() || region_.end().lessThan(requested_end)) {
			return false;
		}
		
		if (region_.begin() != requested_address) {
			long new_size = requested_address.value() - region_.begin().value();
			region = split(region, new_size);
			region_ = region.dereference();
		}
		
		freeListRemoveRegion(region);
		region_.set_is_used(true);

		return true;
	}
	
	public long size() { 
		return whole_region_.size();
	}
	
	public void freeListAddRegion(Pointer<Region> region) {
		free_size_ += region.dereference().size();
		free_regions_.add(region);
	}
	
	public Pointer<Region> findRegion(Address address) {
		if (!whole_region_.contains(address)) return null;

		Region key = new Region(address, 0, false);
		Pointer<Region> iter = null;
		Iterator<Pointer<Region>> iterator = all_regions_.iterator();
		
		while(iterator.hasNext()) {
			Region region = iterator.next().dereference();
			if(region.begin().value() == key.begin().value() && region.size() == key.size() && region.is_used() == key.is_used()) {
				if(iterator.hasNext()) {
					iter = iterator.next();
				}
				break;
			}
		}
		
		return iter;
	}
	
	public void freeListRemoveRegion(Pointer<Region> region) {
		free_size_ -= region.dereference().size();
		free_regions_.remove(region);
	}
	
	public Pointer<Region> split(Pointer<Region> region, long new_size) {
		Region region_ = region.dereference();
		boolean used = region_.is_used();
		Pointer<Region> new_region = Pointer.init(new Region(new Address(region_.begin().value() + new_size), region_.size() - new_size, used));
		
		if (!used) {
			freeListRemoveRegion(region);
		}
		region_.set_size(new_size);

		all_regions_.add(new_region);

		if (!used) {
			freeListAddRegion(region);
			freeListAddRegion(new_region);
		}
		return new_region;
	}
	
	private static class Region extends AddressRegion {
		private boolean is_used_ = false;
		
		public Region(Address address, long size, boolean is_used) {
			super(address, size);
			is_used_ = is_used;
		}
		
		public boolean is_used() {
			return is_used_;
		}
		
		public void set_is_used(boolean used) {
			is_used_ = used;
		}
	}
	
	private static class AllRegionsSet extends HashSet<Pointer<Region>> {
		public AllRegionsSet() {
			super();
		}
	}
}