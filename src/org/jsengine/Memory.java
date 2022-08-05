package org.jsengine;

import org.jsengine.utils.Pointer;
import org.jsengine.v8.Internal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import java.util.TreeSet;
import java.util.SortedSet;
import java.util.Comparator;
import java.util.Iterator;

// A class representation of memory management of machine that runs v8.
// this class is not included on v8 classes

public class Memory {
	private static SortedSet<MemoryAddress> virtual_memory_address_list = new TreeSet<MemoryAddress>(new MemoryAddressComparator());
	private static Map<Long, PageFrame> virtual_memory = new HashMap<Long, PageFrame>();
	private static long addr = 0x01; 
	
	public static Pointer<Object> allocate() {
		return allocate((Object) null);
	}
	
	public static Pointer<Object> allocate(long size) {
		MemoryAddress mem_addr = getNextAvailableAddress(size);
		PageFrame<Object> page = new PageFrame<Object>(null, mem_addr.start, mem_addr.offset);
		virtual_memory.put(mem_addr.start, page);
		return new Pointer<Object>(mem_addr.start);
	}
	
	// used when we want to allocate an object to heap
	// the same with using "new" keyword in c++
	public static <T> Pointer<T> allocate(T data) {
		long size = 1; // default size of an empty class, char and boolean
		if (data instanceof Integer)
			size = 4;
		else if (data instanceof String)
			size = ((String) data).length() + 1; // '\0' terminated
		else if (data instanceof Long)
			size = 8;
		else if (data instanceof Float)
			size = 4;
		
		MemoryAddress mem_addr = getNextAvailableAddress(size);
		PageFrame<T> page = new PageFrame<T>(data, mem_addr.start, mem_addr.offset);
		virtual_memory.put(mem_addr.start, page);
		
		return new Pointer<T>(mem_addr.start);
	}
	
	public static boolean free(long address, long size) {
		Iterator<MemoryAddress> mem_addrs = virtual_memory_address_list.iterator();
		long offset = address + size - 1;
		while (mem_addrs.hasNext()) {
			MemoryAddress mem_addr = mem_addrs.next();
			if (mem_addr.start == address && mem_addr.offset == offset) {
				virtual_memory.remove(mem_addr.start);
				virtual_memory_address_list.remove(mem_addr);
				return true;
			}
		}
		
		return false;
	}
	
	public static <T> void store(long address, T data) {
		PageFrame<T> page = virtual_memory.get(address);
		// TODO: implement a MemoryAddress existence checking here
		if (page == null)
			virtual_memory.put(address, new PageFrame<T>(data, address, 1));
		else
			page.data = data;
	}
	
	public static Object load(long index) {
		PageFrame page_frame = virtual_memory.get(index);
		if (page_frame == null)
			throw new RuntimeException("accesing unallocated memory.");
		else 
			return page_frame.data;
	}
	
	private static boolean isAddressAvailable(long start, long end) {
		for(MemoryAddress addr : virtual_memory_address_list) {
			if(start >= addr.start && end <= addr.offset) {
				return false;
			}
		}
		return true;
	}
	
	public static boolean isAddressReserved(long address) {
		for(MemoryAddress addr : virtual_memory_address_list) {
			if(address >= addr.start && address <= addr.offset) {
				return true;
			}
		}
		return false;
	}
	
	public static Pointer<Object> reserve(long start, long offset) {
		MemoryAddress addr = reserveAddress(start, offset);
		if (addr == null)
			return Globals.nullptr;
		else
			return new Pointer<Object>(addr.start);
	}
	
	public static Pointer<Object> commit(long start, long offset) {
		return commitReserveAddress(start, offset);
	}
	
	private static MemoryAddress getNextAvailableAddress(long size) {
		Iterator<MemoryAddress> mem_addrs = virtual_memory_address_list.iterator();
		MemoryAddress prev_addr = null;
		
		while (mem_addrs.hasNext()) {
			MemoryAddress current_addr = mem_addrs.next();
			if (!mem_addrs.hasNext())
					return new MemoryAddress(current_addr.offset + 1, current_addr.offset + size, true, true);
					
			if (prev_addr == null) {
				prev_addr = current_addr;
				
				if (mem_addrs.hasNext())
					continue;
			}
			
			if ((prev_addr.offset + 1) == current_addr.start) {
				prev_addr = current_addr;
				continue;
			}
				
			if ((prev_addr.offset + size) < current_addr.start)
				return new MemoryAddress(prev_addr.offset + 1, prev_addr.offset + size, true, true);
		}
		
		return new MemoryAddress(1, size, true, true);
	}
	
	private static MemoryAddress reserveAddress(long hint, long size) {
		System.out.println("Memory:reserveAddress::hint : " + hint);
		if (hint == 0) {
			MemoryAddress addr = getNextAvailableAddress(size);
			addr.committed = false;
			return addr;
		}
		Iterator<MemoryAddress> mem_addrs = virtual_memory_address_list.iterator();
		MemoryAddress prev_addr = null;
		long offset = hint + size - 1;
		
		while (mem_addrs.hasNext()) {
			MemoryAddress current_addr = mem_addrs.next();
			if (hint >= current_addr.start && hint <= current_addr.offset || offset >= current_addr.start && offset <= current_addr.offset) {
				return null;
			}
		}
		
		return new MemoryAddress(hint, offset, true, false);
	}
	
	private static Pointer<Object> commitReserveAddress(long address, long size) {
		Iterator<MemoryAddress> mem_addrs = virtual_memory_address_list.iterator();
		long offset = address + size - 1;
		while (mem_addrs.hasNext()) {
			MemoryAddress mem_addr = mem_addrs.next();
			if (mem_addr.start == address && mem_addr.offset == offset) {
				PageFrame<Object> page = new PageFrame<Object>(null, mem_addr.start, mem_addr.offset);
				virtual_memory.put(mem_addr.start, page);
				mem_addr.committed = true;
				return new Pointer<Object>(mem_addr.start);
			}
		}
		
		return Globals.nullptr;
	}
	
	private static class PageFrame<T> {
		public T data;
		public long address = 0;
		public long offset = 0;
		public PageFrame(T data, long address, long offset) {
			this.data = data;
			this.address = address;
			this.offset = offset;
		}
	}
	
	private static class MemoryAddress {
		public long start = 0L;
		public long offset = 0L;
		public boolean reserved = false;
		public boolean committed = false;
		
		public MemoryAddress(long start, long offset, boolean reserved, boolean committed) {
			this.start = start;
			this.offset = offset;
			this.reserved = reserved;
			this.committed = committed;
			//System.out.println("Memory::MemoryAddress::start : " + start );
			if (reserved)
				virtual_memory_address_list.add(this);
			//System.out.println("Memory::virtual_memory_address_list : " + virtual_memory_address_list );
		}
		
		public MemoryAddress(long start, long offset, boolean reserved) {
			this(start, offset, reserved, false);
		}
		
		public MemoryAddress(long start, long offset) {
			this(start, offset, false, false);
		}
		
		public String toString() {
			return "0x" + start;
		}
		
		public static MemoryAddress test(long start) {
			return new MemoryAddress(start, start, false, false);
		}
	}
	
	private static class MemoryAddressComparator implements Comparator<MemoryAddress> {
		@Override
		public int compare(MemoryAddress ma1, MemoryAddress ma2) {
			if (ma1.start >= ma2.start && ma1.start <= ma2.offset || ma1.offset >= ma2.start && ma1.offset <= ma2.offset)
					return 0;
			return (int) (ma1.start - ma2.start);
		}
	}
}